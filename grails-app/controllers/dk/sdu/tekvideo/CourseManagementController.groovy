package dk.sdu.tekvideo

import grails.converters.JSON
import org.springframework.security.access.annotation.Secured

/**
 * @author Dan Thrane
 */
@Secured("ROLE_TEACHER")
class CourseManagementController {
    UserService userService
    CourseService courseService
    CourseManagementService courseManagementService

    def index() {
        String statusStr = params.status ?: "VISIBLE"
        def status = NodeStatus.fromValue(statusStr.toUpperCase()) ?: NodeStatus.VISIBLE
        def courses = courseManagementService.getCourses(status)

        if (courses.success) {
            [activeCourses: courses.result, status: status]
        } else {
            render status: courses.suggestedHttpStatus, text: courses.message
        }
    }

    def jstCourses() {
        render courseManagementService.getJsTreeCourses() as JSON
    }

    def jstSubjects(Course course) {
        render courseManagementService.getJsTreeSubjects(course) as JSON
    }

    def jstVideos(Subject subject) {
        render courseManagementService.getJsTreeVideos(subject) as JSON
    }

    // TODO: This should really be refactored
    def courseStatus(Course course, String status) {
        def current = course?.localStatus
        def statusS = NodeStatus.fromValue(status?.toUpperCase()) ?: null
        if (statusS == null) {
            flash.error = "Ugyldig status"
            redirect(action: "index")
        } else {
            def res = courseManagementService.changeCourseStatus(course, statusS)
            if (res.success) {
                redirect(action: "index", params: [status: current])
            } else {
                flash.error = res.message
                redirect(action: "index")
            }
        }
    }

    def subjectStatus(Subject subject, String status) {
        def current = subject?.localStatus
        def statusS = NodeStatus.fromValue(status?.toUpperCase()) ?: null
        if (statusS == null) {
            flash.error = "Ugyldig status"
            redirect(action: "index")
        } else {
            def res = courseManagementService.changeSubjectStatus(subject, statusS)
            if (res.success) {
                redirect(action: "manage", params: [status: current, id: subject.course.id])
            } else {
                flash.error = res.message
                redirect(action: "index")
            }
        }
    }

    def videoStatus(Video video, String status) {
        def current = video?.localStatus
        def statusS = NodeStatus.fromValue(status?.toUpperCase()) ?: null
        if (statusS == null) {
            flash.error = "Ugyldig status"
            redirect(action: "index")
        } else {
            def res = courseManagementService.changeVideoStatus(video, statusS)
            if (res.success) {
                redirect(action: "manageSubject", params: [status: current, id: video.subject.id])
            } else {
                flash.error = res.message
                redirect(action: "index")
            }
        }
    }

    def manage(Course course) {
        String statusStr = params.status ?: "VISIBLE"
        def status = NodeStatus.fromValue(statusStr.toUpperCase()) ?: NodeStatus.VISIBLE
        def subjects = courseManagementService.getSubjects(status, course)

        if (courseManagementService.canAccess(course) && subjects.success) {
            [course: course, subjects: subjects.result, status: status]
        } else {
            notAllowedCourse()
        }
    }

    def manageSubject(Subject subject) {
        String statusStr = params.status ?: "VISIBLE"
        def status = NodeStatus.fromValue(statusStr.toUpperCase()) ?: NodeStatus.VISIBLE
        def videos = courseManagementService.getVideos(status, subject)

        if (courseManagementService.canAccess(subject.course) && videos.success) {
            [subject: subject, videos: videos.result, status: status]
        } else {
            notAllowedCourse()
        }
    }

    def getVideos(Subject subject) {
        if (courseManagementService.canAccess(subject.course)) {
            render template: "videos", model: [videos: subject.videos]
        } else {
            notAllowedCourse()
        }
    }

