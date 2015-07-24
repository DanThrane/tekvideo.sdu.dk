package dk.sdu.tekvideo

import grails.validation.Validateable

/**
 * @author Dan Thrane
 */
@Validateable
class SubjectCRUDCommand extends CRUDCommand<Subject> {
    Subject domain
    Boolean isEditing
}
