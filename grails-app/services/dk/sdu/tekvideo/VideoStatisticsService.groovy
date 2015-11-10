package dk.sdu.tekvideo

import dk.sdu.tekvideo.events.VisitVideoEvent

import java.time.ZoneId
import java.time.format.DateTimeFormatter

import static dk.sdu.tekvideo.ServiceResult.ok
import static dk.sdu.tekvideo.ServiceResult.fail

import grails.transaction.Transactional
import org.hibernate.SessionFactory

/**
 * @author Dan Thrane
 */
@Transactional
class VideoStatisticsService {
    private static final DateTimeFormatter TIME_PATTERN = DateTimeFormatter.ofPattern("dd/MM HH:mm")

    SessionFactory sessionFactory

    ServiceResult<List> retrieveAnswerSummary(Video video) {
        if (video) {
            String query = $/
            SELECT
                e.answer    AS answer,
                COUNT(*)    AS frequency,
                e.correct   AS correct,
                e.subject   AS subject,
                e.question  AS question,
                e.field     AS field
            FROM
                event as e
            WHERE
                e.video_id = :video_id AND
                e.answer IS NOT NULL
            GROUP BY
                e.answer,
                e.correct,
                e.subject,
                e.question,
                e.field
            ORDER BY
                frequency DESC
            /$

            def resultList = sessionFactory.currentSession
                    .createSQLQuery(query)
                    .setLong("video_id", video.id)
                    .list()

            ok resultList.collect {
                [
                        answer   : it[0],
                        frequency: it[1],
                        correct  : it[2],
                        subject  : it[3],
                        question : it[4],
                        field    : it[5]
                ]
            }
        } else {
            fail("video_statistics.not_found", false, [:], 404)
        }
    }

    ServiceResult<Map> retrieveViewBreakdown(Video video) {
        if (video) {
            String query = $/
            SELECT
                COUNT(*)                                                          AS visits,
                COUNT(e.user_id)                                                  AS student_visits,
                COALESCE(SUM(CASE WHEN e.user_id IS NULL THEN 1 ELSE 0 END), 0)   AS guest_visits
            FROM
                event as e
            WHERE
                e.class = :visit_video_event_class AND
                e.video_id = :video_id
            /$

            def resultList = sessionFactory.currentSession
                    .createSQLQuery(query)
                    .setString("visit_video_event_class", VisitVideoEvent.name)
                    .setLong("video_id", video.id)
                    .list()
            if (resultList.size() > 0) {
                def result = resultList[0]
                return ok(item: [visits: result[0], studentVisits: result[1], guestVisits: result[2]])
            } else {
                return fail("video_statistics.not_found")
            }
        } else {
            return fail("video_statistics.not_found", false, [:], 404)
        }
    }

    ServiceResult<List<Map>> retrieveViewingStatisticsForStudents(Video video) {
        if (video) {
            String query = $/
            SELECT
                myusers.id          AS user_id,
                myusers.username    AS username,
                event."timestamp"   AS "timestamp",
                video.id            AS video_id,
                video.name          AS video_name
            FROM
                course_student,
                myusers,
                student,
                course,
                event,
                video,
                subject
            WHERE
                course_student.course_id = course.id AND
                course_student.student_id = student.id AND
                myusers.id = event.user_id AND
                student.user_id = myusers.id AND
                course.id = subject.course_id AND
                event.video_id = video.id AND
                video.subject_id = subject.id AND
                event.class = 'dk.sdu.tekvideo.events.VisitVideoEvent' AND
                video.id = ?;
            /$
            def resultList = sessionFactory.currentSession
                    .createSQLQuery(query)
                    .setLong(0, video.id)
                    .list()

            ok resultList.collect {
                [
                        userId: it[0],
                        username: it[1],
                        timestamp: it[2],
                        videoId: it[3],
                        videoName: it[4]
                ]
            }
        } else {
            fail("video_statistics.not_found", false, [:], 404)
        }
    }

    ServiceResult<Map> findViewingStatistics(Video video, long from, long to, long periodInMs) {
        if (video) {
            List<VisitVideoEvent> events = VisitVideoEvent.findAllByVideoId(video.id)
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
            ok(item: [labels: labels, data: data])
        } else {
            fail("video_statistics.not_found", false, [:], 404)
        }
    }
}