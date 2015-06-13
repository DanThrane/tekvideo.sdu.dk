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
            notAllowedCourse()
        }
    }

    def getVideos(Subject subject) {
        if (teacherService.canAccess(subject.course)) {
            render template: "videos", model: [videos: subject.videos]
        } else {
            notAllowedCourse()
        }
    }

    def createSubject(Course course) {
        if (teacherService.canAccess(course)) {
            [course: course]
        } else {
            notAllowedCourse()
        }
    }

    def postSubject(Course course, CreateSubjectCommand command) {
        if (teacherService.canAccess(course)) {
            command?.subject?.course = course
            def subject = teacherService.createSubject(course, command)
            if (subject.success) {
                flash.success = "Emne '$command.subject.name' oprettet!"
                redirect action: "manage", id: course.id
            } else {
                render view: "createSubject", model: [course: course, command: command]
            }
        } else {
            notAllowedCourse()
        }
    }

    private void notAllowedCourse() {
        flash.error = "Du har ikke tiladelse til at tilgå dette kursus"
        redirect action: "index"
    }

}
