package dk.danthrane.twbs

import java.text.DecimalFormat

import static dk.danthrane.TagLibUtils.*

/**
 * A tag lib for the progress bar component
 *
 * @author Dan Thrane
 */
class ProgressBarTagLib {
    static namespace = "twbs"

    def progressBar = { attrs, body ->
        assistAutoComplete(attrs.min, attrs.max, attrs.value, attrs.context, attrs.showLabel, attrs.animated,
                attrs.striped, attrs.minWidth, attrs.numberFormat, attrs.class)
        def item = progressItem(attrs, body)
        out << progressStack(attrs, { out << item })
    }

    def progressStack = { attrs, body ->
        out << render([plugin: "twbs3", template: "/twbs/progressbar/stack", model: [attrs: attrs]], body)
    }

    def progressItem = { attrs, body ->
        // Values
        Long minValue = attrs.remove("min") as Long ?: 0
        Long maxValue = attrs.remove("max") as Long ?: 100
        Double value = attrs.remove("value") as Double ?: fail(Double, "value", "twbs:progressBar")

        // Styling
        ContextualColor color = attrs.remove("context") as ContextualColor ?: ContextualColor.DEFAULT
        boolean showLabel = optionalBoolean(attrs.remove("showLabel"))
        boolean animated = optionalBoolean(attrs.remove("animated"))
        boolean striped = optionalBoolean(attrs.remove("striped"))
        String minWidth = attrs.remove("minWidth") ?: "2em"
        String numberFormat = attrs.remove("numberFormat") ?: "#.##"

        // Extra DOM attributes
        String clazz = attrs.remove("class")
        String id = attrs.remove("id")

        // Prepare model
        long range = maxValue - minValue
        double percentage = ((value - minValue) / range) * 100
        DecimalFormat format = new DecimalFormat(numberFormat)
        String label = "${format.format(percentage)}%"
        long width = Math.ceil(percentage)
        if (width < 0) width = 0

        List<String> classes = []
        if (striped || animated) {
            classes += "progress-bar-striped"
        }
        if (animated) {
            classes += "active"
        }
        if (color != ContextualColor.DEFAULT) {
            classes += "progress-bar-$color.baseName"
        }

        String style = "width: ${width}%;"
        if (showLabel) {
            style += "min-width: $minWidth;"
        }

        Map model = [showLabel: showLabel, clazz: clazz, label: label, barClasses: classes.join(" "),
                     minValue: minValue, maxValue: maxValue, value: value, style: style, id: id]
        out << render([plugin: "twbs3", template: "/twbs/progressbar/item", model: model], body)
    }

}
