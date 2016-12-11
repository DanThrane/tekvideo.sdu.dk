package dk.sdu.tekvideo

import grails.validation.Validateable

@Validateable
class CreateExerciseItemCommand {
    Long identifier
    String exercise

    static constraints = {
        identifier nullable: true
    }
}
