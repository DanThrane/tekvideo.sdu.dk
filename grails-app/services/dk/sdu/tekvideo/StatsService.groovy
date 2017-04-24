package dk.sdu.tekvideo

import dk.sdu.tekvideo.stats.ExerciseProgress
import dk.sdu.tekvideo.stats.ExerciseVisit
import dk.sdu.tekvideo.stats.ProgressBodyCell
import dk.sdu.tekvideo.stats.ProgressHeadCell
import dk.sdu.tekvideo.stats.ProgressTable
import dk.sdu.tekvideo.stats.ProgressionStatus
import dk.sdu.tekvideo.stats.StandardViewingStatisticsConfiguration
import dk.sdu.tekvideo.stats.StatsAuthenticatedUser
import dk.sdu.tekvideo.stats.StatsGuestUser
import dk.sdu.tekvideo.stats.StatsUser
import dk.sdu.tekvideo.stats.UserProgression
import dk.sdu.tekvideo.stats.ViewingStatisticsConfiguration
import dk.sdu.tekvideo.stats.WeeklyViewingStatisticsConfiguration
import dk.sdu.tekvideo.stats.WrittenGroupStreak
import org.apache.http.HttpStatus
import org.codehaus.groovy.grails.web.mapping.LinkGenerator

import java.text.DateFormat
import java.time.DayOfWeek
import java.time.ZoneId
import java.time.format.TextStyle

import static dk.sdu.tekvideo.ServiceResult.*

class StatsService {
    def courseManagementService
    def nodeService
    def urlMappingService

    LinkGenerator grailsLinkGenerator

    ServiceResult<List<NodeBrowserInformation>> coursesForBrowser() {
        def coursesResult = courseManagementService.activeCourses
        if (!coursesResult.success) return coursesResult.convertFailure()
        return ok(convertToBrowser(coursesResult.result))
    }

    ServiceResult<List<NodeBrowserInformation>> subjectsForBrowser(Course course) {
        def accessCheck = checkAccess(course)
        if (!accessCheck.success) return accessCheck

        return ok(convertToBrowser(course.activeSubjects))
    }

    ServiceResult<List<NodeBrowserInformation>> exercisesForBrowser(Subject subject) {
        def accessCheck = checkAccess(subject)
        if (!accessCheck.success) return accessCheck

        return ok(convertToBrowser(subject.allActiveExercises))
    }

    private ServiceResult checkAccess(Node node) {
        if (!courseManagementService.canAccessNode(node)) {
            return fail(message: "Ikke tilladt", suggestedHttpStatus: HttpStatus.SC_UNAUTHORIZED)
        }
        return ok()
    }

    private List<NodeBrowserInformation> convertToBrowser(List<Node> nodes) {
        def browserInformation = nodes.collect { nodeService.getInformationForBrowser(it, false) }
        // Fix up the links such that they point to the stats dashboard
        for (int i in 0..<nodes.size()) {
            browserInformation[i].url = linkToNode(nodes[i])
        }
        return browserInformation
    }

    String generateLink(String type, Long id, String action = null, String format = null) {
        def attrs = [
                mapping: "stats",
                params : [:]
        ]

        if (type != null) {
            assert id != null
            attrs.params.type = type
            attrs.params.id = id
        }

        if (action != null) {
            attrs.params.act = action
        }

        if (format != null) {
            attrs.params.format = format
        }

        grailsLinkGenerator.link(attrs)
    }

    String linkToNode(Node node, String action = null, String format = null) {
        if (node instanceof Course) return linkToCourse(node, action, format)
        if (node instanceof Subject) return linkToSubject(node, action, format)
        if (node instanceof Video) return linkToVideo(node, action, format)
        if (node instanceof WrittenExerciseGroup) return linkToWrittenExercise(node, action, format)
        else throw new IllegalArgumentException("Unknown node type: ${node.class.name}")
    }

    String linkToRoot() {
        return generateLink(null, null)
    }

    String linkToCourse(Course course, String action = null, String format = null) {
        generateLink("course", course.id, action, format)
    }

    String linkToSubject(Subject subject, String action = null, String format = null) {
        generateLink("subject", subject.id, action, format)
    }

    String linkToVideo(Video video, String action = null, String format = null) {
        generateLink("video", video.id, action, format)
    }

    String linkToWrittenExercise(WrittenExerciseGroup group, String action = null, String format = null) {
        generateLink("written", group.id, action, format)
    }

