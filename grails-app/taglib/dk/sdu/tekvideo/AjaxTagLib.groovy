package dk.sdu.tekvideo

import static dk.danthrane.TagLibUtils.assistAutoComplete

/**
 * @author Dan Thrane
 */
class AjaxTagLib {
    static namespace = "sdu"

    def requireAjaxAssets = { attrs, body ->
        out << asset.javascript(src: "ajax_util.js")
    }

    def ajaxSubmitButton = { attrs, body ->
        // Assist on the attributes provided by the actual button
        assistAutoComplete(attrs.size, attrs.style, attrs.class, attrs.block, attrs.active, attrs.disabled)
        out << twbs.button(attrs, {
            out << render([template: "/ajax/submit", model: attrs], body)
        })
    }
}
