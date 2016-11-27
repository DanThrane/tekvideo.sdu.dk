package dk.sdu.tekvideo

class WrittenExercise extends Exercise {
    @Override
    Node getParent() {
        SubjectExercise.findByExercise(this)?.subject
    }
}
