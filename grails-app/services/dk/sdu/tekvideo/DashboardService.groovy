package dk.sdu.tekvideo

import dk.sdu.tekvideo.events.AnswerQuestionEvent
import dk.sdu.tekvideo.events.VisitVideoEvent
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

    ServiceResult<List<Map>> findStudentActivity(Node node, List<Video> leaves, Long periodFrom, Long periodTo) {
        def course = findCourse(node)
        if (leaves == null) leaves = []
        def videoIds = leaves.stream().map { it.id }.collect(Collectors.toList())
        if (course) {
            String query = $/
                SELECT
                    course_students.username,
                    course_students.user_id,
                    course_students.elearn_id,
                    course_students.student_id,
                    answers.answer_count,
                    answers.correct_answers,
                    views.unique_views
                FROM
                  (
                    SELECT
                        myusers.username AS username,
                        myusers.elearn_id AS elearn_id,
                        myusers.id AS user_id,
                        student.id AS student_id
                    FROM
                        course,
                        course_student,
                        myusers,
                        student
                    WHERE
                        course.id = :course_id AND
                        course.id = course_student.course_id AND
                        student.id = course_student.student_id AND
                        myusers.id = student.user_id
                  ) AS course_students
                  LEFT OUTER JOIN
                  (
                    SELECT
                        user_id,
                        COUNT(*) AS answer_count,
                        SUM(CASE correct WHEN TRUE THEN 1 ELSE 0 END) AS correct_answers
                    FROM
                        event
                    WHERE
                        event.class = 'dk.sdu.tekvideo.events.AnswerQuestionEvent' AND
                        event.timestamp >= :from_timestamp AND
                        event.timestamp <= :to_timestamp AND
                        event.video_id IN :video_ids
                    GROUP BY
                        user_id
                  ) AS answers ON (answers.user_id = course_students.user_id)
                  LEFT OUTER JOIN
                  (
                    SELECT
                        user_id,
                        COUNT(DISTINCT video_id) AS unique_views
                    FROM
                        event
                    WHERE
                        event.class = 'dk.sdu.tekvideo.events.VisitVideoEvent' AND
                        event.timestamp >= :from_timestamp AND
                        event.timestamp <= :to_timestamp AND
                        event.video_id IN :video_ids
                    GROUP BY
                        user_id
                  ) AS views ON(answers.user_id = views.user_id);
            /$
            def resultList = sessionFactory.currentSession
                    .createSQLQuery(query)
                    .setParameterList("video_ids", videoIds)
                    .setLong("course_id", course.id)
                    .setLong("from_timestamp", periodFrom)
                    .setLong("to_timestamp", periodTo)
                    .list()

            ok item: resultList.collect {
                [
                        username      : it[0],
                        userId        : it[1],
                        elearnId      : it[2],
                        studentId     : it[3],
                        answerCount   : it[4] ?: 0,
                        correctAnswers: it[5] ?: 0,
                        uniqueViews   : it[6] ?: 0
                ]
            }
        } else {
            fail message: "Course not found!"
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
        return fail()
    }

    ServiceResult<Map> findCourseParticipation(Course course, Long periodFrom, Long periodTo) {
        /*
        def start = System.currentTimeMillis()
        def subjects = course.activeSubjects
        def allVideos = subjects.collect { it.activeVideos }.flatten() as List<Video>
        def videoIds = allVideos.collect { it.id }

        def categories = subjects.collectEntries {
            [(it.id):
                     [
                             name    : it.name,
                             node    : "subject/${it.id}",
                             nodeType: "subject",
                             nodeId  : it.id
                     ]
            ]
        }

        def columns = allVideos.collect {
            [
                    name      : it.name,
                    node      : "video/${it.id}",
                    nodeType  : "video",
                    nodeId    : it.id,
                    fieldCount: videoService.getVideoMetaDataSafe(it).fieldCount,
                    belongsTo : it.subject.id
            ]
        }

        def students = Student.findAllByIdInList(CourseStudent.findAllByCourse(course).collect { it.studentId })
        def studentUserIds = students.collect { it.userId }.toSet()
        def allParticipatingUserIds = AnswerQuestionEvent.findAllByVideoIdInList(videoIds).collect { it.userId }.toSet()
        def allUserIds = (studentUserIds + allParticipatingUserIds).toList()
        def allUsers = User.findAllByIdInList(allUserIds).toSet()
        def seenEvents = VisitVideoEvent.findAllByVideoIdInList(videoIds).groupBy { it?.userId }

        def participation = allUsers.collect { user ->
            def relevantAnswers = AnswerQuestionEvent.findAllByCorrectAndUserAndTimestampBetweenAndVideoIdInList(true,
                    user, periodFrom, periodTo, videoIds)

            def answersGrouped = relevantAnswers.groupBy { it.videoId }

            def summary = allVideos.collectEntries { video ->
                def answersForVideo = answersGrouped[video.id]
                def correctAnswers = 0
                if (answersForVideo != null) {
                    correctAnswers = answersGrouped[video.id].groupBy {
                        new Triple(it.question, it.subject, it.field)
                    }.size()
                }
                def seen = seenEvents[user.id]?.find { it.videoId == video.id } != null

                [
                        (video.id): [
                                correctAnswers: correctAnswers,
                                seen          : seen
                        ]
                ]
            }

            [
                    username : (user) ? user.username : "Gæst",
                    answers  : summary,
                    isStudent: (user) ? user.id in studentUserIds : false,
            ]
        }
        def end = System.currentTimeMillis() - start
        return ok(item: [
                categories   : categories,
                columns      : columns,
                participation: participation,
                tookTime     : end
        ])
        */
        return findCourseLevelParticipation(course, periodFrom, periodTo)
    }

    ServiceResult<Map> findSubjectParticipation(Subject subject, Long periodFrom, Long periodTo) {

        return ok([:])
    }

    ServiceResult<Map> findVideoParticipation(Video video, Long periodFrom, Long periodTo) {

        return ok([:])
    }

    ServiceResult<UserParticipationReport> findCourseLevelParticipation(Course course, Long periodFrom, Long periodTo) {
        if (courseManagementService.canAccess(course)) {
            def dbStart = System.currentTimeMillis()
            List<Long> studentUserIdList = Student.findAllByIdInList(
                    CourseStudent.findAllByCourse(course).collect { it.studentId }
            ).collect { it.userId }

            def subjects = course.activeSubjects
            def subjectVideoList = SubjectVideo.findAllBySubjectInList(subjects)
            def videoIds = subjectVideoList.collect { it.videoId }
            def videos = Video.findAllByIdInList(videoIds).groupBy { it.id }
            def answerEvents = AnswerQuestionEvent.findAllByVideoIdInListAndCorrectAndTimestampBetween(videoIds, true, periodFrom, periodTo).findAll {
                it.userId != null
            }
            def visitEvents = VisitVideoEvent.findAllByVideoIdInListAndTimestampBetween(videoIds, periodFrom, periodTo).findAll {
                it.userId != null
            }
            def dbEnd = System.currentTimeMillis()
            Set<Long> studentUserIds = studentUserIdList.toSet()
            Map<Long, List<Long>> videoIdsBySubject = subjectVideoList
                    .groupBy { it.subjectId }
                    .collectEntries { [(it.key): it.value.collect { it.videoId }] }


            def answerEventsByVideo = answerEvents.groupBy { it.videoId }
            def visitEventsByVideo = visitEvents.groupBy { it.videoId }
            def usersWithAnswers = answerEvents.collect { it.collect { it.userId } }.flatten() as List<Long>
            def usersWithVisits = visitEvents.collect { it.collect { it.userId } }.flatten() as List<Long>
            def allUsers = User.findAllByIdInList(usersWithAnswers + usersWithVisits + studentUserIds)
            def groupEnd = System.currentTimeMillis()

            def timeVideoCollect = 0
            def answerCollect = 0
            def visitCollect = 0

            def result = new UserParticipationReport()
            result.identifier = course.identifier
            result.children = subjects.collect { it.identifier }

            result.details = subjects.collectEntries {
                def myVideoIdList = videoIdsBySubject[it.id] ?: []
                def myVideoIds = myVideoIdList.toSet()

                def videoCollectStart = System.currentTimeMillis()
                def myVideos = myVideoIds.collect { videos[it] }.flatten() as List<Video>
                def videoCollectEnd = System.currentTimeMillis()
                def myAnswers = answerEventsByVideo.findAll { it.key in myVideoIds }
                def answerCollectEnd = System.currentTimeMillis()
                def myVisits = visitEventsByVideo.findAll { it.key in myVideoIds }
                def visitCollectEnd = System.currentTimeMillis()
                timeVideoCollect += videoCollectEnd - videoCollectStart
                answerCollect += answerCollectEnd - videoCollectEnd
                visitCollect += visitCollectEnd - answerCollectEnd

                [(it.identifier.identifier): findSubjectLevelParticipation(it,
                        studentUserIds,
                        allUsers,
                        myVideos,
                        myAnswers,
                        myVisits
                )]
            }
            result.participation = result.findUserParticipationFromDetails()

            def subjectsEnd = System.currentTimeMillis()

            println("Database calls took: ${dbEnd - dbStart}")
            println("Local grouping took: ${groupEnd - dbEnd}")
            println("Collecting information on sub-nodes took: ${subjectsEnd - groupEnd}")
            println("Video collect took: $timeVideoCollect")
            println("Answer collect took: $answerCollect")
            println("Visit collect took: $visitCollect")

            return ok(item: result)
        } else {
            return fail()
        }
    }

    private UserParticipationReport findSubjectLevelParticipation(Subject subject, Set<Long> studentUsers,
            List<User> allUsers, List<Video> videos, Map<Long, List<AnswerQuestionEvent>> answerEventsByVideo,
            Map<Long, List<VisitVideoEvent>> visitEventsByVideo) {
        def result = new UserParticipationReport()
        result.identifier = subject.identifier
        result.children = videos.collect { it.identifier }
        result.details = videos.collectEntries {
            def answers = answerEventsByVideo[it.id] ?: []
            def visits = visitEventsByVideo[it.id] ?: []

            [(it.identifier.identifier): findVideoLevelParticipationNoSecurityCheck(it, studentUsers, allUsers,
                    answers, visits)]
        }
        result.participation = result.findUserParticipationFromDetails()
        return result
    }

    private UserParticipationReport findVideoLevelParticipationNoSecurityCheck(Video video, Set<Long> studentUsers,
            List<User> allUsers, List<AnswerQuestionEvent> correctAnswerEvents, List<VisitVideoEvent> visitEvents) {
        def timeline = videoService.getTimeline(video)
        def result = new UserParticipationReport()
        def answersBySubject = correctAnswerEvents.groupBy { it.subject }
        result.identifier = video.identifier
        result.children = timeline.subjects.collect { it.identifier }
        result.details = timeline.subjects.collectEntries {
            def answers = answersBySubject[it.timelineId] ?: []

            [(it.identifier.identifier): findVideoSubjectParticipationNoSecurityCheck(it, studentUsers, allUsers,
                    answers)]
        }
        result.participation = result.findUserParticipationFromDetails()
        return result
    }

    private UserParticipationReport findVideoSubjectParticipationNoSecurityCheck(VideoSubject subject,
            Set<Long> studentUsers, List<User> allUsers, List<AnswerQuestionEvent> correctAnswerEvents) {
        def result = new UserParticipationReport()
        def answersByQuestion = correctAnswerEvents.groupBy { it.question }
        result.identifier = subject.identifier
        result.children = subject.questions.collect { it.identifier }
        result.details = subject.questions.collectEntries {
            def answers = answersByQuestion[it.timelineId] ?: []

            [(it.identifier.identifier): findVideoQuestionParticipationNoSecurityCheck(it, studentUsers, allUsers,
                    answers)]
        }
        result.participation = result.findUserParticipationFromDetails()
        return result
    }

    private UserParticipationReport findVideoQuestionParticipationNoSecurityCheck(VideoQuestion question, Set<Long> studentUsers,
            List<User> allUsers, List<AnswerQuestionEvent> correctAnswerEvents) {
        def result = new UserParticipationReport()
        def answersByField = correctAnswerEvents.groupBy { it.field }
        result.identifier = question.identifier
        result.children = question.fields.collect { it.identifier }
        result.details = question.fields.collectEntries {
            def answers = answersByField[it.timelineId] ?: []

            [(it.identifier.identifier): findVideoFieldParticipationNoSecurityCheck(it, studentUsers, allUsers,
                    answers)]
        }
        result.participation = result.findUserParticipationFromDetails()
        return result
    }

    private UserParticipationReport findVideoFieldParticipationNoSecurityCheck(VideoField field, Set<Long> studentUsers,
            List<User> allUsers, List<AnswerQuestionEvent> correctAnswerEvents) {
        def result = new UserParticipationReport()
        result.identifier = field.identifier
        result.children = []
        result.details = [:]
        def answersByUser = correctAnswerEvents.groupBy { it.userId }
        result.participation = allUsers.collect { user ->
            boolean hasAnswer = answersByUser[user.id] ? !answersByUser[user.id].empty : false

            new UserParticipation(
                    user: user,
                    isStudent: user.id in studentUsers,
                    stats: new GradingStats(
                            maxScore: 1,
                            score: hasAnswer ? 1 : 0,
                            seen: true
                    )
            )
        }
        return result
    }

    /*
    ServiceResult<Void> findCourseParticipation(Course course, Long periodFrom, Long periodTo) {
        String query = $/
            SELECT
              students.*,
              course_video.*,
              CASE WHEN EXISTS(SELECT *
                              FROM event e
                              WHERE e.class = 'dk.sdu.tekvideo.events.VisitVideoEvent' AND
                                    e.user_id = students.user_id AND
                                    e.video_id = course_video.video_id AND
                                    e.timestamp >= :periodFrom AND e.timestamp <= :periodTo)
                THEN TRUE
                ELSE FALSE
              END AS seen,
              (
                SELECT COUNT(*) AS correct_answers
                FROM (
                  SELECT e.subject, e.field, e.question
                  FROM event e
                  WHERE
                    e.class = 'dk.sdu.tekvideo.events.AnswerQuestionEvent' AND
                    e.user_id = students.user_id AND
                    e.correct = TRUE AND
                    e.video_id = course_video.video_id AND
                    e.timestamp >= :periodFrom AND e.timestamp <= :periodTo
                  GROUP BY e.video_id, e.subject, e.field, e.question
                ) AS correct_unique
              )
            FROM
              (
                SELECT
                  myusers.username,
                  myusers.id AS user_id
                FROM course_student, student, myusers
                WHERE
                  course_student.course_id = :course_id AND
                  course_student.student_id = student.id AND
                  student.user_id = myusers.id
              ) AS students,
              (
                SELECT
                  video.name   AS video_name,
                  video.id     AS video_id,
                  subject.id   AS subject_id,
                  subject.name AS subject_name
                FROM video, course, subject, subject_video, course_subject
                WHERE
                  course.id = :course_id AND
                  course_subject.course_id = course.id AND
                  course_subject.subject_id = subject.id AND
                  subject_video.subject_id = subject.id AND
                  subject_video.video_id = video.id
              ) AS course_video;
        /$

        def resultList = sessionFactory.currentSession
                .createSQLQuery(query)
                .setLong("course_id", course.id)
                .list()

        return ok()
    }
    */
}
