package dk.sdu.tekvideo

import grails.validation.Validateable

@Validateable
class CreateSimilarResourceCommand {
    Long id
    String title
    String link
}
