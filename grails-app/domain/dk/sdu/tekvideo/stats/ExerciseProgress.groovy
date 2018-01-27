package dk.sdu.tekvideo.stats

import dk.sdu.tekvideo.Exercise
import dk.sdu.tekvideo.User

class ExerciseProgress {
    Exercise exercise
    User user
    String uuid

    static constraints = {
        user nullable: true
        uuid nullable: true
    }

    static mapping = {
        version false
    }
}
