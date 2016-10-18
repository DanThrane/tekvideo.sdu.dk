package dk.sdu.tekvideo

import grails.validation.Validateable

@Validateable
class CreateSimilarResourceCommand {
    Exercise exercise
    String title
    String link
}
