package dk.sdu.tekvideo.twbs

import org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib

class GridTagLib {
    static namespace = "twbs"

    def container = { attrs, body ->
        String clazz = attrs.class ? attrs.class : ""
        String id = attrs.id ? "id='$attrs.id'" : ""
        out << "<div class=\"container $clazz\" $id>"
        out << body()
        out << '</div>'
    }

    def fluidContainer = { attrs, body ->
        String clazz = attrs.class ? attrs.class : ""
        String id = attrs.id ? "id='$attrs.id'" : ""
        out << "<div class=\"container-fluid $clazz\" $id>"
        out << body()
        out << '</div>'
    }

    def row = { attrs, body ->
        String clazz = attrs.remove("class") ?: ""
        out << """<div class=\"row $clazz\""""
        out << ApplicationTagLib.attrsToString(attrs)
        out << '>'
        out << body()
        out << '</div>'
    }

    def column = { attrs, body ->
        String type = attrs.type ? attrs.type : "md"
        String columns = attrs.cols ? attrs.cols : "12"
        String clazz = attrs.class ? attrs.class : ""
        String offset = attrs.offset ? "col-$type-offset-$attrs.offset" : ""
        out << "<div class=\"col-$type-$columns $clazz $offset\">"
        out << body()
        out << "</div>"
    }

}
