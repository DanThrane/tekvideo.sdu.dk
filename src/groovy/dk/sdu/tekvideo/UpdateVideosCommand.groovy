package dk.sdu.tekvideo

import grails.validation.Validateable

/**
 * @author Dan Thrane
 */
@Validateable
class UpdateVideosCommand {
    Subject subject
    List<Video> order

    static constraints = {
        subject nullable: false
        order nullable: true
    }
}
