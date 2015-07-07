package dk.sdu.tekvideo

import grails.validation.Validateable

/**
 * @author Dan Thrane
 */
@Validateable
class CreateVideoCommand {
    Subject subject
    String name
    String youtubeId
    String timelineJson
}
