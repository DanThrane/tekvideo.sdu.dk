package dk.sdu.tekvideo

import org.codehaus.groovy.grails.web.util.WebUtils
import org.springframework.context.MessageSource
import org.springframework.web.servlet.support.RequestContextUtils

class I18nService {
    MessageSource messageSource

    Map<Object, Object> getAllMessages() {
        Locale locale = RequestContextUtils.getLocale(WebUtils.retrieveGrailsWebRequest().request)
        return messageSource.withTraits(ExposeMessagePropertiesTrait).getMessageKeys(locale)
    }

    Map<Object, Object> getAllMessages(String prefix) {
        return getAllMessages().findAll { key, value -> key.startsWith(prefix) }
    }
}
