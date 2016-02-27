package dk.sdu.tekvideo

import static dk.danthrane.TagLibUtils.*

class CourseTagLib {
    static namespace = "sdu"
    def studentService

    def signupButton = { attrs, body ->
        Course course = attrs.remove("course") ?: fail("course", "sdu:signupButton")
        def student = studentService.authenticatedStudent
        if (student) {
            boolean inCourse = studentService.isInCourse(student, course)
            out << render([template: "/course/signupButton", model:
                    [course: course, inCourse: inCourse]
            ], body)
        }
    }
}
