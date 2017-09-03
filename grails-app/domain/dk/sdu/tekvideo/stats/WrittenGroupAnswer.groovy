package dk.sdu.tekvideo.stats

import dk.sdu.tekvideo.WrittenExercise

class WrittenGroupAnswer {
    ExerciseProgress progress
    WrittenExercise exercise
    Date dateCreated
    Boolean passes

    static mapping = {
        version false
    }
}
