package dk.sdu.tekvideo

import grails.validation.Validateable

@Validateable
class CreateCommentCommand {
    Exercise id
    String comment
}
