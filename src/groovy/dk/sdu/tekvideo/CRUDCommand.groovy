package dk.sdu.tekvideo

/**
 * @author Dan Thrane
 */
trait CRUDCommand<D> {
    D domain
    Boolean isEditing
}
