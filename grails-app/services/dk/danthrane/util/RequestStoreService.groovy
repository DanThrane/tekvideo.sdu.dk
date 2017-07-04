package dk.danthrane.util

import org.grails.web.util.WebUtils

import javax.servlet.ServletRequest

/**
 * @author Dan Thrane
 */
class RequestStoreService {

    static final String STORE_PREFIX = "dk.thrane.util.RequestStore."

    def propertyMissing(String name) {
        ServletRequest request = WebUtils.retrieveGrailsWebRequest().getCurrentRequest()
        return request.getAttribute(STORE_PREFIX + name)
    }

    def propertyMissing(String name, value) {
        ServletRequest request = WebUtils.retrieveGrailsWebRequest().getCurrentRequest()
        request.setAttribute(STORE_PREFIX + name, value)
    }
}
