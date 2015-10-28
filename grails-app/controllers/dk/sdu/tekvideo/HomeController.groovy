package dk.sdu.tekvideo

import org.springframework.security.access.annotation.Secured

@Secured("permitAll")
class HomeController {
    def studentService
    def videoService

    def index() {
        def featuredVideos = videoService.findFeaturedVideos()
        def student = studentService.authenticatedStudent
        Set<Course> courses = student ? studentService.getAllCourses(student) : []
        def videoBreakdown = videoService.findVideoBreakdown(featuredVideos)
        if (videoBreakdown.success) {
            render view: "/index", model: [featuredVideos: videoBreakdown.result, student: student, courses: courses]
        } else {
            render text: "Error", status: 500
        }
    }
}
