package dk.sdu.tekvideo

import grails.converters.JSON

import static dk.sdu.tekvideo.TagLibUtils.getRequiredAttribute
/**
 * @author Dan Thrane
 */
class InternationalizationTagLib {
    static namespace = "i18n"

    I18nService i18nService

    def exposeMessages = { attrs, body ->
        String prefix = getRequiredAttribute(attrs, "prefix", "i18n:exposeMessages")
        Map keys = i18nService.getAllMessages(prefix)
        out << "var i18n = {};"
        out << "\n"
        out << "i18n.messages = "
        out << (keys as JSON)
        out << ";"
    }
}
