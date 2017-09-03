package dk.sdu.tekvideo

import grails.validation.Validateable

/**
 * @author Dan Thrane
 */
class CreateOrUpdateVideoCommand implements Validateable {
    Subject subject
    String name
    String youtubeId
    String timelineJson
    String description
    Boolean isEditing
    Video editing
    Boolean videoType
    Boolean visible

    static constraints = {
        editing nullable: true
        subject nullable: true
        description nullable: true, blank: true
    }
}
