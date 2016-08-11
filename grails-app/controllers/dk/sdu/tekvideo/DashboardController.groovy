package dk.sdu.tekvideo

import grails.converters.JSON
import org.springframework.security.access.annotation.Secured

@Secured("ROLE_TEACHER")
class DashboardController {
    DashboardService dashboardService
    CourseManagementService courseManagementService

    def index() {
        [courses: courseManagementService.activeCourses.result]
    }

    def visits(String identifier, Long periodFrom, Long periodTo, Boolean cumulative) {
        def node = dashboardService.nodeFromIdentifier(identifier)
        if (node.success) {
            def leaves = dashboardService.findLeaves(node.result)
            if (leaves.success) {
                def stats = dashboardService.findViewingStatistics(leaves.result, periodFrom, periodTo, cumulative)
                if (stats.success) {
                    render stats.result as JSON
                } else {
                    response.status = stats.suggestedHttpStatus
                    render stats as JSON
                }
            } else {
                render leaves as JSON
            }
        } else {
            render node as JSON
        }
    }

    def popularVideos(String identifier, Long periodFrom, Long periodTo) {
        def node = dashboardService.nodeFromIdentifier(identifier)
        if (node.success) {
            def leaves = dashboardService.findLeaves(node.result)
            if (leaves.success) {
                def stats = dashboardService.findPopularVideos(leaves.result, periodFrom, periodTo)
                if (stats.success) {
                    render stats.result as JSON
                } else {
                    response.status = stats.suggestedHttpStatus
                    render stats as JSON
                }
            } else {
                render leaves as JSON
            }
        } else {
            render node as JSON
        }
    }

    def comments(String identifier, Long periodFrom, Long periodTo) {
        def node = dashboardService.nodeFromIdentifier(identifier)
        if (node.success) {
            def leaves = dashboardService.findLeaves(node.result)
            if (leaves.success) {
                def stats = dashboardService.findRecentComments(leaves.result, periodFrom, periodTo)
                if (stats.success) {
                    render stats.result as JSON
                } else {
                    response.status = stats.suggestedHttpStatus
                    render stats as JSON
                }
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

    def answers(Video id, Long periodFrom, Long periodTo) {
        def answers = dashboardService.getAnswers(id, periodFrom, periodTo)
        response.status = answers.suggestedHttpStatus
        render answers as JSON
    }

    def studentActivity(String identifier, Long periodFrom, Long periodTo) {
        def node = dashboardService.nodeFromIdentifier(identifier)
        if (node.success) {
            def leaves = dashboardService.findLeaves(node.result)
            if (leaves.success) {
                render dashboardService.findStudentActivity(node.result, leaves.result, periodFrom, periodTo) as JSON
            } else {
                render leaves as JSON
            }
        } else {
            render node as JSON
        }
    }

}
