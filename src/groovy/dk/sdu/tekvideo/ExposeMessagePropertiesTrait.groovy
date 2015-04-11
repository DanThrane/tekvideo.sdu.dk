package dk.sdu.tekvideo

import groovy.transform.TypeCheckingMode
import grails.compiler.GrailsCompileStatic

/**
 * This trait allows us to extract message codes from the I18n system.
 * Usage: ctx.getBean('messageSource').withTraits(MessagePropertiesTrait).messageKeys(locale)
 * (the locale can be obtained using org.springframework.web.servlet.support.RequestContextUtils.getLocale(request))
 *
 * Source: http://www.razum.si/blog/grails-javascript-i18n-messages
 */
@GrailsCompileStatic(TypeCheckingMode.SKIP)
trait ExposeMessagePropertiesTrait {
    Properties getMessageKeys(Locale locale) {
        this.getMergedProperties(locale).properties
    }

    Properties getPluginMessageKeys(Locale locale) {
        this.getMergedPluginProperties(locale).properties
    }
}