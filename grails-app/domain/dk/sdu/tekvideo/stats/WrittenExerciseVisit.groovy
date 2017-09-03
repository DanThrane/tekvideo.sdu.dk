package dk.sdu.tekvideo.stats

import dk.sdu.tekvideo.WrittenExercise

class WrittenExerciseVisit {
    ExerciseProgress progress
    WrittenExercise subExercise
    Date timestamp

    static mapping = {
        version false
    }
}
