package dk.sdu.tekvideo

import grails.validation.Validateable

/**
 * @author Dan Thrane
 */
@Validateable
class UpdateExercisesCommand {
    Subject subject
    List<Exercise> order

    static constraints = {
        subject nullable: false
        order nullable: true
    }
}
