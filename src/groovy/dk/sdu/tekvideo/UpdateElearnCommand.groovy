package dk.sdu.tekvideo

import grails.validation.Validateable

@Validateable
class UpdateElearnCommand {
    String elearn

    static constraints = {
        elearn blank: false, nullable: false
    }
}
