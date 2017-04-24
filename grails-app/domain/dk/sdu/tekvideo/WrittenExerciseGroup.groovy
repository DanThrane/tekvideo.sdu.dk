package dk.sdu.tekvideo

class WrittenExerciseGroup extends Exercise {
    static hasMany = [exercises: WrittenExercise]
    String thumbnailUrl
    Integer streakToPass

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

    @Override
    int getScoreToPass() {
        return streakToPass
    }
}
