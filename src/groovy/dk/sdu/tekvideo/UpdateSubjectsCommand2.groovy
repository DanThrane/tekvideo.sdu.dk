package dk.sdu.tekvideo

import dk.sdu.tekvideo.v2.Course2
import dk.sdu.tekvideo.v2.Subject2
import grails.validation.Validateable

@Validateable
class UpdateSubjectsCommand2 {
    Course2 course
    List<Subject2> order

    static constraints = {
        course nullable: false
        order nullable: true
    }
}
