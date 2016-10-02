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


    /**
     * Returns a mapping between every video in the course to a number of views for a particular user.
     *
     * @param user      The user to lookup the video visit count, can be null. If null, visit counts for every video
     *                  will be 0.
     * @param course    The course to limit the search to
     * @return          A mapping between every video in a course to the number of visits to said video.
     */
    Map<Long, Integer> findExerciseVisitCountInCourse(User user, Course course) {
        String query = $/
            WITH
                events_by_user AS (
                  SELECT
                    event.video_id,
                    COUNT(*) AS visit_count
                  FROM
                    event
                  WHERE
                    event.class = 'dk.sdu.tekvideo.events.VisitVideoEvent' AND
                    user_id = :user_id
                  GROUP BY
                    event.video_id
              ),
                selected_exercises AS (
                  SELECT exercise.id AS selected_exercise_id
                  FROM
                    course_subject, subject_exercise, exercise
                  WHERE
                    course_subject.course_id = :course_id AND
                    course_subject.subject_id = subject_exercise.subject_id AND
                    subject_exercise.exercise_id = exercise.id
              )
            SELECT
              selected_exercise_id,
              visit_count
            FROM
              selected_exercises
              LEFT OUTER JOIN events_by_user ON (video_id = selected_exercise_id);
        /$

        long userId = (user != null) ? user.id : -1
        def resultList = sessionFactory.currentSession
                .createSQLQuery(query)
                .setLong("user_id", userId)
                .setLong("course_id", course.id)
                .list()

        Map<Long, Integer> result = [:]
        resultList.each { result[it[0] as Long] = it[1] ?: 0 }
        return result
    }

}
