package dk.sdu.tekvideo

import static dk.danthrane.TagLibUtils.fail

class FaIconTagLib {
    static namespace = "fa"

    def icon = { attrs, body ->
        FaIcon icon = attrs.icon ?: fail(FaIcon, "icon", "fa:icon")
        Integer rotate = attrs.rotate ?: -1
        Boolean pulse = attrs.pulse ?: false
        Boolean spin = attrs.spin ?: false
        Integer size = attrs.size ? Integer.parseInt(attrs.size) : -1
        Boolean border = attrs.border ?: false
        Boolean inverse = attrs.inverse ?: false
        Boolean fixedWidth = attrs.fixedWidth ?: false

        out << "<i class=\"fa fa-$icon.name"
        if (rotate != -1) {
            out << " fa-rotate-$rotate"
        }
        if (pulse) {
            out << " fa-pulse"
        }
        if (spin) {
            out << " fa-spin"
        }
        if (size != -1) {
            if (size == 1) {
                out << " fa-lg"
            } else {
                out << " fa-${size}x"
            }
        }
        if (border) {
            out << " fa-border"
        }
        if (inverse) {
            out << " fa-inverse"
        }
        if (fixedWidth) {
            out << " fa-fw"
        }
        out << "\"></i>"
    }

    def require = { attrs, body ->
        out << """<link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css">"""
    }

}
