package dk.danthrane.twbs

import dk.danthrane.util.TagContextService

import static dk.danthrane.TagLibUtils.*

class ButtonTagLib {
    static namespace = "twbs"
    TagContextService tagContextService

    private Map prepareCommonButtonAttributes(attrs) {
        // Attributes
        ButtonSize size = attrs.remove("size") ?: ButtonSize.DEFAULT
        ButtonStyle style = attrs.remove("style") ?: ButtonStyle.DEFAULT
        String clazz = attrs.remove("class") ?: ""
        boolean block = optionalBoolean(attrs.remove("block"))
        boolean active = optionalBoolean(attrs.remove("active"))
        boolean disabled = optionalBoolean(attrs.remove("disabled"))
        boolean justified

        // Styling
        def buttonGroupAttributes = tagContextService.getContextAttributes("twbs:buttonGroup")
        if (buttonGroupAttributes?.justified) {
            justified = true
        } else {
            justified = false
        }

        // Preparation
        List classes = ["btn", clazz, "btn-$style.clazz"]
        if (size != ButtonSize.DEFAULT) {
            classes.add("btn-$size.clazz")
        }
        if (block) {
            classes.add("btn-block")
        }
        if (active) {
            classes.add("active")
        }
        if (disabled) {
            classes.add("disabled")
        }
        if (tagContextService.isInContext("twbs:navbar") && !tagContextService.isInContext("twbs:navbarForm")) {
            classes.add("navbar-btn")
        }
        return [disabled: disabled, classes: classes.join(" "), justified: justified, attrs: attrs]
    }

    def button = { attrs, body ->
        assistAutoComplete(attrs.size, attrs.style, attrs.class, attrs.block, attrs.active, attrs.disabled)
        Map model = prepareCommonButtonAttributes(attrs)
        if (model.disabled) {
            model.disabledAttribute = expandAttribute("disabled", "disabled")
        } else {
            model.disabledAttribute = ""
        }

        out << render([plugin: "twbs3", template: "/twbs/button/button", model: model], body)
    }

    def linkButton = { attrs, body ->
        assistAutoComplete(attrs.size, attrs.style, attrs.class, attrs.block, attrs.active, attrs.disabled,
                attrs.absolute, attrs.action, attrs.base, attrs.controller, attrs.event, attrs.fragment, attrs.id,
                attrs.mapping, attrs.params, attrs.uri, attrs.url)
        Map model = prepareCommonButtonAttributes(attrs)
        attrs.class = model.classes
        out << g.link(attrs, body)
    }

    def buttonGroup = { attrs, body ->
        boolean vertical = optionalBoolean(attrs.remove("vertical"))
        boolean justified = optionalBoolean(attrs.remove("justified"))
        ButtonSize size = attrs.remove("size") as ButtonSize ?: ButtonSize.DEFAULT
        String clazz = attrs.remove("class") ?: ""

        List classes = [clazz]
        if (vertical) {
            classes += "btn-group-vertical"
        } else {
            classes += "btn-group"
        }

        if (size != ButtonSize.DEFAULT) {
            classes += "btn-group-$size.clazz"
        }

        if (justified) {
            classes += "btn-group-justified"
        }

        tagContextService.contextWithAttributes("twbs:buttonGroup", [justified: justified]) {
            Map model = [classes: classes.join(" "), attrs: attrs]
            out << render([plugin: "twbs3", template: "/twbs/button/buttonGroup", model: model], body)
        }
    }

    def buttonToolbar = { attrs, body ->
        String clazz = attrs.remove("class") ?: ""
        out << render([plugin: "twbs3", template: "/twbs/button/buttonToolbar", model: [attrs: attrs, clazz: clazz]], body)
    }

}
