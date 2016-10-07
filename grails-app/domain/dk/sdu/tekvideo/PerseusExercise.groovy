package dk.sdu.tekvideo

class PerseusExercise extends Exercise {
    @Override
    Node getParent() {
        SubjectExercise.findByExercise(this)?.subject
    }
}
