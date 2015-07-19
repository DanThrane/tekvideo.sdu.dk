package dk.sdu.tekvideo

import grails.validation.Validateable

/**
 * @author Dan Thrane
 */
@Validateable
class CourseCRUDCommand {
    Course course
    Boolean isEditing
}
