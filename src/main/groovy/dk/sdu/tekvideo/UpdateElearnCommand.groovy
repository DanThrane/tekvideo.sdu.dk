package dk.sdu.tekvideo

import grails.validation.Validateable

class UpdateElearnCommand implements Validateable {
    String elearn

    static constraints = {
        elearn blank: false, nullable: false
    }
}
