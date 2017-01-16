package dk.sdu.tekvideo

import grails.util.Environment
import org.apache.http.HttpStatus

import static dk.sdu.tekvideo.ServiceResult.*

class ErrorReportingService {
    def mailService
    def springSecurityService
    def grailsApplication

    ServiceResult<Void> report(ErrorReportCommand command) {
        if (!command.validate()) {
            return fail([
                    message            : "Dårlig besked",
                    suggestedHttpStatus: HttpStatus.SC_BAD_REQUEST
            ])
        }

        if (!springSecurityService.currentUser == null) {
            return fail([
                    message            : "Ikke tilladt",
                    suggestedHttpStatus: HttpStatus.SC_UNAUTHORIZED
            ])
        }

        def subjectText = "[SDU-TEKVIDEO] Bug Report"
        def bodyText = """
URL: ${command.url}
User: ${springSecurityService.currentUser.username}

Expected:
${command.expected}

Actual:
${command.actual}
        """.trim()



        if (Environment.current == Environment.DEVELOPMENT) {
            log.info("Subject: ${subjectText}\nBody: ${bodyText}")
        } else {
            mailService.sendMail {
                async true
                to grailsApplication.config.maintainers.split(",")
                subject subjectText
                body bodyText
            }
        }
        return ok()
    }
}
