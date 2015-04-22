package dk.sdu.tekvideo.twbs

class ButtonTagLib {
    static namespace = "twbs"

    static enum ButtonSize {
        LARGE(clazz: "lg"),
        DEFAULT(clazz: null),
        SMALL(clazz: "sm"),
        XTRA_SMALL(clazz: "xs")

        String clazz
    }

    def button = { attrs, body ->
        String style = attrs.btnstyle ? attrs.btnstyle : "default"
        String type = attrs.type ? attrs.type : null
        String name = attrs.name ? attrs.name : ""
        String value = attrs.value ? attrs.value : ""
        String formaction = attrs.formaction ? attrs.formaction : name
        String sizeAttr = computeSizeAttribute(attrs.size as ButtonSize)

        out << "<button"
        if (type) {
            out << " type=\"$type\""
        }
        out << "class=\"btn btn-$style $sizeAttr\" name=\"$name\" value=\"$value\" value=\"$formaction\">${body()}" +
                "</button>"
    }

    def linkButton = { attrs, body ->
        String style = attrs.btnstyle ? attrs.btnstyle : "default"
        String controller = attrs.controller ?: null
        String action = attrs.action ?: null
        String linkId = attrs.linkId ?: null
        String clazz = attrs.class ?: ""
        String id = attrs.id ?: ""
        String sizeAttr = computeSizeAttribute(attrs.size as ButtonSize)
        Map attributes = attrs.attributes ?: []

        out << "<a href=\"${computeLink(controller, action, linkId)}\" class=\"btn btn-$style " +
                "$sizeAttr $clazz\" id=\"$id\""
        attributes.each { k, v ->
            out << "$k='$v'"
        }
        out << ">"
        out << body()
        out << "</a>"
    }

    String computeSizeAttribute(ButtonSize size) {
        return (size == null || size == ButtonSize.DEFAULT) ? "" : "btn-${size.clazz}"
    }

    String computeLink(String controller, String action, String id) {
        if (controller == null) return "#"
        return createLink(controller: controller, action: action, id: id)
    }

}
