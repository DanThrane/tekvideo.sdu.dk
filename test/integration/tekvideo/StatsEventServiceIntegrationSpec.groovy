package tekvideo

import dk.sdu.tekvideo.Event
import dk.sdu.tekvideo.Exercise
import dk.sdu.tekvideo.StatsEventService
import dk.sdu.tekvideo.User
import dk.sdu.tekvideo.Video
import dk.sdu.tekvideo.WrittenExercise
import dk.sdu.tekvideo.WrittenExerciseGroup
import dk.sdu.tekvideo.data.CourseData
import dk.sdu.tekvideo.data.SubjectData
import dk.sdu.tekvideo.data.UserData
import dk.sdu.tekvideo.data.VideoData
import dk.sdu.tekvideo.data.WrittenExerciseData
import dk.sdu.tekvideo.stats.ExerciseProgress
import dk.sdu.tekvideo.stats.ExerciseVisit
import dk.sdu.tekvideo.stats.VideoAnswer
import dk.sdu.tekvideo.stats.WrittenExerciseVisit
import dk.sdu.tekvideo.stats.WrittenGroupStreak
import spock.lang.Specification

class StatsEventServiceIntegrationSpec extends Specification {
    def statsEventService

    def "test various event types and how they are handled"() {
        given: "a basic system"
        def teacher = UserData.buildTestTeacher()
        def course = CourseData.buildTestCourse("Course", teacher)
        def subject = SubjectData.buildTestSubject("Subject", course)
        def video = VideoData.buildTestVideo("Video", subject)
        def group = WrittenExerciseData.buildTestExercise("Group", subject)
        def subExercise = group.exercises[0]

        and: "some valid events"
        def event1 = answerQuestionEvent(video, true)
        def event2 = visitExerciseEvent(video)
        def event3 = visitExerciseEvent(group)
        def event4 = answerWrittenExercise(group, subExercise, true)
        def event5 = visitWrittenExercise(group, subExercise)

        and: "a bogus event"
        def bogusEvent = new Event()
        bogusEvent.kind = "foo"
        bogusEvent.timestamp = System.currentTimeMillis()
        bogusEvent.user = null
        bogusEvent.uuid = UUID.randomUUID().toString()
        bogusEvent.additionalData = [foo: "bar"]

        when: "checking if service can handle events"
        then: "valid events can be handled"
        statsEventService.canHandle(event1.kind)
        statsEventService.canHandle(event2.kind)
        statsEventService.canHandle(event3.kind)
        statsEventService.canHandle(event4.kind)
        statsEventService.canHandle(event5.kind)

        and: "invalid events cannot be handled"
        !statsEventService.canHandle(bogusEvent.kind)

        when: "passed to EventHandler#handle"
        def result1 = statsEventService.handle(event1)
        def result2 = statsEventService.handle(event2)
        def result3 = statsEventService.handle(event3)
        def result4 = statsEventService.handle(event4)
        def result5 = statsEventService.handle(event5)
        def bogusResult = statsEventService.handle(bogusEvent)

        then: "valid events are successful"
        result1.success
        result2.success
        result3.success
        result4.success
        result5.success

        and: "invalid events are not"
        !bogusResult.success
    }

    def "test streaks"() {
        given: "a written exercise setup"
        def group = WrittenExerciseData.buildTestExercise()
        def subExercise = group.exercises[0]

        and: "a sample event"
        def correctEvent = answerWrittenExercise(group, subExercise, true)
        def incorrectEvent = answerWrittenExercise(group, subExercise, false)
        incorrectEvent.uuid = correctEvent.uuid
        incorrectEvent.user = correctEvent.user

        when: "we haven't submitted an event"
        then: "there is no ExerciseProgress present"
        ExerciseProgress.findByExerciseAndUuid(group, incorrectEvent.uuid) == null

        when: "we submit an event"
        statsEventService.handle(correctEvent)
        def progress = ExerciseProgress.findByExerciseAndUuid(group, correctEvent.uuid)

        then: "there is an ExerciseProgress"
        progress != null

        and: "a there is a WrittenGroup streak"
        WrittenGroupStreak.findByProgress(progress) != null

        and: "the streak is 1"
        WrittenGroupStreak.findByProgress(progress).currentStreak == 1
        WrittenGroupStreak.findByProgress(progress).longestStreak == 1

        when: "we add a few more correct exercises"
        (1..2).each { statsEventService.handle(correctEvent) }

        then: "the current and longest streak should be 3"
        WrittenGroupStreak.findByProgress(progress).currentStreak == 3
        WrittenGroupStreak.findByProgress(progress).longestStreak == 3

        when: "we submit an incorrect"
        statsEventService.handle(incorrectEvent)

        then: "the current streak should go down, and longest should remain"
        WrittenGroupStreak.findByProgress(progress).currentStreak == 0
        WrittenGroupStreak.findByProgress(progress).longestStreak == 3

        when: "we submit 5 more correct"
        (1..5).each { statsEventService.handle(correctEvent) }

        then: "the longest should update"
        WrittenGroupStreak.findByProgress(progress).currentStreak == 5
        WrittenGroupStreak.findByProgress(progress).longestStreak == 5
    }

