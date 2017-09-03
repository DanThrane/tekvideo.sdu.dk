package dk.sdu.tekvideo

import grails.validation.Validateable

/**
 * @author Dan Thrane
 */
class CourseCRUDCommand implements Validateable {
    Course domain
    Boolean isEditing
    Boolean visible
}
