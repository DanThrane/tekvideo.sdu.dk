package dk.danthrane

import org.grails.taglib.GrailsTagException
import org.springframework.web.util.HtmlUtils

class TagLibUtils {

    static <E> E fail(String tagName, String attr) {
        throw new GrailsTagException("Tag $tagName requires attribute $attr!")
    }

    @SuppressWarnings(["UnusedMethodParameter", "GroovyUnusedDeclaration"])
    static <E> E fail(Class<E> clazz, String attr, String tagName) {
        throw new GrailsTagException("Tag $tagName requires attribute $attr!")
    }

    static String expandAttribute(String attr, value) {
        return "$attr=\"${HtmlUtils.htmlEscape(value as String)}\""
    }

    static String expandAttributes(Map<String, ?> attributes) {
        String result = ""
        attributes.each { k, v -> result += expandAttribute(k, v) }
        return result
    }

    static String expandOptionalAttribute(String attr, value) {
        if (value != null) {
            return expandAttribute(attr, value)
        }
        return ""
    }

    static boolean optionalBoolean(attr, boolean defaultValue = false) {
        attr != null ? Boolean.valueOf(attr as String) : defaultValue
    }

    static void assistAutoComplete(... dummy) {
        // Unfortunately we cannot simply ask for the attributes map, as this will break IntelliJ's simple attribute
        // auto-complete. Rather have a bit of extra typing in this tag-lib, than having to remember every attribute
        // for every tag. So we call this function so that it can see that they are in use, even though this function
        // doesn't care at all for them. All the work is really done by prepareCommonInputAttributes
    }

}
