package dk.sdu.tekvideo

import grails.validation.Validateable

/**
 * @author Dan Thrane
 */
@Validateable
class CourseCRUDCommand extends CRUDCommand<Course> {
    Course domain
    Boolean isEditing
    Boolean visible
}
