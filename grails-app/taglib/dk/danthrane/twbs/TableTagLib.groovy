package dk.danthrane.twbs

import static dk.danthrane.TagLibUtils.*

/**
 * A tag lib for the table component.
 *
 * @author Dan Thrane
 */
class TableTagLib {
    static namespace = "twbs"

    def table = { attrs, body ->
        String clazz = attrs.remove("class")
        boolean striped = optionalBoolean(attrs.remove("striped"))
        boolean bordered = optionalBoolean(attrs.remove("bordered"))
        boolean hover = optionalBoolean(attrs.remove("hover"))
        boolean condensed = optionalBoolean(attrs.remove("condensed"))
        boolean responsive = optionalBoolean(attrs.remove("responsive"))

        List<String> tableExtraOptions = []
        if (striped) {
            tableExtraOptions += "table-striped"
        }
        if (bordered) {
            tableExtraOptions += "table-bordered"
        }
        if (hover) {
            tableExtraOptions += "table-hover"
        }
        if (condensed) {
            tableExtraOptions += "table-condensed"
        }

        def model = [clazz: clazz, extraOptions: tableExtraOptions.join(" "), responsive: responsive, attrs: attrs]
        out << render([plugin: "twbs3", template: "/twbs/table/table", model: model], body)
    }

    def tr = { attrs, body ->
        String clazz = attrs.remove("class")
        ContextualColor context = attrs.remove("context") as ContextualColor ?: ContextualColor.DEFAULT
        out << render([plugin: "twbs3", template: "/twbs/table/tr", model: [clazz: clazz, context: context,
                                                                      attrs: attrs]], body)
    }

    def td = { attrs, body ->
        String clazz = attrs.remove("class")
        ContextualColor context = attrs.remove("context") as ContextualColor ?: ContextualColor.DEFAULT
        out << render([plugin: "twbs3", template: "/twbs/table/td", model: [clazz: clazz, context: context,
                                                                      attrs: attrs]], body)
    }

    def th = { attrs, body ->
        String clazz = attrs.remove("class")
        ContextualColor context = attrs.remove("context") as ContextualColor ?: ContextualColor.DEFAULT
        out << render([plugin: "twbs3", template: "/twbs/table/th", model: [clazz: clazz, context: context,
                                                                      attrs: attrs]], body)
    }

}