    def createSubject(Course course) {
        def teacher = userService.authenticatedTeacher
        if (courseManagementService.canAccess(course)) {
            render view: "createOrEditSubject", model: [course: course, teacher: teacher, isEditing: false]
        } else {
            notAllowedCourse()
        }
    }

    def editSubject(Subject subject) {
        def teacher = userService.authenticatedTeacher
        if (courseManagementService.canAccess(subject.course)) {
            render view: "createOrEditSubject",
                    model: [course   : subject.course, command: new SubjectCRUDCommand(domain: subject),
                            isEditing: true, teacher: teacher]
        } else {
            notAllowedCourse()
        }
    }

    def postSubject(Course course, SubjectCRUDCommand command) {
        if (courseManagementService.canAccess(course)) {
            def subject = courseManagementService.createOrEditSubject(course, command)
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
        if (courseManagementService.canAccess(course)) {
            [course: course, subjects: course.subjects, isEditing: false, subject: params.subject]
        } else {
            notAllowedCourse()
        }
    }

    def editVideo(Video video) {
        if (userService.authenticatedTeacher) {
            render view: "createVideo", model: [
                    isEditing: true,
                    video    : video,
                    subjects : video.subject.course.subjects,
                    course   : video.subject.course,
                    subject  : video.subject
            ]
        } else {
            notAllowedCourse()
        }
    }

    def postVideo(CreateOrUpdateVideoCommand command) {
        def result = courseManagementService.createOrEditVideo(command)
        response.status = result.suggestedHttpStatus
        render result as JSON
    }

    def createCourse() {
        render view: "createOrEditCourse", model: [isEditing: false]
    }

    def editCourse(Course course) {
        if (courseManagementService.canAccess(course)) {
            render view: "createOrEditCourse", model: [
                    command  : new CourseCRUDCommand(
                            domain: course,
                            visible: course.localStatus == NodeStatus.VISIBLE
                    ),
                    isEditing: true
            ]
        } else {
            notAllowedCourse()
        }
    }

    def postCourse(CourseCRUDCommand command) {
        def course = courseManagementService.createOrEditCourse(command)
        if (course.success) {
            flash.success = "Ændringer til '$command.domain.name' blev succesfuldt registeret!"
            redirect action: "index"
        } else {
            render view: "createOrEditCourse", model: [command: command]
        }
    }

    def updateVideos(UpdateVideosCommand command) {
        def result = courseManagementService.updateVideos(command)
        response.status = result.suggestedHttpStatus
        render result as JSON
    }

    def updateSubjects(UpdateSubjectsCommand command) {
        def result = courseManagementService.updateSubjects(command)
        response.status = result.suggestedHttpStatus
        render result as JSON
    }

    def moveVideo(MoveVideoCommand command) {
        def result = courseManagementService.moveVideo(command)
        response.status = result.suggestedHttpStatus
        render result as JSON
    }

    def moveSubject(MoveSubjectCommand command) {
        def result = courseManagementService.moveSubject(command)
        response.status = result.suggestedHttpStatus
        render result as JSON
    }

    def importCourse() {
        [courses: courseService.listActiveCourses()]
    }

    def submitImportCourse(ImportCourseCommand command) {
        def result = courseManagementService.importCourse(command)
        if (result.success) {
            flash.success = "Kurset '$command.course.name' er successfuldt blevet importeret til $command.newCourseName"
            redirect action: "manage", id: result.result.id
        } else {
            if (result.suggestedHttpStatus == 400) {
                // The request had errors, return to the form
                render view: "importCourse", model: [courses: courseService.listActiveCourses(), command: command]
            } else {
                response.status = result.suggestedHttpStatus
                render result.message
            }
        }
    }

    def deleteCourse(DeleteCourseCommand command) {
        def result = courseManagementService.deleteCourse(command)
        response.status = result.suggestedHttpStatus
        render result as JSON
    }

    private void notAllowedCourse() {
        flash.error = "Du har ikke tiladelse til at tilgå dette kursus"
        redirect action: "index"
    }

}
