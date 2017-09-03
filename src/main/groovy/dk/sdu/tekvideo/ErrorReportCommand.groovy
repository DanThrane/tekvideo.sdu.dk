package dk.sdu.tekvideo

import grails.validation.Validateable

class ErrorReportCommand implements Validateable {
    String url
    String expected
    String actual
}
