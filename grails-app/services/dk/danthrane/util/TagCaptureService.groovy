package dk.danthrane.util

import org.grails.taglib.GrailsTagException
import org.grails.web.util.WebUtils

import javax.servlet.ServletRequest

class TagCaptureService {

    static final String ATTRIBUTE = "dk.danthrane.util.TagCapture.content"

    void captureTag(String key, Closure tag) {
        ServletRequest request = WebUtils.retrieveGrailsWebRequest().getCurrentRequest()
        Map<String, Closure> attribute = request.getAttribute(ATTRIBUTE) as Map
        if (!attribute) {
            attribute = [:]
            request.setAttribute(ATTRIBUTE, attribute)
        }
        attribute[key] = tag
    }

    Map<String, Closure<String>> getTags() {
        ServletRequest request = WebUtils.retrieveGrailsWebRequest().getCurrentRequest()
        return request.getAttribute(ATTRIBUTE) as Map
    }

    Closure<String> retrieveTag(String key) {
        def attribute = getTags()
        if (attribute != null && attribute.containsKey(key)) {
            return attribute.remove(key)
        } else {
            return null
        }
    }

    boolean hasTag(String key) {
        def attribute = getTags()
        if (attribute != null && attribute.containsKey(key)) {
            return true
        }
        return false
    }

    void requireTags(String tag, String... tags) {
        Map allTags = getTags()
        if (allTags == null) {
            throw new GrailsTagException("Tag $tag, requires keys: $tags expected, but not found!")
        }
        tags.each {
            if (!allTags.containsKey(it)) {
                throw new GrailsTagException("Key ('$it') expected in $tag, but not found!")
            }
        }
    }
}
