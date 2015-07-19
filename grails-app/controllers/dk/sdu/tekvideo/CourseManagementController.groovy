package dk.sdu.tekvideo

import grails.converters.JSON
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

    def createVideo(Course course) {
        if (teacherService.canAccess(course)) {
            [course: course, subjects: course.subjects]
        } else {
            notAllowedCourse()
        }
    }

    def postVideo(CreateVideoCommand command) {
        def result = teacherService.createVideo(command)
        response.status = result.suggestedHttpStatus
        render result as JSON
    }

    def createCourse() {
        render view: "createOrEditCourse", model: [isEditing: false]
    }

    def editCourse(Course course) {
        if (teacherService.canAccess(course)) {
            render view: "createOrEditCourse", model: [command: new CourseCRUDCommand(course: course), isEditing: true]
        } else {
            notAllowedCourse()
        }
    }

    def postCourse(CourseCRUDCommand command) {
        def course = teacherService.createOrEditCourse(command)
        if (course.success) {
            flash.success = "Ændringer til '$command.course.name' blev succesfuldt registeret!"
            redirect action: "index"
        } else {
            render view: "createOrEditCourse", model: [command: command]
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
