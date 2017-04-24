package dk.sdu.tekvideo.data

import dk.sdu.tekvideo.Event
import dk.sdu.tekvideo.Exercise
import dk.sdu.tekvideo.StatsEventService
import dk.sdu.tekvideo.User
import dk.sdu.tekvideo.Video
import dk.sdu.tekvideo.WrittenExercise
import dk.sdu.tekvideo.WrittenExerciseGroup

class EventData {
    static Event answerQuestionEvent(Video video, boolean correct, User user = null, String uuid = null) {
        def event = new Event()
        event.kind = StatsEventService.EVENT_ANSWER_QUESTION
        event.timestamp = System.currentTimeMillis()
        event.user = user
        event.uuid = uuid ?: UUID.randomUUID().toString()
        event.additionalData = [
                exercise: video.id,
                subject : 0,
                question: 0,
                correct : correct,
                details : [[answer: "foo", correct: correct, field: 0]]
        ]
        return event
    }

    static Event visitExerciseEvent(Exercise exercise, User user = null, String uuid = null) {
        def event = new Event()
        event.kind = StatsEventService.EVENT_VISIT_EXERCISE
        event.timestamp = System.currentTimeMillis()
        event.user = user
        event.uuid = uuid ?: UUID.randomUUID().toString()
        event.additionalData = [
                exercise: exercise.id
        ]
        return event
    }

    static Event answerWrittenExercise(WrittenExerciseGroup group, WrittenExercise subExercise,
                                       boolean correct, User user = null, String uuid = null) {
        def event = new Event()
        event.kind = StatsEventService.EVENT_ANSWER_WRITTEN_EXERCISE
        event.timestamp = System.currentTimeMillis()
        event.user = user
        event.uuid = uuid ?: UUID.randomUUID().toString()
        event.additionalData = [
                passes     : correct,
                exercise   : group.id,
                subExercise: subExercise.id
        ]
        return event
    }

    static Event visitWrittenExercise(WrittenExerciseGroup group, WrittenExercise subExercise,
                                      User user = null, String uuid = null) {
        def event = new Event()
        event.kind = StatsEventService.EVENT_VISIT_WRITTEN_EXERCISE
        event.timestamp = System.currentTimeMillis()
        event.user = user
        event.uuid = uuid ?: UUID.randomUUID().toString()
        event.additionalData = [
                exercise   : group.id,
                subExercise: subExercise.id
        ]
        return event
    }
}
