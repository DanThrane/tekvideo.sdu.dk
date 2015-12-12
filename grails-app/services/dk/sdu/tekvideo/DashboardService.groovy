package dk.sdu.tekvideo

import dk.sdu.tekvideo.events.AnswerQuestionEvent
import dk.sdu.tekvideo.events.VisitVideoEvent
import org.hibernate.SessionFactory

import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.stream.Collectors

import static dk.sdu.tekvideo.ServiceResult.*

import grails.transaction.Transactional

@Transactional
class DashboardService {
    private static final DateTimeFormatter TIME_PATTERN = DateTimeFormatter.ofPattern("dd/MM HH:mm")

    def teachingService
    CourseManagementService courseManagementService
    SessionFactory sessionFactory

    private Course findCourse(Node node) {
        while (node != null && node.parent != null) {
            node = node.parent
        }
        return (node instanceof Course) ? node : null
    }

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

    ServiceResult<List<Video>> findLeaves(Node node) {
        def course = findCourse(node)
        if (course != null && courseManagementService.canAccess(course)) {
            if (node instanceof Course) {
                def subjects = Subject.findAllByCourse(course)
                ok item: Video.findAllBySubjectInList(subjects)
            } else if (node instanceof Subject) {
                ok item: Video.findAllBySubject(node)
            } else if (node instanceof Video) {
                ok item: [node]
            } else {
                fail message: "Ukendt type", suggestedHttpStatus: 500
            }
        } else {
            fail message: "Du har ikke rettigheder til at tilgå dette kursus", suggestedHttpStatus: 403
        }
    }

    Map findViewingStatistics(List<Video> leaves, Long period) {
        if (leaves == null) leaves = []
        def videoIds = leaves.stream().map { it.id }.collect(Collectors.toList())

        long from = System.currentTimeMillis() - period * 24 * 60 * 60 * 1000
        long to = System.currentTimeMillis()
        List<VisitVideoEvent> events
        if (period <= 0) {
            events = VisitVideoEvent.findAllByVideoIdInList(videoIds).findAll { it != null }
            from = events.min { it.timestamp }.timestamp
        } else {
            events = VisitVideoEvent.findAllByVideoIdInListAndTimestampBetween(videoIds, from, to).findAll {
                it != null
            }
        }
        long periodInMs = (to - from) / 24

        List labels = []
        List data = []
        // Generate some labels (X-axis)
        long counter = from
        while (counter < to) {
            labels.add(TIME_PATTERN.format(new Date(counter).toInstant().atZone(ZoneId.systemDefault())))
            data.add(0)
            counter += periodInMs
        }
        events.each {
            long time = it.timestamp
            int index = (time - from) / periodInMs
            if (index > 0) {
                data[index]++
            }
        }

        return [labels: labels, data: data]
    }

    Map asdqwe(List<Video> leaves, Long period) {
        if (leaves == null) leaves = []
        def videoIds = leaves.stream().map { it.id }.collect(Collectors.toList())
        long from = System.currentTimeMillis() - period * 24 * 60 * 60 * 1000
        long to = System.currentTimeMillis()

        List<AnswerQuestionEvent> answerEvents = period <= 0 ?
                AnswerQuestionEvent.findAllByVideoIdInList(videoIds).findAll { it != null } :
                AnswerQuestionEvent.findAllByVideoIdInListAndTimestampBetween(videoIds, from, to).findAll { it != null }

        List<VisitVideoEvent> visitEvents = period <= 0 ?
                VisitVideoEvent.findAllByVideoIdInList(videoIds).findAll { it != null } :
                VisitVideoEvent.findAllByVideoIdInListAndTimestampBetween(videoIds, from, to).findAll { it != null }

        long totalVisits = visitEvents.size()
        long totalAnswers = answerEvents.size()
        long correctAnswers = answerEvents.findAll { it.correct }.size()

        return [totalVisits   : totalVisits,
                totalAnswers  : totalAnswers,
                correctAnswers: correctAnswers]
    }


    List<Map> findPopularVideos(List<Video> leaves, Long period) {
        if (leaves == null) leaves = []
        def videoIds = leaves.stream().map { it.id }.collect(Collectors.toList())
        long from = System.currentTimeMillis() - period * 24 * 60 * 60 * 1000
        long to = System.currentTimeMillis()

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
                event.video_id IN (SELECT id FROM video) AND
                event.timestamp >= :from_timestamp AND
                event.timestamp <= :to_timestamp AND
                event.video_id IN :video_ids
            GROUP BY
                event.video_id, answers.answerCount, answers.correctAnswers
            ORDER BY
                COUNT(*) DESC
            LIMIT 5;
            /$

        def resultList = sessionFactory.currentSession
                .createSQLQuery(query)
                .setParameterList("video_ids", videoIds)
                .setLong("from_timestamp", from)
                .setLong("to_timestamp", to)
                .list()

        return resultList.collect {
            def video = Video.get(it[0] as Long)

            [
                    "videoId": it[0],
                    "videoName": video.name,
                    "subjectName": video.subject.name,
                    "courseName": video.subject.course.name,
                    "answerCount": it[1] ?: 0,
                    "correctAnswers": it[2] ?: 0,
                    "visits": it[3] ?: 0,
            ]
        }
    }
}
