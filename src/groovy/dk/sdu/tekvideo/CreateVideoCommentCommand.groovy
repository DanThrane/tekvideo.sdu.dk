package dk.sdu.tekvideo

import grails.validation.Validateable

@Validateable
class CreateVideoCommentCommand {
    Video id
    String comment
}
