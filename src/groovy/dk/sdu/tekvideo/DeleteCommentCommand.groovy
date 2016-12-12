package dk.sdu.tekvideo

import grails.validation.Validateable

@Validateable
class DeleteCommentCommand {
    Long id
    Long comment
}
