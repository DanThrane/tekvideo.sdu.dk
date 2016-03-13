package dk.sdu.tekvideo

import grails.validation.Validateable

/**
 * @author Dan Thrane
 */
@Validateable
class CourseCRUDCommand implements CRUDCommand<Course> {
    Course domain
    Boolean isEditing
    Boolean visible
}
