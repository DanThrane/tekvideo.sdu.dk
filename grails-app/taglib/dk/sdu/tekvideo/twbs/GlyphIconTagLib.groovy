package dk.sdu.tekvideo.twbs

class GlyphIconTagLib {
    static namespace = "twbs"

    def icon = { attrs, body ->
        Icon icon = attrs.icon // Kept as an enum to assist auto-completion
        out << """<span class="glyphicon glyphicon-$icon.name"></span>"""
    }
}