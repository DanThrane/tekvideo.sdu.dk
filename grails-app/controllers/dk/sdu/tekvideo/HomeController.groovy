package dk.sdu.tekvideo

import org.springframework.security.access.annotation.Secured

@Secured("permitAll")
class HomeController {
    def studentService

    def index() {
        def featuredVideos = Video.listOrderByDateCreated(max: 10)
        def student = studentService.authenticatedStudent
        Set<Course> courses = student ? studentService.getAllCourses(student) : []
        render view: "/index", model: [featuredVideos: featuredVideos, student: student, courses: courses]
    }
}
