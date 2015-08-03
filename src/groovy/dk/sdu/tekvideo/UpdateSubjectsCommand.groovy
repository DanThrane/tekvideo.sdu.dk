package dk.sdu.tekvideo

import grails.validation.Validateable

@Validateable
class UpdateSubjectsCommand {
    Course course
    List<Subject> order

    static constraints = {
        course nullable: false
        order nullable: true
    }
}
