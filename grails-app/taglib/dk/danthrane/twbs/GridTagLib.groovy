package dk.danthrane.twbs

import dk.danthrane.TagLibUtils

class GridTagLib {
    static namespace = "twbs"

    def container = { attrs, body ->
        boolean fluid = attrs.fluid ?: false
        String clazz = attrs.class ?: ""
        String id = attrs.id ? "id='$attrs.id'" : ""
        out << "<div class=\"container${fluid ? "-fluid" : ""} $clazz\" $id>"
        out << body()
        out << '</div>'
    }

    def row = { attrs, body ->
        String clazz = attrs.remove("class") ?: ""
        out << "<div class='row $clazz' ${TagLibUtils.expandAttributes(attrs)}>"
        out << body()
        out << '</div>'
    }

    def column = { attrs, body ->
        // Deprecated
        GridSize type = attrs.type ?: GridSize.MEDIUM
        String columns = attrs.cols ?: "12"
        String clazz = attrs.class ?: ""
        String offset = attrs.offset ? "col-${type.columnName}-offset-$attrs.offset" : ""

        String sm = attrs.sm
        String md = attrs.md
        String lg = attrs.lg
        String pullSm = attrs."pull-sm"
        String pullMd = attrs."pull-md"
        String pullLg = attrs."pull-lg"
        String pushSm = attrs."push-sm"
        String pushMd = attrs."push-md"
        String pushLg = attrs."push-lg"
        String offsetSm = attrs."offset-sm"
        String offsetMd = attrs."offset-md"
        String offsetLg = attrs."offset-lg"

        List classes = []
        if (sm || md || lg) {
            if (sm) classes += "col-sm-$sm"
            if (md) classes += "col-sm-$md"
            if (lg) classes += "col-sm-$lg"

            if (pullSm) classes += "col-sm-pull-$pullSm"
            if (pullMd) classes += "col-md-pull-$pullMd"
            if (pullLg) classes += "col-lg-pull-$pullLg"

            if (pushSm) classes += "col-sm-push-$pushSm"
            if (pushMd) classes += "col-md-push-$pushMd"
            if (pushLg) classes += "col-lg-push-$pushLg"

            if (offsetSm) classes += "col-sm-offset-$offsetSm"
            if (offsetMd) classes += "col-md-offset-$offsetMd"
            if (offsetLg) classes += "col-lg-offset-$offsetLg"
        } else {
            classes += "col-${type.columnName}-$columns"
            classes += offset
        }
        classes += clazz

        out << "<div class=\"${classes.join(" ")}\">"
        out << body()
        out << "</div>"
    }

}
