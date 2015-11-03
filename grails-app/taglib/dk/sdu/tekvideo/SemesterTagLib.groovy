package dk.sdu.tekvideo

import static dk.sdu.tekvideo.TagLibUtils.fail

/**
 * @author Dan Thrane
 */
class SemesterTagLib {
    static namespace = "sdu"

    def semesterString = { attrs, body ->
        Course course = attrs.remove("course") ?: fail("course", "sdu:semesterString")
        out << ((course.spring) ? "Forår" : "Efterår")
        out << " "
        out << course.year
    }
}
