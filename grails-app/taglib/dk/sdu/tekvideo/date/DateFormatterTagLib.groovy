package dk.sdu.tekvideo.date

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

import static dk.sdu.tekvideo.TagLibUtils.fail

class DateFormatterTagLib {
    static namespace = "date"

    def oldDtFormatter = { attrs, body ->
        Date date = attrs.date ?: fail("date", "date:oldDtFormatter")
        String format = attrs.format ?: null
        LocalDateTime dateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
        out << dtformatter(format: format, time: dateTime)
    }

    def dtformatter = { attrs, body ->
        DateTimeFormatter formatter = attrs.format ? DateTimeFormatter.ofPattern(attrs.format) :
                DateTimeFormatter.ISO_LOCAL_DATE_TIME
        LocalDateTime t = attrs.time
        out << formatter.format(t)
    }
}
