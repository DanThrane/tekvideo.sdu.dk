package dk.sdu.tekvideo

import dk.sdu.tekvideo.events.AnswerQuestionEvent
import grails.converters.JSON

class VideoTimeline {
    Video video
    List<VideoSubject> subjects

    GradingStats grade(List<AnswerQuestionEvent> answerEvents) {
        assert answerEvents.every { it.videoId == video.id }

        GradingStats stats = new GradingStats()
        stats.identifier = video.identifier
        def eventsGroupedBySubject = answerEvents.groupBy { it.subject }
        stats.children = subjects.collectEntries {
            def events = eventsGroupedBySubject[it.timelineId] ?: []

            [(it.identifier): it.grade(events)]
        }
        stats.updateStatsFromChildren()

        return stats
    }

    static VideoTimeline fromVideo(Video video) {
        def result = new VideoTimeline()
        result.video = video

        try {
            def parsed = (List<Map>) JSON.parse(video.timelineJson)
            List<VideoSubject> collectedSubjects = []
            parsed.eachWithIndex { entry, i ->
                collectedSubjects.add(VideoSubject.fromMap(i, result, entry))
            }
            result.subjects = collectedSubjects
        } catch (Exception ignored) {
            result.subjects = []
        }
        return result
    }
}
