package dk.sdu.tekvideo

import grails.validation.Validateable

@Validateable
class ErrorReportCommand {
    String url
    String expected
    String actual
}
