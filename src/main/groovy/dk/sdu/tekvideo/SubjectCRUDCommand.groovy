package dk.sdu.tekvideo

import grails.validation.Validateable

/**
 * @author Dan Thrane
 */
class SubjectCRUDCommand implements Validateable {
    Subject domain
    Boolean isEditing
    Boolean visible
}
