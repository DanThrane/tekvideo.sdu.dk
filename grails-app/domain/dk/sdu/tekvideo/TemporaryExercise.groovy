package dk.sdu.tekvideo

class TemporaryExercise {
    String name
    String exercise

    static mapping = {
        exercise type: "text"
        version false
    }

    static constraints = {
        name unique: true
    }
}
