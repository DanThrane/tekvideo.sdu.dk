package dk.sdu.tekvideo.twbs

class GridTagLib {
    static namespace = "twbs"

    def container = { attrs, body ->
        out << '<div class="container">'
        out << body()
        out << '</div>'
    }

    def row = { attrs, body ->
        out << '<div class="row">'
        out << body()
        out << '</div>'
    }

    def column = { attrs, body ->
        def type = attrs.type ? attrs.type : "md"
        def columns = attrs.cols ? attrs.cols : "12"
        out << "<div class=\"col-$type-$columns\">"
        out << body()
        out << "</div>"
    }

}
