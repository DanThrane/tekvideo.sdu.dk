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

}
