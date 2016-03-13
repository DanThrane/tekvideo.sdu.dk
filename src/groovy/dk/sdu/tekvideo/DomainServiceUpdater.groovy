package dk.sdu.tekvideo

import static dk.sdu.tekvideo.ServiceResult.ok

/**
 * @author Dan Thrane
 */
class DomainServiceUpdater<C extends CRUDCommand, D> {
    private Closure<ServiceResult<Void>> domainValidationClosure = { GormUtil.validateDomain(it.domain) }
    private Closure<ServiceResult<Void>> postValidationClosure = { ok() }
    private saveClosure = { C command -> GormUtil.saveDomain(command.domain) }
    private Closure<ServiceResult<Void>> initClosure = { ok() }

    DomainServiceUpdater(@DelegatesTo(DomainServiceUpdater) Closure init = {}) {
        init.delegate = this
        init.resolveStrategy = Closure.DELEGATE_FIRST
        init()
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
    void init(Closure<ServiceResult<Void>> initClosure) {
        this.initClosure = initClosure
    }

    /**
     * Performs validation on the domain object supplied in the command object.
     *
     * This method is usually not overridden.
     *
     * @return  "ok" if the domain validates, otherwise a failure
     */
    void domainValidation(Closure<ServiceResult<Void>> domainValidationClosure) {
        this.domainValidationClosure = domainValidationClosure
    }

    /**
     * An additional validation phase run after the traditional validation
     *
     * @return  "ok" if the validation passes, otherwise a failure
     */
    void postValidation(Closure<ServiceResult<Void>> postValidationClosure) {
        this.postValidationClosure = postValidationClosure
    }

    void save(Closure saveClosure) {
        this.saveClosure = saveClosure
    }

    ServiceResult<Void> doDomainValidation(C command) {
        return domainValidationClosure(command)
    }

    ServiceResult<D> dispatch(C command) {
        def initResult = initClosure(command)
        if (initResult.success) {
            def validation = domainValidationClosure(command)
            if (validation.success) {
                def postValidation = postValidationClosure(command)
                if (postValidation.success) {
                    saveClosure(command)
                    return ok(command.domain as D)
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
