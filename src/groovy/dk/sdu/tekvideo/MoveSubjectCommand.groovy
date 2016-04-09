package dk.sdu.tekvideo

import grails.validation.Validateable

@Validateable
class MoveSubjectCommand {
    Subject subject
    Course newCourse
    Integer position

    static constraints = {
        subject nullable: false
        newCourse nullable: false
        position nullable: false, min: 0
    }
}
