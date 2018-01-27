package dk.sdu.tekvideo

import static dk.sdu.tekvideo.TagLibUtils.fail

/**
 * @author Dan Thrane
 */
class SemesterTagLib {
    static namespace = "sdu"

    def semesterString = { attrs, body ->
        Course course = attrs.remove("course")
        Integer year = attrs.remove("year")?.toString()?.toInteger()
        Boolean spring = attrs.remove("spring")?.toString()?.equalsIgnoreCase("true")
        if (course != null) {
            year = course.year
            spring = course.spring
        }

        if (year == null || spring == null) fail("course", "sdu:semesterString")

        out << ((spring) ? "Forår" : "Efterår")
        out << " "
        out << year
    }
}
