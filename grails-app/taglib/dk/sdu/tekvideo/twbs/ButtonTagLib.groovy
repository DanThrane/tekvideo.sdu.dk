package dk.sdu.tekvideo.twbs

class ButtonTagLib {
    static namespace = "twbs"

    def button = { attrs, body ->
        String style = attrs.btnstyle ? attrs.btnstyle : "default"
        String type = attrs.type

        out << "<button"
        if (type) {
            out << " type=\"$type\""
        }
        out << "class=\"btn btn-$style\">${body()}</button>"
    }
}
