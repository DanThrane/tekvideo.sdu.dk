package dk.sdu.tekvideo

import org.springframework.security.access.annotation.Secured

@Secured("permitAll")
class HomeController {
//    def studentService

    def index() {
//        def student = studentService.authenticatedStudent
//        Set<Course> courses = student ? studentService.getAllCourses(student) : []
//        render view: "/index", model: [student: student, courses: courses]
        forward controller: "course"
    }
}
