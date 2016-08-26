package dk.sdu.tekvideo

import dk.sdu.tekvideo.events.AnswerQuestionEvent
import dk.sdu.tekvideo.events.VisitVideoEvent

class VideoField {
    Integer timelineId
    String name
    Double leftoffset
    Double topoffset
    VideoAnswer answer
    VideoQuestion parent

    boolean matches(AnswerQuestionEvent event) {
        def questionId = parent.timelineId
        def subjectId = parent.parent.timelineId
        return event.field == timelineId && event.subject == subjectId && questionId == event.question
    }

    GradingStats grade(List<AnswerQuestionEvent> answerEvents) {
        assert answerEvents.every {
            it.field == timelineId && it.question == parent.timelineId && it.subject == parent.parent.timelineId
        }

        def result = new GradingStats()
        result.identifier = identifier
        result.maxScore = 1
        result.score = answerEvents.any { it.correct } ? 1 : 0
        result.seen = answerEvents.size() > 0
        return result
    }

    NodeIdentifier getIdentifier() {
        parent.identifier.child(timelineId)
    }

    static VideoField fromMap(Integer timelineId, VideoQuestion parent, Map<String, Object> map) {
        return new VideoField(
                timelineId: timelineId,
                parent: parent,
                name: map.name,
                leftoffset: map.leftoffset as Double,
                topoffset: map.topoffset as Double,
                answer: VideoAnswer.fromMap(map.answer as Map<String, Object>)
        )
    }

    @Override
    String toString() {
        return "VideoField{" +
                "name='" + name + '\'' +
                ", leftoffset=" + leftoffset +
                ", topoffset=" + topoffset +
                ", answer=" + answer +
                '}';
    }
}
