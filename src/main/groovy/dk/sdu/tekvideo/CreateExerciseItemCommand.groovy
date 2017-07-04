package dk.sdu.tekvideo

class CreateExerciseItemCommand {
    Long identifier
    String exercise

    static constraints = {
        identifier nullable: true
    }
}