    List<SimpleCrumb> getBreadcrumbsToNode(Node node) {
        List<SimpleCrumb> result = []
        Node current = node
        while (current != null) {
            result.add(new SimpleCrumb(current.name, linkToNode(current)))
            current = current.parent
        }
        result.add(new SimpleCrumb("Stats", linkToRoot()))
        result.reverse(true)
        result.last().active = true
        return result
    }

    ServiceResult<List<User>> studentsForCourse(Course course) {
        def accessCheck = checkAccess(course)
        if (!accessCheck.success) return accessCheck

        ok(User.executeQuery("""
            SELECT 
                s.user
            FROM 
                CourseStudent cs INNER JOIN cs.student s
            WHERE
                cs.course = :course
        """, [course: course]).asList())
    }

    ServiceResult<ProgressTable> courseProgress(Course course) {
        def accessCheck = checkAccess(course)
        if (!accessCheck.success) return accessCheck

        List<Exercise> leaves = Exercise.executeQuery("""
            SELECT se.exercise
            FROM CourseSubject cs, SubjectExercise se
            WHERE
                cs.course = :course AND
                cs.subject = se.subject AND
                cs.course.localStatus = :visible AND
                se.subject.localStatus = :visible AND
                se.exercise.localStatus = :visible
        """, [
                visible: NodeStatus.VISIBLE,
                course: course
        ])

        return ok(progressTableFromExercises(course, leaves))
    }

    ServiceResult<ProgressTable> subjectProgress(Subject subject) {
        def accessCheck = checkAccess(subject)
        if (!accessCheck.success) return accessCheck

        List<Exercise> leaves = subject.allActiveExercises
        return ok(progressTableFromExercises(subject.course, leaves))
    }

    private ProgressTable progressTableFromExercises(Course course, List<Exercise> leaves) {
        // Progress reports. Will be used to look for answers
        def progressReports = ExerciseProgress.findAllByExerciseInList(leaves)
        def reportsByUser = progressReports.groupBy { it.user }
        def reportsByUuid = progressReports.groupBy { it.uuid }

        // Find all relevant users
        List<StatsUser> usersWithAnswers = progressReports.collect {
            if (it.user != null) {
                return new StatsAuthenticatedUser(user: it.user)
            } else {
                return new StatsGuestUser(token: it.uuid)
            }
        }
        List<StatsUser> studentUsers = User.executeQuery("""
            SELECT cs.student.user
            FROM CourseStudent cs
            WHERE cs.course = :course
        """, [course: course]).collect { new StatsAuthenticatedUser(user: it) }

        // Use a set to ensure we get no duplicates from student users with answers
        Set<StatsUser> allStatsUsers = new HashSet<>(usersWithAnswers)
        allStatsUsers.addAll(studentUsers)

        // Find relevant information from these reports. For example answers and visits
        Map<ExerciseProgress, WrittenGroupStreak> writtenStreaks =
                WrittenGroupStreak.findAllByProgressInList(progressReports)
                    .groupBy { it.progress }
                    .collectEntries { [(it.key): it.value.first()] }

        Map<ExerciseProgress, Integer> visits = ExerciseVisit.findAllByProgressInList(progressReports)
                .groupBy { it.progress }
                .collectEntries { [(it.key): it.value.size()] }

        // Create ProgressTable
        def table = new ProgressTable()

        // Add header cells to table
        table.head = leaves.collect {
            new ProgressHeadCell(
                    title: it.name,
                    url: urlMappingService.generateLinkToExercise(it),
                    node: it,
                    maxScore: it.scoreToPass
            )
        }

        // Award points to every user
        for (user in allStatsUsers) {
            def row = new UserProgression()
            row.user = user

            def progressReportsByUser =
                    user instanceof StatsGuestUser ? reportsByUuid[user.token] :
                    user instanceof StatsAuthenticatedUser ? reportsByUser[user.user] :
                    null

            for (exercise in leaves) {
                def progressReport = progressReportsByUser.find { it.exercise == exercise }

                def cell = new ProgressBodyCell()
                cell.score = 0
                cell.status = ProgressionStatus.NOT_STARTED

                def maxScore = exercise.scoreToPass
                if (progressReport != null) {
                    def hasVisited = visits.getOrDefault(progressReport, 0) > 0

                    // Calculate score
                    if (exercise instanceof WrittenExerciseGroup) {
                        cell.score = writtenStreaks[progressReport]?.longestStreak ?: 0
                    } else if (exercise instanceof Video) {
                        cell.score = hasVisited ? 1 : 0
                    } else {
                        log.warn("Unknown exercise type! ${exercise.class.simpleName}")
                    }

                    cell.status = calculateStatus(hasVisited, maxScore, cell)
                }
                row.cells.add(cell)
            }

            table.body.add(row)
        }

        return table
    }

