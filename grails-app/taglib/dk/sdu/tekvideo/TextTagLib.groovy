package dk.sdu.tekvideo

class TextTagLib {
    static namespace = "sdu"

    def abbreviate = { attrs, body ->
        int maxLength = attrs.remove("length") ?: 250
        String string = body().encodeAsHTML()

        def originalLength = string.length()
        def substring = string.substring(0, Math.min(originalLength, maxLength))
        out << substring
        if (originalLength > maxLength) {
            out << "..."
        }
    }
}
