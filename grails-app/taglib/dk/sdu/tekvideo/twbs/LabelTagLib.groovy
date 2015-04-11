package dk.sdu.tekvideo.twbs

import static dk.sdu.tekvideo.TagLibUtils.getRequiredAttribute

class LabelTagLib {
    static namespace = "twbs"

    def label = { attrs, body ->
        LabelType type = getRequiredAttribute(attrs, "type", "twbs:label")
        out << """
        <span class="label ${computeLabelClass(type)}">${body()}<span>
        """
    }

    private String computeLabelClass(LabelType type) {
        return (type == LabelType.DEFAULT) ? "" : "label-${type.clazz}"
    }
}
