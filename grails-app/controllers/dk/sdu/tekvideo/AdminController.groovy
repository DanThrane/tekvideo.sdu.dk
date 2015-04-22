package dk.sdu.tekvideo

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import grails.validation.Validateable

@Secured("ROLE_TEACHER")
class AdminController {

    AdminService adminService

    def index() { }

    def videoStatistics(Video video) {
        // TODO Confirm that the teacher owns this video
        def statistics = adminService.findViewingStatistics(video, System.currentTimeMillis() - (1000 * 60 * 60 * 24),
                System.currentTimeMillis() + (1000 * 60 * 60), 1000 * 60 * 60)
        [video: video, statistics: statistics]
    }

    def videoSummary(VideoSummaryQueryCommand command) {
        withFormat {
            html {
                Teacher teacher = adminService.findCurrentTeacher()
                List<Course> courses = Course.findAllByTeacher(teacher)
                List<Subject> subjects = Subject.findAllByCourseInList(courses)
                return [summary: adminService.findSummaryData(command, teacher, courses, subjects), courses: courses,
                 subjects: subjects]
            }
            json {
                def summaryData = adminService.findSummaryData(command)

                render([html: g.render(template: "summaryRows", model: [summary: summaryData]),
                        ids: summaryData.keySet().id] as JSON)
            }
        }
    }
}

@Validateable
class VideoSummaryQueryCommand {
    Subject subject
    Course course

    @Override
    String toString() {
        "subject=$subject, course=$course"
    }
}