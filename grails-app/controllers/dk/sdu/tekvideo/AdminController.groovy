package dk.sdu.tekvideo

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import grails.validation.Validateable

import java.util.stream.Collectors

@Secured("ROLE_TEACHER")
class AdminController {

    AdminService adminService

    def index() { }

    def videoStatistics(Video video) {
        // TODO Confirm that the teacher owns this video
        def statistics = adminService.findViewingStatistics(video, System.currentTimeMillis() - (1000 * 60 * 60 * 24),
                System.currentTimeMillis() + (1000 * 60 * 60), 1000 * 60 * 60)
        [video: video, statistics: statistics, subjects: video.subjects.stream().limit(3).collect(Collectors.toList())]
    }

    def videoViewingChart(Video video, Long period) {
        // TODO Confirm that the teacher owns this video
        long from = System.currentTimeMillis()
        long to = System.currentTimeMillis()
        long periodInMs

        from -= 1000 * 60 * 60 * 24 * period
        periodInMs = (to - from) / 24

        def statistics = adminService.findViewingStatistics(video, from, to, periodInMs)
        render statistics as JSON
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