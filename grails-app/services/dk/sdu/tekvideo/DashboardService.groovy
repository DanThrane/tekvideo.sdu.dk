package dk.sdu.tekvideo

import dk.sdu.tekvideo.events.AnswerQuestionEvent
import dk.sdu.tekvideo.events.VisitVideoEvent
import grails.util.Triple
import org.apache.http.HttpStatus
import org.hibernate.SessionFactory

import java.sql.Timestamp
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.stream.Collectors

import static dk.sdu.tekvideo.ServiceResult.*

import grails.transaction.Transactional

@Transactional
class DashboardService {
    private static final DateTimeFormatter TIME_PATTERN = DateTimeFormatter.ofPattern("dd/MM HH:mm")

    def urlMappingService
    def videoService
    CourseManagementService courseManagementService
    SessionFactory sessionFactory

    private Course findCourse(Node node) {
        while (node != null && node.parent != null) {
            node = node.parent
        }
        return (node instanceof Course) ? node : null
    }

    /**
     * Returns a node from its identifier.
     *
     * The identifier is of the form "<type>/<id>". The type represents the type of node, these can be
     * ("course", "subject", or "video"). The identifier corresponds to the database, i.e. Course#id, Subject#id, and
     * Video#id.
     *
     * This method will fail if any of the following conditions are met:
     *
     * <ul>
     *   <li>The identifier is not of the correct format</li>
     *   <li>The identifier is of the correct format, but the identifier doesn't point to a valid entry</li>
     *   <li>The identifier is not a valid number</li>
     * </ul>
     *
     * @param identifier The identifier
     * @return The corresponding node, if successful. Otherwise an appropriate error will be returned
     */
    ServiceResult<Node> nodeFromIdentifier(String identifier) {
        def split = identifier.split("/")
        if (split.length != 2) {
            return fail(message: "Invalid identifier")
        } else {
            try {
                def id = Long.parseLong(split[1])
                Node result = null
                switch (split[0]) {
                    case "course":
                        result = Course.get(id)
                        break
                    case "subject":
                        result = Subject.get(id)
                        break
                    case "video":
                        result = Video.get(id)
                        break
                }
                if (result != null) {
                    return ok(item: result)
                } else {
                    return fail(message: "Node not found!")
                }
            } catch (NumberFormatException ignored) {
                return fail(message: "Invalid ID")
            }
        }
    }

    /**
     * Finds the leaves (i.e. videos) starting at a particular node.
     *
     * This method will fail if the node is of an unknown type, or if the authenticated user doesn't own the course.
     *
     * No ordering of the videos are preserved.
     *
     * @param node The node to start searching at
     * @return A list of videos (unordered).
     */
    ServiceResult<List<Video>> findLeaves(Node node) {
        def course = findCourse(node)
        if (course != null && courseManagementService.canAccess(course)) {
            if (node instanceof Course) {
                def subjects = course.subjects
                ok item: SubjectVideo.findAllBySubjectInList(subjects).video
            } else if (node instanceof Subject) {
                ok item: node.videos
            } else if (node instanceof Video) {
                ok item: [node]
            } else {
                fail message: "Ukendt type", suggestedHttpStatus: 500
            }
        } else {
            fail message: "Du har ikke rettigheder til at tilgå dette kursus", suggestedHttpStatus: 403
        }
    }

