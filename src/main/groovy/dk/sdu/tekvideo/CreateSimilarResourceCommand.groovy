package dk.sdu.tekvideo

import grails.validation.Validateable

class CreateSimilarResourceCommand implements Validateable {
    Long id
    String title
    String link
}
