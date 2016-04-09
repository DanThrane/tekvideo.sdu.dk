package dk.sdu.tekvideo

import grails.validation.Validateable

@Validateable
class MoveVideoCommand {
    Video video
    Subject newSubject
    Integer position

    static constraints = {
        video nullable: false
        newSubject nullable: false
        position nullable: false, min: 0
    }
}
