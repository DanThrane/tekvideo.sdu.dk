package dk.sdu.tekvideo

class WrittenExercise extends Exercise {
    String exercise

    static mapping = {
        exercise type: "text"
        version false
    }

    @Override
    Node getParent() {
        SubjectExercise.findByExercise(this)?.subject
    }
}
