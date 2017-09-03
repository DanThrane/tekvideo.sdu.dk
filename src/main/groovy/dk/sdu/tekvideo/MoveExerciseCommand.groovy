package dk.sdu.tekvideo

import grails.validation.Validateable

class MoveExerciseCommand implements Validateable {
    Exercise exercise
    Subject newSubject
    Integer position

    static constraints = {
        exercise nullable: false
        newSubject nullable: false
        position nullable: false, min: 0
    }
}
