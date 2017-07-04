package dk.danthrane.twbs

import static dk.danthrane.TagLibUtils.assistAutoComplete
import static dk.danthrane.TagLibUtils.fail

class ModalTagLib {
    static namespace = "twbs"

    def modal = { attrs, body ->
        def bodyContent = body()
        Boolean animation = attrs.remove("animation") ?: true
        ModalSize size = attrs.remove("size") ?: ModalSize.DEFAULT
        String klass = attrs.remove("class")
        String sizeClass = (size != ModalSize.DEFAULT) ? "modal-${size.type}" : ""

        List classes = []
        if (animation) classes += "fade"
        if (klass) classes += klass

        def model = [clazz: classes.join(" "), sizeClass: sizeClass, attrs: attrs]
        out << render([plugin: "twbs3", template: "/twbs/modal/modal", model: model], { bodyContent })
    }

    def modalHeader = { attrs, body ->
        out << g.content([key: "modal-header"], body)
    }

    def modalFooter = { attrs, body ->
        out << g.content([key: "modal-footer"], body)
    }

    def modalButton = { attrs, body ->
        assistAutoComplete(attrs.size, attrs.style, attrs.class, attrs.block, attrs.active, attrs.disabled)
        String target = attrs.target ?: fail("twbs:modalButton", "target")
        attrs."data-toggle" = "modal"
        attrs."data-target" = target
        out << twbs.button(attrs, body)
    }

    static enum ModalSize {
        DEFAULT(type: null),
        MEDIUM(type: "md"),
        LARGE(type: "lg")

        String type
    }
}
