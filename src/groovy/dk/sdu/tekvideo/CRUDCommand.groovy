package dk.sdu.tekvideo

import grails.validation.Validateable

/**
 * @author Dan Thrane
 */
@Validateable
class CRUDCommand<D> {
    D domain
    Boolean isEditing
}
