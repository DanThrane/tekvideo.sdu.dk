package dk.sdu.tekvideo.date

import org.springframework.web.servlet.support.RequestContextUtils

import java.time.LocalDateTime

import static dk.sdu.tekvideo.TagLibUtils.getRequiredAttribute

class PikadayTagLib {
    static namespace = "date"

    /**
     * Displays a Pikadate field. It stores the value of the element two places. In a hidden field, which is intended
     * for the actual form submission. The date will be stored in ISO format for the hidden field. Along with a visible
     * field, which should be user friendly.
     *
     * <i>This tag should only be called within a script tag.</i>
     *
     * <b>body:</b>                             This attribute takes no body. <br>
     * </p>
     * <b>hiddenSelector:</b>                   The CSS selector for the hidden field. <br>
     * <b>(optional) selector:</b>              The CSS selector for the visible field. Will default to
     *                                          <code>hiddenSelector + "Display"</code><br>
     * <b>(optional) displayFormat:</b>         The display format used in the visible field. For formats see the
     *                                          documentation of moment.js. This will default to the default of
     *                                          the current locale (see misc.datetimeformat). <br>
     * <b>(optional) value:</b>                 Of type {@link java.time.LocalDateTime}. A date time object, which will
     *                                          be set in the hidden field, and displayed nicely in the display field.
     */
    def initField = { attrs, body ->
        String hiddenSelector = getRequiredAttribute(attrs, "hiddenSelector", "date:initField")
        String selector = (attrs.selector) ?: "${hiddenSelector}Display"
        String format = (attrs.displayFormat) ?: g.message(code: "misc.datetimeformat")
        LocalDateTime value = attrs.value

        out << """initField("$selector", "$hiddenSelector", "$value", "$format");"""
    }

    /**
     * Initializes the date picker service. Must be called before any calls to initField. Also requires that
     * requireField has been called from the head tag.
     */
    def initService = { attrs, body ->
        Locale locale = RequestContextUtils.getLocale(request)
        out << """
        function initField(displaySelector, hiddenSelector, val, displayFormat) {
            var ISO_FORMAT = "YYYY-MM-DDTHH:mm:ss";
            var displayField = \$(displaySelector);
            var hiddenField = \$(hiddenSelector);
            var picker = new Pikaday({
                field: displayField[0],
                onSelect: function() {
                    console.log(this.getMoment());
                    hiddenField.val(this.getMoment().format(ISO_FORMAT));
                },
                showTime: true,
                use24hour: true,
                showSeconds: false,
                format: displayFormat
            });

            var value = moment(val);
            displayField.val(value.format(displayFormat));
            hiddenField.val(value.format(ISO_FORMAT));

            picker.gotoToday();
        }
        moment.locale("${locale.language}");
        """
    }

    /**
     * Appends the dependencies for adding a field. Must be done inside a head tag.
     */
    def requireField = { attrs, body ->
        out << asset.javascript(src: "moment.js")
        out << asset.javascript(src: "pikaday.js")
        out << asset.stylesheet(src: "pikaday.css")
    }
}
