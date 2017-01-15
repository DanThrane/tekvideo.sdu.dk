package dk.sdu.tekvideo

import grails.converters.JSON
import org.springframework.security.access.annotation.Secured

@Secured(["ROLE_TEACHER", "ROLE_STUDENT"])
class ErrorReportingController {
    static allowedMethods = [report: "POST"]
    def errorReportingService

    def report(ErrorReportCommand command) {
        def result = errorReportingService.report(command)
        response.status = result.suggestedHttpStatus
        render result as JSON
    }
}