    /**
     * Returns viewing statistics for a list of videos over some period of time.
     *
     * The views are additive in the way that, the output will count the total number of views for all of the videos,
     * it will return the individual views for each video.
     *
     * All videos must be owned by the authenticated user, otherwise this method will fail.
     *
     * The number of data points will always be 24.
     *
     * @param leaves The leaves to gather data from
     * @param periodFrom From when data should be gathered (timestamp [is milliseconds])
     * @param periodTo Until when data should be gathered (timestamp [is milliseconds])
     * @return Viewing statistics for the relevant nodes
     */
    ServiceResult<ViewingStatistics> findViewingStatistics(List<Video> leaves, Long periodFrom, Long periodTo,
                                                           Boolean cumulative) {
        if (leaves == null) leaves = []
        def videoIds = leaves.stream().map { it.id }.collect(Collectors.toList())

        def ownsAllVideos = leaves
                .collect { findCourse(it) }
                .toSet()
                .stream()
                .allMatch { courseManagementService.canAccess(it) }

        if (!ownsAllVideos) {
            return fail(message: "Du har ikke rettigheder til at tilgå dette kursus", suggestedHttpStatus: 403)
        }

        List<VisitVideoEvent> events
        events = VisitVideoEvent.findAllByVideoIdInListAndTimestampBetween(videoIds, periodFrom, periodTo).findAll {
            it != null
        }
        long periodInMs = (periodTo - periodFrom) / 24

        List<String> labels = []
        List<Integer> data = []
        // Generate some labels (X-axis)
        long counter = periodFrom
        (0..23).each {
            labels.add(TIME_PATTERN.format(new Date(counter).toInstant().atZone(ZoneId.systemDefault())))
            data.add(0)
            counter += periodInMs
        }
        events.each {
            long time = it.timestamp
            int index = (time - periodFrom) / periodInMs
            if (index > 0) {
                data[index]++
            }
        }

        if (cumulative) {
            (1..23).each { index ->
                data[index] += data[index - 1]
            }
        }
        return ok(new ViewingStatistics(labels: labels, data: data))
    }

    /**
     * Finds the most popular videos for a given list of videos.
     *
     * The most popular videos are determined from the absolute number of hits a video gets. They will be returned
     * from in order from most popular to least popluar.
     *
     * The currently authenticated user must own all the videos, otherwise this method will fail.
     *
     * @param leaves The videos to retrieve statistics for
     * @param periodFrom From when data should be gathered (timestamp [is milliseconds])
     * @param periodTo Until when data should be gathered (timestamp [is milliseconds])
     * @return
     */
    ServiceResult<List<PopularVideoStatistic>> findPopularVideos(List<Video> leaves, Long periodFrom, Long periodTo) {
        if (leaves == null) leaves = []
        def videoIds = leaves.stream().map { it.id }.collect(Collectors.toList())

        if (!videoIds) return fail("no videos")

        def ownsAllVideos = leaves
                .collect { findCourse(it) }
                .toSet()
                .stream()
                .allMatch { courseManagementService.canAccess(it) }

        if (!ownsAllVideos) {
            return fail(message: "Du har ikke rettigheder til at tilgå dette kursus", suggestedHttpStatus: 403)
        }

        String query = $/
            SELECT
                event.video_id,
                answers.answerCount,
                answers.correctAnswers,
                COUNT(*) AS visits
            FROM
                event LEFT OUTER JOIN (
                    SELECT
                        e.video_id,
                        COUNT(*) AS answerCount,
                        SUM(CASE e.correct WHEN TRUE THEN 1 ELSE 0 END) AS correctAnswers
                    FROM
                        event e
                    WHERE
                        e.class='dk.sdu.tekvideo.events.AnswerQuestionEvent' AND
                        e.timestamp >= :from_timestamp AND
                        e.timestamp <= :to_timestamp AND
                        e.video_id IN :video_ids
                    GROUP BY
                        e.video_id
                    ORDER BY
                        COUNT(*) DESC
                ) AS answers ON (event.video_id = answers.video_id)
            WHERE
                class='dk.sdu.tekvideo.events.VisitVideoEvent' AND
                event.video_id IN :video_ids AND
                event.timestamp >= :from_timestamp AND
                event.timestamp <= :to_timestamp
            GROUP BY
                event.video_id, answers.answerCount, answers.correctAnswers
            ORDER BY COUNT(*) DESC
            LIMIT 5;
            /$

        def resultList = sessionFactory.currentSession
                .createSQLQuery(query)
                .setParameterList("video_ids", videoIds)
                .setLong("from_timestamp", periodFrom)
                .setLong("to_timestamp", periodTo)
                .list()

        return ok(resultList.collect {
            def video = Video.get(it[0] as Long)

            new PopularVideoStatistic([
                    "videoId"       : it[0],
                    "videoName"     : video.name,
                    "subjectName"   : video.subject.name,
                    "courseName"    : video.subject.course.name,
                    "answerCount"   : it[1] ?: 0,
                    "correctAnswers": it[2] ?: 0,
                    "visits"        : it[3] ?: 0,
            ])
        })
    }

