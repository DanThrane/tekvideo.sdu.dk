package dk.danthrane.twbs

import static dk.danthrane.TagLibUtils.fail

class LabelTagLib {
    static namespace = "twbs"

    def label = { attrs, body ->
        LabelType type = attrs.type ?: fail(LabelType, "type", "twbs:label")
        out << """
        <span class="label ${computeLabelClass(type)}">${body()}<span>
        """
    }

    private String computeLabelClass(LabelType type) {
        return (type == LabelType.DEFAULT) ? "" : "label-${type.clazz}"
    }
}
