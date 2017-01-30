package dk.sdu.tekvideo

import grails.converters.JSON

class TekVideoTagLib {
    static namespace = "tv"

    def browser = { attrs, body ->
        List<NodeBrowserInformation> items = (List) attrs.remove("items")
        items.each { println(it.description) }

        out << "<tv-browser items='"
        out << (items as JSON).toString().encodeAsHTML()
        out << "'></tv-browser>"
    }
}
