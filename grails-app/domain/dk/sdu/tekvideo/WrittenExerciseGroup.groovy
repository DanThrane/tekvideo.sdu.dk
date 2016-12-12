package dk.sdu.tekvideo

class WrittenExerciseGroup extends Exercise {
    static hasMany = [exercises: WrittenExercise]
    String thumbnailUrl

    static mapping = {
        version false
    }

    static constraints = {
        thumbnailUrl nullable: true
    }

    @Override
    Node getParent() {
        SubjectExercise.findByExercise(this)?.subject
    }
}