    def "test exercise visits"() {
        given: "a test exercise"
        def video = VideoData.buildTestVideo()

        and: "a visit event"
        def event = visitExerciseEvent(video)

        when: "we haven't done anything"
        then: "there is no exercise progress"
        ExerciseProgress.findByExerciseAndUuid(video, event.uuid) == null

        when: "we visit the exercise"
        statsEventService.handle(event)
        def progress = ExerciseProgress.findByExerciseAndUuid(video, event.uuid)

        then: "we get exercise progress"
        progress != null

        and: "a visit"
        ExerciseVisit.countByProgress(progress) == (Integer) 1

        when: "we add more visits"
        (1..100).each { statsEventService.handle(event) }

        then: "we have more visits"
        ExerciseVisit.countByProgress(progress) == (Integer) 101
    }

    def "test video answers"() {
        given: "a video"
        def video = VideoData.buildTestVideo()

        and: "some events"
        def correctAnswer = answerQuestionEvent(video, true)
        def incorrectAnswer = answerQuestionEvent(video, false)
        incorrectAnswer.uuid = correctAnswer.uuid
        incorrectAnswer.user = correctAnswer.user

        when: "we haven't done anything"
        then: "there is no progress"
        ExerciseProgress.findByExerciseAndUuid(video, correctAnswer.uuid) == null

        when: "we add an answer"
        statsEventService.handle(correctAnswer)
        def progress = ExerciseProgress.findByExerciseAndUuid(video, correctAnswer.uuid)

        then: "we do have progress"
        progress != null

        and: "we can see some answers"
        VideoAnswer.findAllByProgress(progress).groupBy { it.correct }[true].size() == (Integer) 1
        VideoAnswer.findAllByProgress(progress).groupBy { it.correct }[false] == null

        when: "we add more correct and incorrect answers"
        (1..30).each { statsEventService.handle(correctAnswer) }
        (1..30).each { statsEventService.handle(incorrectAnswer) }
        def answersByCorrectness = VideoAnswer.findAllByProgress(progress).groupBy { it.correct }

        then: "we can find these answers"
        answersByCorrectness[true].size() == (Integer) 31
        answersByCorrectness[false].size() == (Integer) 30
    }

    def "test written sub-exercise visits"() {
        given: "a written exercise"
        def group = WrittenExerciseData.buildTestExercise()
        def subExercise = group.exercises[0]

        and: "a visit event"
        def event = visitWrittenExercise(group, subExercise)

        when: "we haven't visited anything"
        then: "there is no progress"
        ExerciseProgress.findByExerciseAndUuid(group, event.uuid) == null

        when: "we visit an exercise"
        statsEventService.handle(event)
        def progress = ExerciseProgress.findByExerciseAndUuid(group, event.uuid)

        then: "there is progress"
        progress != null

        and: "the sub-exercise is marked as visited"
        WrittenExerciseVisit.countByProgressAndSubExercise(progress, subExercise) == (Integer) 1

        when: "we add more visits"
        (1..30).each { statsEventService.handle(event) }

        then: "we have more visits"
        WrittenExerciseVisit.countByProgressAndSubExercise(progress, subExercise) == (Integer) 31
    }

    private Event answerQuestionEvent(Video video, boolean correct, User user = null) {
        def event = new Event()
        event.kind = StatsEventService.EVENT_ANSWER_QUESTION
        event.timestamp = System.currentTimeMillis()
        event.user = user
        event.uuid = UUID.randomUUID().toString()
        event.additionalData = [
                exercise: video.id,
                subject : 0,
                question: 0,
                correct : correct,
                details : [[answer: "foo", correct: correct, field: 0]]
        ]
        return event
    }

    private Event visitExerciseEvent(Exercise exercise, User user = null) {
        def event = new Event()
        event.kind = StatsEventService.EVENT_VISIT_EXERCISE
        event.timestamp = System.currentTimeMillis()
        event.user = user
        event.uuid = UUID.randomUUID().toString()
        event.additionalData = [
                exercise: exercise.id
        ]
        return event
    }

    private Event answerWrittenExercise(WrittenExerciseGroup group, WrittenExercise subExercise,
                                        boolean correct, User user = null) {
        def event = new Event()
        event.kind = StatsEventService.EVENT_ANSWER_WRITTEN_EXERCISE
        event.timestamp = System.currentTimeMillis()
        event.user = user
        event.uuid = UUID.randomUUID().toString()
        event.additionalData = [
                passes: correct,
                exercise: group.id,
                subExercise: subExercise.id
        ]
        return event
    }

    private Event visitWrittenExercise(WrittenExerciseGroup group, WrittenExercise subExercise,
                                       User user = null) {
        def event = new Event()
        event.kind = StatsEventService.EVENT_VISIT_WRITTEN_EXERCISE
        event.timestamp = System.currentTimeMillis()
        event.user = user
        event.uuid = UUID.randomUUID().toString()
        event.additionalData = [
                exercise: group.id,
                subExercise: subExercise.id
        ]
        return event
    }

}
