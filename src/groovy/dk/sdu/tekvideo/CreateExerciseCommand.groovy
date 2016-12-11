package dk.sdu.tekvideo

import grails.validation.Validateable

@Validateable
class CreateExerciseCommand {
    String name
    String description
    Subject subject
    List<CreateExerciseItemCommand> exercises
    Boolean isEditing
    WrittenExerciseGroup editing

    static constraints = {
        name nullable: false
        subject nullable: false
        isEditing nullable: false
        editing nullable: true, validator: { val, obj ->
            if (obj.isEditing) {
                return val != null
            } else {
                return true
            }
        }
    }
}
