package dk.sdu.tekvideo

import org.springframework.security.access.annotation.Secured

/**
 * @author Dan Thrane
 */
@Secured("ROLE_TEACHER")
class CourseManagementController {

    TeacherService teacherService

    def index() {
        def courses = teacherService.activeCourses
        if (courses.success) {
            [activeCourses: courses.result]
        } else {
            render status: courses.suggestedHttpStatus, text: courses.message
        }
    }

    def manage(Course course) {
        if (teacherService.canAccess(course)) {
            [course: course]
        } else {
            render status: 400
        }
    }

    def getVideos(Subject subject) {
        if (teacherService.canAccess(subject.course)) {
            render template: "videos", model: [videos: subject.videos]
        } else {
            render status: 400
        }
    }

}
