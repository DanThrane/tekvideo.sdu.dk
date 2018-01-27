package dk.sdu.tekvideo

import dk.sdu.tekvideo.stats.AnswerVideoCommand
import dk.sdu.tekvideo.stats.AnswerVideoDetails
import dk.sdu.tekvideo.stats.EventHandler
import dk.sdu.tekvideo.stats.VideoAnswer
import dk.sdu.tekvideo.stats.VideoAnswerDetails
import dk.sdu.tekvideo.stats.ExerciseProgress
import dk.sdu.tekvideo.stats.ExerciseVisit
import dk.sdu.tekvideo.stats.WrittenExerciseVisit
import dk.sdu.tekvideo.stats.WrittenGroupAnswer
import dk.sdu.tekvideo.stats.WrittenGroupStreak
import org.springframework.http.HttpStatus

class StatsEventService implements EventHandler {
    static final String EVENT_ANSWER_QUESTION = "ANSWER_QUESTION"
    static final String EVENT_VISIT_EXERCISE = "VISIT_EXERCISE"
    static final String EVENT_ANSWER_WRITTEN_EXERCISE = "ANSWER_WRITTEN_EXERCISE"
    static final String EVENT_VISIT_WRITTEN_EXERCISE = "VISIT_WRITTEN_EXERCISE"
    static final Set<String> KNOWN_EVENTS = Collections.unmodifiableSet([
            EVENT_ANSWER_QUESTION, EVENT_VISIT_EXERCISE, EVENT_ANSWER_WRITTEN_EXERCISE, EVENT_VISIT_WRITTEN_EXERCISE
    ].toSet())

    @Override
    boolean canHandle(String eventKind) {
        return eventKind in KNOWN_EVENTS
    }

    @Override
    ServiceResult<Void> handle(Event event) {
        switch (event.kind) {
            case EVENT_ANSWER_QUESTION:
                return handleAnswerQuestion(event)
            case EVENT_VISIT_EXERCISE:
                return handleVisitExercise(event)
            case EVENT_ANSWER_WRITTEN_EXERCISE:
                return handleAnswerWrittenExercise(event)
            case EVENT_VISIT_WRITTEN_EXERCISE:
                return handleVisitWrittenExercise(event)
            default:
                return ServiceResult.fail(
                        message: "Internal error",
                        internal: true,
                        suggestedHttpStatus: HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        information: [why: "Unhandled event kind ${event.kind}"]
                )
        }
    }

    ServiceResult<Void> handleVisitWrittenExercise(Event event) {
        def exerciseResult = eventProperty(event, "exercise", Integer)
        def subExerciseResult = eventProperty(event, "subExercise", Integer)

        def errors = checkForErrors(exerciseResult, subExerciseResult)
        if (errors) return errors

        def exercise = WrittenExercise.get(subExerciseResult.result)
        def group = WrittenExerciseGroup.get(exerciseResult.result)

        if (exercise == null || group == null) {
            return ServiceResult.fail(
                    message: "Not found",
                    suggestedHttpStatus: HttpStatus.NOT_FOUND.value()
            )
        }

        addWrittenExerciseVisit(getProgress(group, event.user, event.uuid), exercise)
        return ServiceResult.ok()
    }

    ServiceResult<Void> handleAnswerWrittenExercise(Event event) {
        def passesResult = eventProperty(event, "passes", Boolean)
        def exerciseResult = eventProperty(event, "exercise", Integer)
        def subExerciseResult = eventProperty(event, "subExercise", Integer)

        def errors = checkForErrors(passesResult, exerciseResult, subExerciseResult)
        if (errors) return errors

        def exercise = WrittenExercise.get(subExerciseResult.result)
        def group = WrittenExerciseGroup.get(exerciseResult.result)
        if (exercise == null || group == null) {
            return ServiceResult.fail(
                    message: "Not found",
                    suggestedHttpStatus: HttpStatus.NOT_FOUND.value()
            )
        }

        addWrittenAnswer(getProgress(group, event.user, event.uuid), exercise, passesResult.result)
        return ServiceResult.ok()
    }

    private ServiceResult<Void> handleAnswerQuestion(Event event) {
        def videoResult = eventProperty(event, "exercise", Integer)
        def subjectResult = eventProperty(event, "subject", Integer)
        def questionResult = eventProperty(event, "question", Integer)
        def correctResult = eventProperty(event, "correct", Boolean)
        def detailsResult = eventProperty(event, "details", List)

        def errors = checkForErrors(videoResult, subjectResult, questionResult, correctResult, detailsResult)
        if (errors) return errors

        def video = Video.get(videoResult.result)
        if (video == null) {
            return ServiceResult.fail(
                    message: "Not found",
                    suggestedHttpStatus: HttpStatus.NOT_FOUND.value()
            )
        }

        def command = new AnswerVideoCommand()
        command.correct = correctResult.result
        command.question = questionResult.result
        command.subject = subjectResult.result
        command.video = video

        command.details = detailsResult.result.collect {
            def answerResult = dynamicProperty(it, "answer", String)
            def detailsCorrectResult = dynamicProperty(it, "correct", Boolean)
            def fieldResult = dynamicProperty(it, "field", Integer)

            def detailsErrors = checkForErrors(answerResult, detailsCorrectResult, fieldResult)
            if (detailsErrors) return null

            def result = new AnswerVideoDetails()
            result.correct = detailsCorrectResult.result
            result.answer = answerResult.result
            result.field = fieldResult.result
            return result
        }.findAll { it != null }

        if (command.details.empty) {
            return ServiceResult.fail(
                    message: "No details provided",
                    suggestedHttpStatus: HttpStatus.BAD_REQUEST.value()
            )
        }

        def progress = getProgress(video, event.user, event.uuid)
        addVideoAnswer(progress, command)
        return ServiceResult.ok()
    }

