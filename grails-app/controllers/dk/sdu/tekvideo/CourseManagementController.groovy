package dk.sdu.tekvideo

import grails.converters.JSON
import org.springframework.security.access.annotation.Secured

/**
 * @author Dan Thrane
 */
@Secured(["ROLE_TEACHER"])
class CourseManagementController {
    UserService userService
    CourseService courseService
    CourseManagementService courseManagementService
    VideoService videoService

    static allowedMethods = [postCourse         : "POST", postSimilarResource: "POST", postSubject: "POST",
                             postVideo          : "POST", postWrittenExercise: "POST", copyExerciseToSubject: "POST",
                             copySubjectToCourse: "POST", postImportedWrittenExercises: "POST"]

    def index() {
        String statusStr = params.status ?: "VISIBLE"
        def status = NodeStatus.fromValue(statusStr.toUpperCase()) ?: NodeStatus.VISIBLE
        def courses = courseManagementService.getCompleteCourseSummary()

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

    def jstExercises(Subject subject) {
        render courseManagementService.getJsTreeExercises(subject) as JSON
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

    def exerciseStatus(ExerciseStatusChangeCommand command) {
        def current = command.id?.localStatus
        def statusS = NodeStatus.fromValue(command.status?.toUpperCase()) ?: null
        if (statusS == null) {
            flash.error = "Ugyldig status"
            redirect(action: "index")
        } else {
            def res = courseManagementService.changeExerciseStatus(command.id, statusS)
            if (res.success) {
                redirect(action: "manageSubject", params: [status: current, id: command.id.subject.id])
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
        def exercises = courseManagementService.getExercises(status, subject)

        if (courseManagementService.canAccess(subject.course) && exercises.success) {
            def meta = exercises.result.collect {
                if (it instanceof Video) {
                    videoService.getVideoMetaDataSafe(it)
                } else {
                    null
                }
            }

            [subject: subject, excises: exercises.result, meta: meta, status: status]
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

    def createWrittenExercise(Subject subject) {
        if (courseManagementService.canAccessNode(subject)) {
            [isEditing: false, subject: subject]
        } else {
            notAllowedCourse()
        }
    }

    def editWrittenExercise(WrittenExerciseGroup exercise) {
        if (courseManagementService.canAccessNode(exercise)) {
            render view: "createWrittenExercise", model: [
                    isEditing: true,
                    exercise : exercise,
                    subject  : exercise.subject
            ]
        } else {
            notAllowedCourse()
        }
    }

    def postWrittenExercise(CreateExerciseCommand command) {
        def result = courseManagementService.createOrEditWrittenExercise(command)
        response.status = result.suggestedHttpStatus
        render result as JSON
    }

    def importWrittenExercises(Subject subject) {
        if (courseManagementService.canAccessNode(subject)) {
            [subject: subject]
        } else {
            notAllowedCourse()
        }
    }

    def postImportedWrittenExercises(Subject subject, String json) {
        if (courseManagementService.canAccessNode(subject)) {
            def result = courseManagementService.importWrittenExercisesFromJson(subject, json)
            if (result.success) flash.success = result.message
            else flash.error = result.message

            redirect controller: "courseManagement", action: "manageSubject", id: subject.id
        } else {
            notAllowedCourse()
        }
    }

    def editVideo(Video video) {
        if (courseManagementService.canAccessNode(video)) {
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

    def editLinks(Long id) {
        def exercise = Exercise.get(id)
        if (exercise && courseManagementService.canAccessNode(exercise)) {
            [exercise: exercise]
        } else {
            notAllowedCourse()
        }
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

    def updateExercises(UpdateExercisesCommand command) {
        def result = courseManagementService.updateExercises(command)
        response.status = result.suggestedHttpStatus
        render result as JSON
    }

    def updateSubjects(UpdateSubjectsCommand command) {
        def result = courseManagementService.updateSubjects(command)
        response.status = result.suggestedHttpStatus
        render result as JSON
    }

    def moveExercise(MoveExerciseCommand command) {
        def result = courseManagementService.moveExercise(command)
        response.status = result.suggestedHttpStatus
        render result as JSON
    }

    def moveSubject(MoveSubjectCommand command) {
        def result = courseManagementService.moveSubject(command)
        response.status = result.suggestedHttpStatus
        render result as JSON
    }

    def copySubjectToCourse(CopyNodeCommand command) {
        def subject = Subject.get(command.element)
        def course = Course.get(command.destination)
        def result = courseManagementService.copySubjectToCourse(subject, course)
        response.status = result.suggestedHttpStatus
        render result as JSON
    }

    def copyExerciseToSubject(CopyNodeCommand command) {
        def exercise = Exercise.get(command.element)
        def subject = Subject.get(command.destination)
        def result = courseManagementService.copyExerciseToSubject(exercise, subject)
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


    def postSimilarResource(CreateSimilarResourceCommand command) {
        def result = courseManagementService.createSimilarResource(command)
        if (result.success) {
            flash.success = "Link gemt"
        } else {
            flash.error = result.message
        }
        redirect action: "editLinks", id: command.id
    }

    def deleteSimilarResource(Long id, Long link) {
        def resource = SimilarResources.get(link)
        def result = courseManagementService.deleteSimilarResource(id, resource)
        if (result.success) {
            flash.success = "Link slettet"
        } else {
            flash.error = result.message
        }
        redirect action: "editLinks", id: id
    }

    private void notAllowedCourse() {
        flash.error = "Du har ikke tiladelse til at tilgå dette kursus"
        redirect action: "index"
    }

}
