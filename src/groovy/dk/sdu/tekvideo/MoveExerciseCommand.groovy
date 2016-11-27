package dk.sdu.tekvideo

import grails.validation.Validateable

@Validateable
class MoveExerciseCommand {
    Exercise exercise
    Subject newSubject
    Integer position

    static constraints = {
        exercise nullable: false
        newSubject nullable: false
        position nullable: false, min: 0
    }
}
