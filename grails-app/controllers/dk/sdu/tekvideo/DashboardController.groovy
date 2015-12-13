package dk.sdu.tekvideo

import dk.sdu.tekvideo.events.AnswerQuestionEvent
import grails.converters.JSON
import org.springframework.security.access.annotation.Secured

@Secured("ROLE_TEACHER")
class DashboardController {
    DashboardService dashboardService
    CourseManagementService courseManagementService

    def index() {
        [courses: courseManagementService.activeCourses.result]
    }

    def visits(String identifier, Integer period) {
        def node = dashboardService.nodeFromIdentifier(identifier)
        if (node.success) {
            def leaves = dashboardService.findLeaves(node.result)
            if (leaves.success) {
                def stats = dashboardService.findViewingStatistics(leaves.result, period)
                render stats as JSON
            } else {
                render leaves as JSON
            }
        } else {
            render node as JSON
        }
    }

    def popularVideos(String identifier, Integer period) {
        def node = dashboardService.nodeFromIdentifier(identifier)
        if (node.success) {
            def leaves = dashboardService.findLeaves(node.result)
            if (leaves.success) {
                def stats = dashboardService.findPopularVideos(leaves.result, period)
                render stats as JSON
            } else {
                render leaves as JSON
            }
        } else {
            render node as JSON
        }
    }

    def comments(String identifier, Integer period) {
        def node = dashboardService.nodeFromIdentifier(identifier)
        if (node.success) {
            def leaves = dashboardService.findLeaves(node.result)
            if (leaves.success) {
                def stats = dashboardService.findRecentComments(leaves.result, period)
                render stats as JSON
            } else {
                render leaves as JSON
            }
        } else {
            render node as JSON
        }
    }

    def videos(String identifier) {
        def node = dashboardService.nodeFromIdentifier(identifier)
        if (node.success) {
            def leaves = dashboardService.findLeaves(node.result)
            if (leaves.success) {
                render leaves.result as JSON
            } else {
                render leaves as JSON
            }
        } else {
            render node as JSON
        }
    }

    def students(String identifier) {
        def node = dashboardService.nodeFromIdentifier(identifier)
        if (node.success) {
            render dashboardService.findStudents(node.result) as JSON
        } else {
            render node as JSON
        }
    }

    def answers(Video id, Long period) {
        def answers = dashboardService.getAnswers(id, period)
        response.status = answers.suggestedHttpStatus
        render answers as JSON
    }

}
