package dk.sdu.tekvideo.date

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DateFormatterTagLib {
    static namespace = "date"

    def dtformatter = { attrs, body ->
        DateTimeFormatter formatter = attrs.format ? DateTimeFormatter.ofPattern(attrs.format) :
                DateTimeFormatter.ISO_LOCAL_DATE_TIME
        LocalDateTime t = attrs.time
        out << formatter.format(t)
    }
}
