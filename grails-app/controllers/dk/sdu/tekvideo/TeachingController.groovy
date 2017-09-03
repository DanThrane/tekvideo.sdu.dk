package dk.sdu.tekvideo

import org.springframework.security.access.annotation.Secured

class TeachingController {

    @Secured("permitAll")
    def index(String teacher, String course, String subject, Integer vidid, Integer year, Boolean fall, String format) {
        boolean spring = !fall
        println(format)
        // Do some logic, and forward the user to the correct controller etc
        if (course == null) {
            if (format != null) {
                render status: 404, text: "Not found"
                return
            }

            forward controller: "teacher", action: "list", params:
                    [teacherName: teacher]
        } else if (subject == null) {
            forward controller: "course", action: "viewByTeacher", params:
                    [teacherName: teacher, courseName: course, year: year, spring: spring, format: format]
        } else if (vidid == null) {
            forward controller: "subject", action: "viewByTeacherAndCourse", params:
                    [teacherName: teacher, courseName: course, subjectName: subject, year: year, spring: spring,
                     format: format]
        } else {
            if (format != null) {
                render status: 404, text: "Not found"
                return
            }

            forward controller: "exercise", action: "view", params:
                    [teacherName: teacher, courseName: course, subjectName: subject, videoId: vidid, year: year,
                     spring: spring]
        }
    }

}
