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
    String description
    Boolean isEditing
    Video editing


    static constraints = {
        editing nullable: true
        subject nullable: true
        description nullable: true, blank: true
    }
}
