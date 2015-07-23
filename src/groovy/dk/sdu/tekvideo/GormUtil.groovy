package dk.sdu.tekvideo

import grails.validation.Validateable
import org.codehaus.groovy.grails.commons.DefaultGrailsDomainClass
import org.codehaus.groovy.grails.commons.DomainClassArtefactHandler
import org.codehaus.groovy.grails.commons.GrailsDomainClassProperty

import java.util.stream.Collectors

import static dk.sdu.tekvideo.ServiceResult.fail
import static dk.sdu.tekvideo.ServiceResult.ok

/**
 * @author Dan Thrane
 */
class GormUtil {
    static List<GrailsDomainClassProperty> getPersistentProperties(Object domain) {
        def clazz = new DefaultGrailsDomainClass(domain.class)
        Arrays.stream(clazz.persistentProperties).filter {
            !it.bidirectional && isValidateable(it.type)
        }.collect(Collectors.toList())
    }

    /**
     * Validates a domain class in a depth-first fashion. Will return "ok" if this object and all of its children will
     * validate.
     *
     * @param domain    The domain object to validate
     * @throws IllegalArgumentException If the object passed to this is not validateable
     * @return
     */
    static ServiceResult<Void> validateDomain(Object domain) {
        if (isValidateable(domain.class)) {
            println "Validating ${domain.class}"
            def result = domain.validate()
            if (!result) {
                fail "domainservice.field_errors"
            } else {
                getPersistentProperties(domain).each {
                    def subResult = validateDomain(domain.properties[it.name])
                    if (!subResult.success) {
                        return subResult
                    }
                }
                ok null
            }
        } else {
            throw new IllegalArgumentException("Object of type ${domain.class} is not validatable!")
        }
    }

    static boolean isValidateable(Class klass) {
        DomainClassArtefactHandler.isDomainClass(klass) || klass.getAnnotation(Validateable) != null
    }

    static void saveDomain(Object domain) {
        if (isValidateable(domain.class)) {
            getPersistentProperties(domain).each {
                def value = domain.properties[it.name]
                saveDomain(value)
            }
            println "Attempting to save ${domain.class}"
            domain.save()
        } else {
            throw new IllegalArgumentException("Object of type ${domain.class} is not validatable!")
        }
    }
}
