package dk.sdu.tekvideo

import grails.validation.Validateable

class CreateCommentCommand implements Validateable {
    Exercise id
    String comment
}
