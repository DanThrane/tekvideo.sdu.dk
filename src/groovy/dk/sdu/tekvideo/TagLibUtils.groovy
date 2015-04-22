package dk.sdu.tekvideo

import org.codehaus.groovy.grails.web.taglib.exceptions.GrailsTagException

/**
 * @author Dan Thrane
 */
class TagLibUtils {
    static <E> E getRequiredAttribute(attrs, String attr, String tagName) {
        def result = attrs[attr]
        if (result) {
            return result
        }
        throw new GrailsTagException("Tag $tagName requires attribute $attr!")
    }

    static <E> E fail(String attr, String tagName) {
        throw new GrailsTagException("Tag $tagName requires attribute $attr!")
    }

}
