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
        def teacher = teacherService.authenticatedTeacher
        if (teacherService.canAccess(course)) {
            render view: "createOrEditSubject", model: [course: course, teacher: teacher, isEditing: false]
        } else {
            notAllowedCourse()
        }
    }

    def editSubject(Subject subject) {
        def teacher = teacherService.authenticatedTeacher
        if (teacherService.canAccess(subject.course)) {
            render view: "createOrEditSubject",
                    model: [course: subject.course, command: new SubjectCRUDCommand(domain: subject),
                            isEditing: true, teacher: teacher]
        } else {
            notAllowedCourse()
        }
    }

    def postSubject(Course course, SubjectCRUDCommand command) {
        if (teacherService.canAccess(course)) {
            def subject = teacherService.createOrEditSubject(course, command)
            if (subject.success) {
                flash.success = "Ændringer til '$command.domain.name' blev succesfuldt registeret!"
                redirect action: "manage", id: course.id
            } else {
                flash.error = subject.message
                render view: "createOrEditSubject", model: [course: course, command: command]
            }
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
            render view: "createOrEditCourse", model: [command: new CourseCRUDCommand(domain: course), isEditing: true]
        } else {
            notAllowedCourse()
        }
    }

    def postCourse(CourseCRUDCommand command) {
        def course = teacherService.createOrEditCourse(command)
        if (course.success) {
            flash.success = "Ændringer til '$command.domain.name' blev succesfuldt registeret!"
            redirect action: "index"
        } else {
            render view: "createOrEditCourse", model: [command: command]
        }
    }

    def updateVideos(UpdateVideosCommand command) {
        def result = teacherService.updateVideos(command)
        response.status = result.suggestedHttpStatus
        render result as JSON
    }

    private void notAllowedCourse() {
        flash.error = "Du har ikke tiladelse til at tilgå dette kursus"
        redirect action: "index"
    }

}
