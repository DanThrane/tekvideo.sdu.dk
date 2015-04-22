package dk.sdu.tekvideo

import static dk.sdu.tekvideo.TagLibUtils.fail

class MousePanelTagLib {
    static namespace = "sdu"

    def mousePanel = { attrs, body ->
        String id = attrs.id ?: fail("id", "sdu:mousePanel")
        String clazz = attrs.class ?: ""
        String title = attrs.title

        out << """<div class="panel panel-default mousePanel $clazz" id="$id">"""
        if (title) {
            out << """<div class="panel-heading"> <h3 class="panel-title">$title</h3></div>"""
        }
        out << """<div class="panel-body">${body()}</div>"""
    }

    def requireMousePanel = { attrs, body ->
        out << """function createMousePanel(triggerId, panelId) {
    var \$triggerId = \$(triggerId);
    var \$panelId = \$(panelId);

    \$triggerId.mouseenter(function (e) {
        \$panelId.show().css({left: \$triggerId.pageX, top: \$triggerId.pageY});
    });
    \$triggerId.mouseleave(function () {
        \$panelId.hide();
    });
}"""
    }

    def initMousePanel = { attrs, body ->
        String triggerId = attrs.triggerId ?: fail("triggerId", "sdu:initMousePanel")
        String panelId = attrs.panelId ?: fail("panelId", "sdu:initMousePanel")

        out << """createMousePanel("#$triggerId", "#$panelId");"""
    }
}
