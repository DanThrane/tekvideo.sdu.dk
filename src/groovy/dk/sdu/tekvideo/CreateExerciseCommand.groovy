package dk.sdu.tekvideo

import grails.validation.Validateable

@Validateable
class CreateExerciseCommand {
    String name
    Integer streakToPass
    String description
    String thumbnailUrl
    Subject subject
    List<CreateExerciseItemCommand> exercises
    Boolean isEditing
    WrittenExerciseGroup editing

    static constraints = {
        name nullable: false
        subject nullable: false
        isEditing nullable: false
        streakToPass nullable: false
        thumbnailUrl nullable: true, blank: true
        editing nullable: true, validator: { val, obj ->
            if (obj.isEditing) {
                return val != null
            } else {
                return true
            }
        }
    }
}
