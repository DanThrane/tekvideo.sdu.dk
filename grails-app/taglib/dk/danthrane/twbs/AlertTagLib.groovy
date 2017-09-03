package dk.danthrane.twbs

class AlertTagLib {
    static namespace = "twbs"

    def alert = { attrs, body ->
        boolean dismissible = attrs.dismissible ?: true
        String type = attrs.type ?: "info"
        out << "<div class=\"alert alert-$type"
        if (dismissible) {
            out << " alert-dismissible"
        }
        out << "\" role=\"alert\">"
        if (dismissible) {
            out << "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">" +
                    "<span aria-hidden=\"true\">&times;</span></button>"
        }
        out << body()
        out << "</div>"
    }

}