    ServiceResult<List<DetailedComment>> findRecentComments(List<Video> leaves, Long periodFrom, Long periodTo) {
        if (leaves == null) leaves = []
        def videoIds = leaves.stream().map { it.id }.collect(Collectors.toList())

        if (!videoIds) return fail("no videos")
        if (periodFrom == null || periodTo == null || periodTo < periodFrom || periodFrom < 0 || periodTo < 0)
            return fail(message: "bad request", suggestedHttpStatus: HttpStatus.SC_BAD_REQUEST)

        def ownsAllVideos = leaves
                .collect { findCourse(it) }
                .toSet()
                .stream()
                .allMatch { courseManagementService.canAccess(it) }

        if (!ownsAllVideos) {
            return fail(message: "Du har ikke rettigheder til at tilgå dette kursus", suggestedHttpStatus: 403)
        }

        String query = $/
            SELECT
                myusers.username,
                user_id AS user_id,
                video.id AS video_id,
                video.name AS video_title,
                comment.date_created,
                contents,
                comment_id
            FROM
                comment,
                video_comment,
                myusers,
                video
            WHERE
                video_comment.comment_id = comment.id AND
                video_comment.video_comments_id IN :video_ids AND
                comment.date_created >= to_timestamp(:from_timestamp) AND
                comment.date_created <= to_timestamp(:to_timestamp) AND
                myusers.id = comment.user_id AND
                video.id = video_comment.video_comments_id
            ORDER BY
                video_comment.video_comments_id;
        /$

        def resultList = sessionFactory.currentSession
                .createSQLQuery(query)
                .setParameterList("video_ids", videoIds)
                .setLong("from_timestamp", (long) (periodFrom / 1000))
                .setLong("to_timestamp", (long) (periodTo / 1000))
                .list()

        return ok(resultList.collect {
            new DetailedComment([
                    "username"   : it[0],
                    "userId"     : it[1],
                    "videoId"    : it[2],
                    "videoTitle" : it[3],
                    "dateCreated": (it[4] as Timestamp).toInstant().epochSecond * 1000,
                    "comment"    : it[5],
                    "commentId"  : it[6],
                    "videoUrl"   : urlMappingService.generateLinkToVideo(Video.get(it[2]), [absolute: true])
            ])
        })
    }

    Set<Student> findStudents(Node node) {
        return CourseStudent.findAllByCourse(findCourse(node)).student // TODO Don't think this is efficient
    }

    ServiceResult<List<AnswerQuestionEvent>> getAnswers(Video video, Long periodFrom, Long periodTo) {
        if (video) {
            if (courseManagementService.canAccess(video.subject.course)) {
                ok item: AnswerQuestionEvent.findAllByVideoIdAndTimestampBetween(video.id, periodFrom, periodTo)
            } else {
                fail message: "You are not authorized to access this video!", suggestedHttpStatus: 403
            }
        } else {
            fail message: "Unknown video", suggestedHttpStatus: 404
        }
    }

    ServiceResult<Map> findParticipation(Node node, Long periodFrom, Long periodTo) {
        if (node instanceof Course) {
            return findCourseParticipation(node, periodFrom, periodTo)
        } else if (node instanceof Subject) {
            return findSubjectParticipation(node, periodFrom, periodTo)
        } else if (node instanceof Video) {
            return findVideoParticipation(node, periodFrom, periodTo)
        }
        return fail([:])
    }

    ServiceResult<UserParticipationReport> findSubjectParticipation(Subject subject, Long periodFrom, Long periodTo) {
        if (courseManagementService.canAccess(subject.course)) {
            def videoIds = SubjectVideo.findAllBySubject(subject).collect { it.videoId }
            def videos = Video.findAllByIdInList(videoIds)

            def answerEvents = AnswerQuestionEvent.findAllByVideoIdInListAndTimestampBetween(videoIds,
                    periodFrom, periodTo)
            def visitEvents = VisitVideoEvent.findAllByVideoIdInListAndTimestampBetween(videoIds, periodFrom, periodTo)

            def studentUserIds = Student.findAllByIdInList(
                    CourseStudent.findAllByCourse(subject.course).collect { it.studentId }
            ).collect { it.userId }
            def userIdsWithAnswers = answerEvents.collect { it.userId }
            def userIdsWithVisits = visitEvents.collect { it.userId }
            List<User> allUsers = User.findAllByIdInList(studentUserIds + userIdsWithAnswers + userIdsWithVisits)

            return ok(item: findParticipation2(answerEvents, visitEvents, allUsers, studentUserIds.toSet(), videos))
        } else {
            return fail([:])
        }
    }

