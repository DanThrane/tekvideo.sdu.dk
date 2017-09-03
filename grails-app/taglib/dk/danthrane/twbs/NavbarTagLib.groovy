package dk.danthrane.twbs

import dk.danthrane.util.TagCaptureService
import dk.danthrane.util.TagContextService

import static dk.danthrane.TagLibUtils.*

/**
 * @author Dan Thrane
 */
class NavbarTagLib {
    static namespace = "twbs"

    TagCaptureService tagCaptureService
    TagContextService tagContextService

    def navbar = { attrs, body ->
        tagContextService.context("twbs:navbar") {
            String bodyContent = body()
            tagCaptureService.requireTags("twbs:navbar", "navbar-brand")

            NavBarPlacement placement = attrs.remove("placement") as NavBarPlacement ?: NavBarPlacement.DEFAULT
            boolean inverse = optionalBoolean(attrs.remove("inverse"))

            Map model = [navType: (inverse) ? "navbar-inverse" : "navbar-default", navPlacement: placement.className]

            out << render([plugin: "twbs3", template: "/twbs/navbar/navbar", model: model], { bodyContent })
        }
    }

    def navbarPullLeft = { attrs, body ->
        tagContextService.context("twbs:navbarPullLeft") {
            out << body()
        }
    }

    def navbarPullRight = { attrs, body ->
        tagContextService.context("twbs:navbarPullRight") {
            out << body()
        }
    }

    def navbarText = { attrs, body ->
        String clazz = attrs.remove("class")
        String pull = getNavBarPullClass()
        Map model = [clazz: clazz, pull: pull, attrs: attrs]
        out << render([plugin: "twbs3", template: "/twbs/navbar/text", model: model], body)
    }

    def navbarNonNavLink = { attrs, body ->
        assistAutoComplete(attrs.elementId, attrs.absolute, attrs.action, attrs.base, attrs.controller, attrs.event,
                attrs.fragment, attrs.id, attrs.mapping, attrs.params, attrs.uri, attrs.url)
        String baseClass = attrs.class ?: ""
        baseClass += " navbar-link"
        attrs.class = baseClass
        out << g.link(attrs, body)
    }

    def navbarLinks = { attrs, body ->
        String clazz = attrs.remove("class")
        String pull = getNavBarPullClass()
        Map model = [clazz: clazz, pull: pull, attrs: attrs]
        out << render([plugin: "twbs3", template: "/twbs/navbar/links", model: model], body)
    }

    def navbarLink = { attrs, body ->
        assistAutoComplete(attrs.elementId, attrs.absolute, attrs.action, attrs.base, attrs.controller, attrs.event,
                attrs.fragment, attrs.id, attrs.mapping, attrs.params, attrs.uri, attrs.url)

        String classes = ""
        boolean active = optionalBoolean(attrs.remove("active"))
        boolean disabled = optionalBoolean(attrs.remove("disabled"))
        if (active) {
            classes = "active"
        }
        if (disabled) {
            classes += " disabled"
        }

        String bodyContent = body()
        bodyContent += """<span class="sr-only">(current)</span>"""

        String link = g.link(attrs, { bodyContent })

        Map model = [link: link, classes: classes]
        out << render([plugin: "twbs3", template: "/twbs/navbar/link", model: model], body)
    }

    def navbarForm = { attrs, body ->
        tagContextService.context("twbs:navbarForm") {
            String clazz = attrs.remove("class")
            String pull = getNavBarPullClass()
            Map model = [clazz: clazz, pull: pull, attrs: attrs]
            out << render([plugin: "twbs3", template: "/twbs/navbar/form", model: model], body)
        }
    }

    private String getNavBarPullClass() {
        switch (getNavBarPull()) {
            case -1:
                return "navbar-left"
            case 0:
                return ""
            case 1:
                return "navbar-right"
            default:
                throw new IllegalArgumentException("Unknown pull")
        }
    }

    private int getNavBarPull() {
        if (tagContextService.isInContext("twbs:navbarPullLeft")) {
            return -1
        } else if (tagContextService.isInContext("twbs:navbarPullRight")) {
            return 1
        }
        return 0
    }

}
