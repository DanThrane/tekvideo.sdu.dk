package dk.sdu.tekvideo

import dk.sdu.tekvideo.stats.ExerciseProgress
import dk.sdu.tekvideo.stats.ExerciseVisit
import dk.sdu.tekvideo.stats.ProgressBodyCell
import dk.sdu.tekvideo.stats.ProgressHeadCell
import dk.sdu.tekvideo.stats.ProgressTable
import dk.sdu.tekvideo.stats.ProgressionStatus
import dk.sdu.tekvideo.stats.StandardViewingStatisticsConfiguration
import dk.sdu.tekvideo.stats.StatsGuestUser
import dk.sdu.tekvideo.stats.UserProgression
import dk.sdu.tekvideo.stats.ViewingStatisticsConfiguration
import dk.sdu.tekvideo.stats.WeeklyViewingStatisticsConfiguration
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

    String linkToNode(Node node) {
        if (node instanceof Course) return linkToCourse(node)
        if (node instanceof Subject) return linkToSubject(node)
        if (node instanceof Video) return linkToVideo(node)
        if (node instanceof WrittenExerciseGroup) return linkToWrittenExercise(node)
        else throw new IllegalArgumentException("Unknown node type: ${node.class.name}")
    }

    String linkToRoot() {
        return generateLink(null, null)
    }

    String linkToCourse(Course course) {
        generateLink("course", course.id)
    }

    String linkToSubject(Subject subject) {
        generateLink("subject", subject.id)
    }

    String linkToVideo(Video video) {
        generateLink("video", video.id)
    }

    String linkToWrittenExercise(WrittenExerciseGroup group) {
        generateLink("written", group.id)
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

        def table = new ProgressTable()
        def count = 80
        for (i in 1..count) {
            def headCell = new ProgressHeadCell()
            headCell.title = "Dummy cell ${i}"
            headCell.url = "#${i}"
            headCell.maxScore = i * 10
            table.head.add(headCell)
        }

        for (userIdx in 1..100) {
            def progression = new UserProgression()
            progression.user = new StatsGuestUser(token: userIdx.toString())
            for (i in 1..count) {
                def bodyCell = new ProgressBodyCell()
                bodyCell.status = ProgressionStatus.values()[
                        (userIdx * i + 317) % ProgressionStatus.values().size()]
                bodyCell.score = (userIdx * i + 1337) % table.head[i - 1].maxScore
                progression.cells.add(bodyCell)
            }
            table.body.add(progression)
        }
        return ok(table)
    }

    private ViewingStatistics views(List<Exercise> leaves, ViewingStatisticsConfiguration config) {
        def progress = ExerciseProgress.findAllByExerciseInList(leaves)
        def views = ExerciseVisit.findAllByProgressInList(progress)

        if (config instanceof WeeklyViewingStatisticsConfiguration) {
            return weeklyViewsGraph(views)
        } else if (config instanceof StandardViewingStatisticsConfiguration) {
            def timestamps = views.collect { it.timestamp.time }
            def start = timestamps.min()
            def end = timestamps.max()
            return standardViewsGraph(views, start, end, 30, config.cumulative)
        } else {
            throw new IllegalArgumentException("Unknown configuration type ${config}")
        }
    }

    ServiceResult<ViewingStatistics> courseViews(Course course, ViewingStatisticsConfiguration config) {
        def accessCheck = checkAccess(course)
        if (!accessCheck.success) return accessCheck

        def leaves = Exercise.executeQuery("""
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
        if (timePerBucket == 0) timePerBucket = 1

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
