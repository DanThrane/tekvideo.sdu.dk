package dk.sdu.tekvideo

import grails.util.Environment
import org.springframework.security.access.annotation.Secured

import java.time.LocalDateTime

@Secured("permitAll")
class ErrorController {
    def mailService
    def springSecurityService

    def index() {
        def exception = request.exception?.cause

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        if (exception != null) {
            exception.printStackTrace(pw);
        }

        def subjectText = "[SDU-TEKVIDEO] Internal Server Error ${request.forwardURI}"
        def bodyText = """Request URL: ${request.forwardURI}
Time: ${LocalDateTime.now().toString()}
Query string: ${request.queryString}
Cookies: ${request.cookies.collect { "[${it.name}, ${it.value}, ${it.domain}]" }.join(",")}
Method: ${request.method}
Parameters: ${request.parameterMap}
User: ${springSecurityService?.currentUser?.username}
Exception: ${sw.toString()}"""

        if (Environment.current == Environment.DEVELOPMENT) {
            return [subject: subjectText, body: bodyText]
        } else {
            return [:]
        }
    }

}
