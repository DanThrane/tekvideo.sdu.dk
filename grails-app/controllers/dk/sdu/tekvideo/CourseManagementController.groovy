package dk.sdu.tekvideo

import grails.converters.JSON
import org.springframework.security.access.annotation.Secured

/**
 * @author Dan Thrane
 */
@Secured("ROLE_TEACHER")
class CourseManagementController {
    CourseService courseService
    CourseManagementService courseManagementService
    VideoStatisticsService videoStatisticsService
    TeachingService teachingService

    def index() {
        def courses = courseManagementService.activeCourses
        if (courses.success) {
            [activeCourses: courses.result]
        } else {
            render status: courses.suggestedHttpStatus, text: courses.message
        }
    }

    def dashboard() {

    }

    def manage(Course course) {
        if (courseManagementService.canAccess(course)) {
            [course: course]
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
        def teacher = teachingService.authenticatedTeacher
        if (courseManagementService.canAccess(course)) {
            render view: "createOrEditSubject", model: [course: course, teacher: teacher, isEditing: false]
        } else {
            notAllowedCourse()
        }
    }

    def editSubject(Subject subject) {
        def teacher = teachingService.authenticatedTeacher
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
            [course: course, subjects: course.subjects, isEditing: false]
        } else {
            notAllowedCourse()
        }
    }

    def editVideo(Video video) {
        // TODO A bit unclear who should be allowed to edit a video (See issue #14)
        if (teachingService.authenticatedTeacher) {
            render view: "createVideo", model: [isEditing: true, video: video, subjects: video.subject.course.subjects]
        } else {
            notAllowedCourse()
        }
    }

    def postVideo(CreateVideoCommand command) {
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

    def videoStatistics(Video video) {
        if (video == null) {
            flash.error = "Ukendt video"
            redirect action: "index"
        } else {
            if (courseManagementService.canAccess(video.subject.course)) {
                def statistic = videoStatisticsService.findViewingStatistics(
                        video,
                        System.currentTimeMillis() - 1000 * 60 * 60 * 24,
                        System.currentTimeMillis(), 1000 * 60 * 60
                ).result

                def viewBreakdown = videoStatisticsService.retrieveViewBreakdown(video).result
                def answerSummary = videoStatisticsService.retrieveAnswerSummary(video).result
                def viewsAmongStudents = videoStatisticsService.retrieveViewingStatisticsForStudents(video).result

                return [
                        video             : video,
                        statistics        : statistic,
                        viewBreakdown     : viewBreakdown,
                        answerSummary     : answerSummary,
                        viewsAmongStudents: viewsAmongStudents
                ]
            } else {
                notAllowedCourse()
            }
        }
    }

    def videoViewingChart(Video video, Long period) {
        // TODO Confirm that the teacher owns this video
        long from = System.currentTimeMillis()
        long to = System.currentTimeMillis()
        long periodInMs

        from -= 1000 * 60 * 60 * 24 * period
        periodInMs = (to - from) / 24

        def statistics = videoStatisticsService.findViewingStatistics(video, from, to, periodInMs)
        render statistics as JSON
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
