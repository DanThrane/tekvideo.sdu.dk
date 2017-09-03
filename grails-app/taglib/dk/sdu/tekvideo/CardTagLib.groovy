package dk.sdu.tekvideo

/**
 * @author Dan Thrane
 */
class CardTagLib {
    static namespace = "sdu"

    def card = { attrs, body ->
        int cols = attrs.remove("cols") ?: 12
        String clazz = attrs.remove("class")
        String id = attrs.remove("id")
        out << render([template: "/card/card", model: [cols: cols, clazz: clazz, id: id]], body)
    }

    def linkCard = { attrs, body ->
        int cols = attrs.remove("cols") ?: 12
        String clazz = attrs.remove("class")
        String link = g.link(attrs, { out << "<span class='block-link'></span>" })
        out << render([template: "/card/linkCard", model: [link: link, cols: cols, clazz: clazz]], body)
    }
}
