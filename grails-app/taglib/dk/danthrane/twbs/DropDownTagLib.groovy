package dk.danthrane.twbs

import dk.danthrane.util.TagContextService

import static dk.danthrane.TagLibUtils.*

/**
 * @author Dan Thrane
 */
class DropDownTagLib {
    static namespace = "twbs"

    TagContextService tagContextService

    private Map prepareCommonToggleAttributes(attrs, Closure body) {
        String id = attrs.id ?: "dropdownMenu"
        boolean dropup = optionalBoolean(attrs.remove("dropup"))
        boolean expanded = optionalBoolean(attrs.remove("expanded"))
        boolean hideCaret = optionalBoolean(attrs.remove("hideCaret"))

        String dropdownClass = (dropup) ? "dropup" : "dropdown"
        String clazz  = attrs.remove("class") ?: ""

        if (expanded) {
            clazz += " open"
        }

        String bodyContent = body()
        return [id: id, dropdownClass: dropdownClass, clazz: clazz, expanded: expanded, hideCaret: hideCaret,
                     attrs: attrs, bodyContent: bodyContent]
    }

    def dropdownToggle = { attrs, body ->
        assistAutoComplete(attrs.size, attrs.style, attrs.class, attrs.block, attrs.active, attrs.disabled,
                           attrs.dropup, attrs.expanded, attrs.hideCaret, attrs.class)

        Map model = prepareCommonToggleAttributes(attrs, body)
        model.skipWrapper = tagContextService.isInContext("twbs:buttonGroup")
        out << render([plugin: "twbs3", template: "/twbs/dropdown/button", model: model], { model.bodyContent })
    }

    def navDropdownToggle = { attrs, body ->
        assistAutoComplete(attrs.dropup, attrs.expanded, attrs.hideCaret, attrs.class)
        Map model = prepareCommonToggleAttributes(attrs, body)
        out << render([plugin: "twbs3", template: "/twbs/dropdown/navToggle", model: model], { model.bodyContent })
    }

    def dropdownMenu = { attrs, body ->
        String labelledBy = expandOptionalAttribute("aria-labelledby", attrs.remove("labelledBy"))
        String role = attrs.remove("role") ?: "menu"
        String clazz = attrs.remove("class") ?: ""

        Map model = [role: role, clazz: clazz, labelledBy: labelledBy, attrs: attrs]
        out << render([plugin: "twbs3", template: "/twbs/dropdown/menu", model: model], body)
    }

    def dropdownItem = { attrs, body ->
        assistAutoComplete(attrs.size, attrs.style, attrs.class, attrs.block, attrs.active, attrs.disabled,
                attrs.absolute, attrs.action, attrs.base, attrs.controller, attrs.event, attrs.fragment, attrs.id,
                attrs.mapping, attrs.params, attrs.uri, attrs.url)
        boolean disabled = optionalBoolean(attrs.remove("disabled"))
        String disabledClass = ""
        if (!attrs.role) {
            attrs.role = "menuitem"
        }
        if (!attrs.tabindex) {
            attrs.tabindex = -1
        }
        if (disabled) {
            disabledClass = "disabled"
        }
        String link = g.link(attrs, body)
        out << render([plugin: "twbs3", template: "/twbs/dropdown/item", model: [disabled: disabledClass, link: link]])
    }

    def dropdownDivider = { attrs, body ->
        String clazz = attrs.remove("class") ?: ""
        out << render([plugin: "twbs3", template: "/twbs/dropdown/divider", model: [clazz: clazz, attrs: attrs]], body)
    }

    def dropdownHeader = { attrs, body ->
        String clazz = attrs.remove("class") ?: ""
        out << render([plugin: "twbs3", template: "/twbs/dropdown/header", model: [clazz: clazz, attrs: attrs]], body)
    }
}
