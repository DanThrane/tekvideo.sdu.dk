package dk.danthrane.twbs

/**
 * @author Dan Thrane
 */
class MiscTagLib {
    static namespace = "twbs"

    def caret = { attrs, body ->
        out << "<span class='caret'></span>"
    }

    def pageHeader = { attrs, body ->
        out << "<div class=\"page-header\">\n" +
                body() +
                "</div>"
    }

    def badge = { attrs, body ->
        out << "<span class=\"badge\">${body()}</span>"
    }

}
