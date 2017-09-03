package dk.sdu.tekvideo

import grails.converters.JSON
import org.hibernate.SessionFactory

class VideoService {
    def springSecurityService
    def externalVideoHostService
    SessionFactory sessionFactory

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

    String getThumbnail(String youtubeId) {
        return "http://img.youtube.com/vi/${youtubeId}/hqdefault.jpg"
    }

    String getThumbnail(Video video) {
        if (video != null && video.videoType) {
            return getThumbnail(video.youtubeId)
        }
        return null
    }
}