    private ServiceResult<Void> handleVisitExercise(Event event) {
        def exerciseResult = eventProperty(event, "exercise", Integer)
        if (!exerciseResult.success) return exerciseResult.convertFailure()
        def video = Exercise.get(exerciseResult.result)

        if (video == null) {
            return ServiceResult.fail(
                    message: "Not found",
                    suggestedHttpStatus: HttpStatus.NOT_FOUND.value()
            )
        }

        def progress = getProgress(video, event.user, event.uuid)
        addVisit(progress)
        return ServiceResult.ok()
    }

    ExerciseProgress getProgress(Exercise exercise, User user, String uuid = null) {
        assert user != null || uuid != null

        ExerciseProgress result
        if (user != null) {
            result = ExerciseProgress.findByExerciseAndUser(exercise, user)
        } else {
            result = ExerciseProgress.findByExerciseAndUuid(exercise, uuid)
        }

        if (result == null) {
            result = new ExerciseProgress(exercise: exercise, user: user, uuid: uuid).save(flush: true)
        }
        return result
    }

    void addVisit(ExerciseProgress progress) {
        new ExerciseVisit(
                progress: progress,
                timestamp: new Date(System.currentTimeMillis())
        ).save(flush: true)
    }

    void addVideoAnswer(ExerciseProgress progress, AnswerVideoCommand command) {
        def answer = new VideoAnswer(
                progress: progress,
                correct: command.correct,
                subject: command.subject,
                question: command.question
        ).save(flush: true)

        command.details.collect {
            new VideoAnswerDetails(
                    parent: answer,
                    answer: it.answer,
                    correct: it.correct,
                    field: it.field
            )
        }.findAll { it != null }.each { it.save(flush: true) }
    }

    void addWrittenAnswer(ExerciseProgress progress, WrittenExercise exercise, Boolean passes) {
        new WrittenGroupAnswer(progress: progress, exercise: exercise, passes: passes).save(flush: true)

        def streak = WrittenGroupStreak.findByProgress(progress) ?:
                new WrittenGroupStreak(progress: progress, currentStreak: 0, longestStreak: 0)

        if (passes) {
            streak.currentStreak++
            if (streak.currentStreak > streak.longestStreak) {
                streak.longestStreak = streak.currentStreak
            }
        } else {
            streak.currentStreak = 0
        }
        streak.save(flush: true)
    }

    void addWrittenExerciseVisit(ExerciseProgress progress, WrittenExercise subExercise) {
        new WrittenExerciseVisit(
                progress: progress,
                subExercise: subExercise,
                timestamp: new Date(System.currentTimeMillis())
        ).save(flush: true)
    }

    private <T> ServiceResult<T> dynamicProperty(obj, String key, Class<T> type) {
        if (obj[key] != null) {
            def property = obj[key]
            if (type.isInstance(property)) {
                return ServiceResult.ok(type.cast(property))
            } else {
                return ServiceResult.fail(
                        message: "Bad request",
                        suggestedHttpStatus: HttpStatus.BAD_REQUEST.value(),
                        information: [why: "Invalid type for '$key'. " +
                                "Expected ${type.simpleName} got ${property.class.simpleName}"]
                )
            }
        }

        return ServiceResult.fail(
                message: "Bad request",
                suggestedHttpStatus: HttpStatus.BAD_REQUEST.value(),
                informatino: [why: "Property '$key' does not exist on event."]
        )
    }

    private <T> ServiceResult<T> eventProperty(Event event, String key, Class<T> type) {
        if (event.additionalData.containsKey(key)) {
            def property = event.additionalData.get(key)
            if (type == Integer || type == Long) {
                if (Integer.isInstance(property) || Long.isInstance(property)) {
                    if (type == Integer) return ServiceResult.ok((Integer) property)
                    if (type == Long) return ServiceResult.ok((Long) property)
                }
            }
            if (type.isInstance(property)) {
                return ServiceResult.ok(type.cast(property))
            } else {
                return ServiceResult.fail(
                        message: "Bad request",
                        suggestedHttpStatus: HttpStatus.BAD_REQUEST.value(),
                        information: [why: "Invalid type for '$key'. " +
                                "Expected $type got ${property?.class?.simpleName}"]
                )
            }
        }

        return ServiceResult.fail(
                message: "Bad request",
                suggestedHttpStatus: HttpStatus.BAD_REQUEST.value(),
                informatino: [why: "Property '$key' does not exist on event."]
        )
    }

    private <T> ServiceResult<T> checkForErrors(ServiceResult... result) {
        return result.find { !it.success }?.convertFailure()
    }
}
