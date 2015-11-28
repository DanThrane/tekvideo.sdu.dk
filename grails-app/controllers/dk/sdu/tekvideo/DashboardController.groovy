package dk.sdu.tekvideo

import org.springframework.security.access.annotation.Secured

@Secured("ROLE_TEACHER")
class DashboardController {
    DashboardService dashboardService
    CourseManagementService courseManagementService

    def index() {
        [courses: courseManagementService.activeCourses.result]
    }

}