    private ProgressionStatus calculateStatus(boolean hasVisited, int maxScore, ProgressBodyCell cell) {
        if (!hasVisited) return ProgressionStatus.NOT_STARTED

        if (cell.score >= maxScore) {
            return ProgressionStatus.PERFECT
        } else if (maxScore != 0 && cell.score / maxScore > 0.45) {
            return ProgressionStatus.WORKING_ON_IT
        } else {
            return ProgressionStatus.STARTED_LITTLE_PROGRESS
        }
    }

    private ViewingStatistics views(List<Exercise> leaves, ViewingStatisticsConfiguration config) {
        def progress = ExerciseProgress.findAllByExerciseInList(leaves)
        def views = ExerciseVisit.findAllByProgressInList(progress)

        if (config instanceof WeeklyViewingStatisticsConfiguration) {
            return weeklyViewsGraph(views)
        } else if (config instanceof StandardViewingStatisticsConfiguration) {
            def timestamps = views.collect { it.timestamp.time }
            long start = timestamps.min() ?: System.currentTimeMillis()
            long end = timestamps.max() ?: start
            return standardViewsGraph(views, start, end, 30, config.cumulative)
        } else {
            throw new IllegalArgumentException("Unknown configuration type ${config}")
        }
    }

    private List<Exercise> courseLeaves(Course course) {
        Exercise.executeQuery("""
            SELECT e
            FROM 
                CourseSubject cs INNER JOIN cs.subject s, 
                SubjectExercise se INNER JOIN se.exercise e
            WHERE 
                cs.course = :course AND 
                se.subject = s AND 
                s.localStatus = :visibleStatus AND
                e.localStatus = :visibleStatus 
        """, [
                course       : course,
                visibleStatus: NodeStatus.VISIBLE
        ])
    }

    ServiceResult<ViewingStatistics> courseViews(Course course, ViewingStatisticsConfiguration config) {
        def accessCheck = checkAccess(course)
        if (!accessCheck.success) return accessCheck

        def leaves = courseLeaves(course)
        return ok(views(leaves, config))
    }

    ServiceResult<ViewingStatistics> subjectViews(Subject subject, ViewingStatisticsConfiguration config) {
        def accessCheck = checkAccess(subject)
        if (!accessCheck.success) return accessCheck

        def leaves = Exercise.executeQuery("""
            SELECT e
            FROM 
                SubjectExercise se INNER JOIN se.exercise e
            WHERE 
                se.subject = :subject AND 
                e.localStatus = :visibleStatus 
        """, [
                subject      : subject,
                visibleStatus: NodeStatus.VISIBLE
        ])
        return ok(views(leaves, config))
    }

    ServiceResult<ViewingStatistics> exerciseViews(Exercise exercise, ViewingStatisticsConfiguration config) {
        def accessCheck = checkAccess(exercise)
        if (!accessCheck.success) return accessCheck
        return ok(views([exercise], config))
    }

    private ViewingStatistics standardViewsGraph(List<ExerciseVisit> visit, long start, long end,
                                                 int numBuckets, boolean cumulative) {

        def result = new ViewingStatistics()
        def formatter = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT)
        result.data = [0] * numBuckets
        long diff = end - start

        int timePerBucket = diff / numBuckets
        if (timePerBucket == 0) timePerBucket = 1000 * 60 * 60 * 24 // Ensure that labels are unique

        result.labels = (1..numBuckets).collect {
            formatter.format(new Date((long) start + timePerBucket * it))
        }

        visit.each {
            int index = Math.min(
                    (int) ((it.timestamp.time - start) / timePerBucket),
                    (int) (numBuckets - 1)
            )
            assert (index >= 0 && index < numBuckets)
            result.data[index]++
        }

        if (cumulative) {
            for (int i in 1..<numBuckets) {
                result.data[i] += result.data[i - 1]
            }
        }
        return result
    }

    private ViewingStatistics weeklyViewsGraph(List<ExerciseVisit> visit) {
        def result = new ViewingStatistics()
        result.data = [0] * 7
        result.labels = DayOfWeek.values().collect {
            it.getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault())
        }

        visit.each {
            int day = it.timestamp.toInstant().atZone(ZoneId.systemDefault()).dayOfWeek.value - 1
            assert (day >= 0 && day < 7)
            result.data[day]++
        }
        return result
    }

}
