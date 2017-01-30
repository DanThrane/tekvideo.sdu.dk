package dk.sdu.tekvideo

import org.springframework.security.access.annotation.Secured

@Secured(["ROLE_TEACHER"])
class StatsController {
    def statsService

    def index()  {
        def browser = statsService.coursesForBrowser()
        if (renderError(browser)) return
        println(browser)
        [items: browser.result]
    }

    private boolean renderError(ServiceResult s) {
        if (!s.success) {
            render status: s.suggestedHttpStatus, text: s.message
            return true
        }
        return false
    }
}
