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
    SessionFactory sessionFactory

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
}
