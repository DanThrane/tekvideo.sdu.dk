package dk.sdu.tekvideo.twbs

import static dk.sdu.tekvideo.TagLibUtils.getRequiredAttribute

class GlyphIconTagLib {
    static namespace = "twbs"

    def icon = { attrs, body ->
        String clazz = attrs.class ? attrs.class : ""
        Icon icon = getRequiredAttribute(attrs, "icon", "twbs:icon") // Kept as an enum to assist auto-completion
        out << """<span class="glyphicon glyphicon-$icon.name $clazz"></span>"""
    }
}
