package dk.sdu.tekvideo.twbs

class LabelTagLib {
    static namespace = "twbs"

    def label = { attrs, body ->
        String type = attrs.type
        out << """
        <span class="label label-$type">
            ${body()}
        <span>
        """
    }
}
