package dk.sdu.tekvideo

import dk.sdu.tekvideo.v2.Subject2
import dk.sdu.tekvideo.v2.Video2
import grails.validation.Validateable

/**
 * @author Dan Thrane
 */
@Validateable
class UpdateVideosCommand2 {
    Subject2 subject
    List<Video2> order

    static constraints = {
        subject nullable: false
        order nullable: true
    }
}
