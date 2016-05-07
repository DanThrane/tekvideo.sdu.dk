package dk.sdu.tekvideo

import static dk.sdu.tekvideo.ServiceResult.ok

/**
 * @author Dan Thrane
 */
class DomainServiceUpdater<C extends CRUDCommand, D> {
    final C command

    DomainServiceUpdater(C command) {
        this.command = command
    }

    /**
     * The initialization phase. This can be used to prepare the command object
     * before any validation is performed on it. It may also be used to make
     * any security checks that can be performed pre-validation.
     *
     * If this method fails, then the entire dispatch will fail with the error
     * supplied by this method. This method will by default return "ok".
     *
     * This method should be overridden.
     *
     * @return  "ok" with any value if execution can continue, otherwise a
     *          failure
     */
    protected ServiceResult<Void> init() {
        ok null
    }

    /**
     * Performs validation on the domain object supplied in the command object.
     *
     * This method is usually not overridden.
     *
     * @return  "ok" if the domain validates, otherwise a failure
     */
    protected ServiceResult<Void> domainValidation() {
        GormUtil.validateDomain(command.domain)
    }

    /**
     * An additional validation phase run after the traditional validation
     *
     * @return  "ok" if the validation passes, otherwise a failure
     */
    protected ServiceResult<Void> postValidation() {
        ok null
    }

    protected ServiceResult<D> postSave() {
        ok(command.domain as D)
    }

    protected void save() {
        GormUtil.saveDomain(command.domain)
    }

    ServiceResult<D> dispatch() {
        def initResult = init()
        if (initResult.success) {
            def validation = domainValidation()
            if (validation.success) {
                def postValidation = postValidation()
                if (postValidation.success) {
                    save()
                    return postSave()
                } else {
                    return postValidation as ServiceResult<D>
                }
            } else {
                return validation as ServiceResult<D>
            }
        } else {
            return initResult as ServiceResult<D>
        }
    }
}