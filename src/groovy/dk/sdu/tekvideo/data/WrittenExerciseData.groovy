package dk.sdu.tekvideo.data

import dk.sdu.tekvideo.Subject
import dk.sdu.tekvideo.SubjectExercise
import dk.sdu.tekvideo.WrittenExercise
import dk.sdu.tekvideo.WrittenExerciseGroup

import java.util.concurrent.atomic.AtomicInteger

class WrittenExerciseData {
    private static AtomicInteger idx = new AtomicInteger(0)

    static WrittenExerciseGroup buildTestExercise(String prefix = "WrittenExercise",
                                                  Subject subject = null, boolean includeIdSuffix = false,
                                                  int streakToPass = 1, int numberOfSubExercises = 1) {
        if (subject == null) subject = SubjectData.buildTestSubject()

        String name = prefix
        if (includeIdSuffix) name += idx.getAndIncrement()

        def result = new WrittenExerciseGroup()
        result.name = name
        result.streakToPass = streakToPass
        result.save(failOnError: true, flush: true)

        SubjectExercise.create(subject, result, [save: true, failOnError: true])

        (0..<numberOfSubExercises).each {
            def exercise = new WrittenExercise()
            exercise.exercise = "foo-$it"
            result.addToExercises(exercise).save(failOnError: true, flush: true)
        }

        return result
    }
}
