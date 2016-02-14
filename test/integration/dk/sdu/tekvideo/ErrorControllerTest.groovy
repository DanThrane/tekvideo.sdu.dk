package dk.sdu.tekvideo

import grails.plugin.mail.MailService
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(ErrorController)
class ErrorControllerTest extends Specification {
    def "test error reporting"() {
        given: "a mocked mail service"
        def mailService = Mock(MailService)
        controller.mailService = mailService

        when:
        controller.index()

        then:
        1 * mailService.sendMail(_)
    }
}
