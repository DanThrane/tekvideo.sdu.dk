package dk.sdu.tekvideo

class WrittenExerciseGroup extends Exercise {
    static hasMany = [exercises: WrittenExercise]

    static mapping = {
        version false
    }

    @Override
    Node getParent() {
        SubjectExercise.findByExercise(this)?.subject
    }
}
