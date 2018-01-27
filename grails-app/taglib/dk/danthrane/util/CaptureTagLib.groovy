package dk.danthrane.util

import static dk.danthrane.TagLibUtils.*

/**
 * @author Dan Thrane
 */
class CaptureTagLib {
    TagCaptureService tagCaptureService

    def content = { attrs, body ->
        String key = attrs.key ?: fail(String, "key", "g:content")
        tagCaptureService.captureTag(key, body)
    }

    def selectContent = { attrs, body ->
        String key = attrs.key ?: fail(String, "key", "g:selectContent")
        Closure tag = tagCaptureService.retrieveTag(key)

        if (tag != null) {
            out << tag()
        }
    }

    def ifContentAvailable = { attrs, body ->
        String key = attrs.key ?: fail(String, "key", "g:ifContentAvailable")

        if (tagCaptureService.hasTag(key)) {
            out << body()
        }
    }

    def ifContentNotAvailable = { attrs, body ->
        String key = attrs.key ?: fail(String, "key", "g:ifContentNotAvailable")

        if (!tagCaptureService.hasTag(key)) {
            out << body()
        }
    }

}
