package dk.sdu.tekvideo

import grails.validation.Validateable

class UpdateSubjectsCommand implements Validateable {
    Course course
    List<Subject> order

    static constraints = {
        course nullable: false
        order nullable: true
    }
}
