package dk.sdu.tekvideo

import grails.converters.JSON
import org.hibernate.SessionFactory

import static dk.sdu.tekvideo.ServiceResult.ok

class VideoService {
    def springSecurityService
    def externalVideoHostService
    SessionFactory sessionFactory

    ServiceResult<List<VideoBreakdown>> findVideoBreakdown(List<Video> videos) {
        def videoIds = videos.collect { it.id }

        def viewCountQuery = $/
            SELECT
              exercise.id, COUNT(exercise_progress.id)
            FROM
              exercise LEFT OUTER JOIN exercise_progress ON exercise.id = exercise_progress.exercise_id
            WHERE
              exercise.class = 'dk.sdu.tekvideo.Video' AND
              exercise.id IN :videos
            GROUP BY exercise.id;
        /$

        List<List> viewCounts = sessionFactory.currentSession
                .createSQLQuery(viewCountQuery)
                .setParameterList("videos", videoIds)
                .list()

        def commentCountQuery = $/
            SELECT
              exercise.id, COUNT(exercise_comment)
            FROM
              exercise LEFT OUTER JOIN exercise_comment ON exercise.id = exercise_comment.exercise_comments_id
            WHERE
              exercise.class = 'dk.sdu.tekvideo.Video' AND
              exercise.id IN :videos
            GROUP BY exercise.id;
        /$

        List<List> commentCounts = sessionFactory.currentSession
                .createSQLQuery(commentCountQuery)
                .setParameterList("videos", videoIds)
                .list()

        // Merge results
        Map<Long, VideoBreakdown> breakdownsById = videos.collectEntries {
            [(it.id): new VideoBreakdown(video: it, commentCount: 0, viewCount: 0)]
        }
        viewCounts.each { breakdownsById[it[0] as Long]?.viewCount = it[1] as Integer }
        commentCounts.each { breakdownsById[it[0] as Long]?.commentCount = it[1] as Integer }

        ok breakdownsById.values().toList()
    }

    VideoMetaData getVideoMetaDataSafe(Video video) {
        int subjectCount = 0
        int questionCount = 0
        int fieldCount = 0
        try {
            List parse = JSON.parse(video.timelineJson)

            subjectCount = parse.size()

            def collectedQuestions = parse.collect { it.questions.size() }
            questionCount = collectedQuestions ? collectedQuestions.sum() : 0

            def collectedFields = parse.collect {
                def collectedFields = it.questions.collect { it.fields.size() }
                return collectedFields ? collectedFields.sum() : 0
            }
            fieldCount = collectedFields ? collectedFields.sum() : 0
        } catch (Exception ignored) {
            // The timeline may be corrupted in many ways. Better to be safe than sorry in this case.
            ignored.printStackTrace()
        }

        def duration = "00:00"
        try {
            def info = externalVideoHostService.getVideoInformation(video.youtubeId, video.videoType)

            if (info.success) duration = info.result.duration
        } catch (Exception ignored) {
            ignored.printStackTrace()
        }
        return new VideoMetaData([
                subjectCount : subjectCount,
                questionCount: questionCount,
                fieldCount   : fieldCount,
                duration     : duration
        ])
    }

    String getThumbnail(Video video) {
        if (video != null && video.videoType) {
            return "http://img.youtube.com/vi/${video.youtubeId}/hqdefault.jpg"
        }
        return null
    }
}