    ServiceResult<Map> findVideoParticipation(Video video, Long periodFrom, Long periodTo) {
        if (courseManagementService.canAccess(video.subject.course)) {
            def answerEvents = AnswerQuestionEvent.findAllByVideoIdAndTimestampBetween(video.id,
                    periodFrom, periodTo)
            def visitEvents = VisitVideoEvent.findAllByVideoIdAndTimestampBetween(video.id, periodFrom, periodTo)

            def studentUserIds = Student.findAllByIdInList(
                    CourseStudent.findAllByCourse(video.subject.course).collect { it.studentId }
            ).collect { it.userId }
            def userIdsWithAnswers = answerEvents.collect { it.userId }
            def userIdsWithVisits = visitEvents.collect { it.userId }
            List<User> allUsers = User.findAllByIdInList(studentUserIds + userIdsWithAnswers + userIdsWithVisits)

            return ok(item: findParticipation2(answerEvents, visitEvents, allUsers, studentUserIds.toSet(), [video]))
        } else {
            fail([:])
        }
    }

    ServiceResult<Map> findCourseParticipation(Course course, Long periodFrom, Long periodTo) {
        if (courseManagementService.canAccess(course)) {
            def videoIds = SubjectVideo.findAllBySubjectInList(course.activeSubjects).collect { it.videoId }
            def videos = Video.findAllByIdInList(videoIds)

            def answerEvents = AnswerQuestionEvent.findAllByVideoIdInListAndTimestampBetween(videoIds,
                    periodFrom, periodTo)
            def visitEvents = VisitVideoEvent.findAllByVideoIdInListAndTimestampBetween(videoIds, periodFrom, periodTo)

            def studentUserIds = Student.findAllByIdInList(
                    CourseStudent.findAllByCourse(course).collect { it.studentId }
            ).collect { it.userId }
            def userIdsWithAnswers = answerEvents.collect { it.userId }
            def userIdsWithVisits = visitEvents.collect { it.userId }
            List<User> allUsers = User.findAllByIdInList(studentUserIds + userIdsWithAnswers + userIdsWithVisits)

            return ok(item: findParticipation2(answerEvents, visitEvents, allUsers, studentUserIds.toSet(), videos))
        } else {
            return fail([:])
        }
    }

    private Map findParticipation2(List<AnswerQuestionEvent> answerEvents, List<VisitVideoEvent> visitEvents,
                                   List<User> allUsers, Set<Long> studentUserIds, List<ExerciseNode> exercises) {
        Map result = [
                users    : [],
                exercises: [:]
        ]

        exercises.each {
            def description = [
                    type  : it.identifier.type,
                    id    : it.identifier.id,
                    nodeId: it.identifier.toString()
            ]
            def name = it instanceof Video ? it.name : "?"
            description.name = name

            if (it instanceof Video) {
                def timeline = videoService.getTimeline(it)
                def sizes = timeline.subjects.collect {
                    it.questions.collect { it.fields.size() }
                }.flatten()

                description.maxPoints = (sizes.sum() ?: 0)
            }

            result.exercises[it.identifier.toString()] = description
        }

        def answersByUser = answerEvents.groupBy { it.userId }
        def visitsByUser = visitEvents.groupBy { it.userId }

        allUsers.each { user ->
            def answers = answersByUser[user.id] ?: []
            def visits = visitsByUser[user.id] ?: []
            def visitsByExercise = visits.groupBy { it.videoId }

            def description = [
                    id       : user.id,
                    username : user.username,
                    isStudent: user.id in studentUserIds,
                    isCas    : user.isCas,
                    answers  : [:]
            ]

            exercises.each { exercise ->
                if (exercise instanceof Video) {
                    def grading = [:]
                    grading.points = answers
                            .findAll { it.correct }
                            .groupBy { new Triple(it.subject, it.question, it.field) }
                            .size()

                    def visitsToExercise = visitsByExercise[exercise.id] ?: []
                    grading.seen = visitsToExercise.size() > 0
                    description.answers[exercise.identifier.toString()] = grading
                }
            }

            result.users += description
        }

        return result
    }

}
