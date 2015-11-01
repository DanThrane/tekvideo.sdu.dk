package dk.sdu.tekvideo

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

@Secured("ROLE_TEACHER")
class VideoHostController {
    def externalVideoHostService

    def info(String id, Boolean type) {
        def result = externalVideoHostService.getVideoInformation(id, type)
        response.status = result.suggestedHttpStatus
        render result as JSON
    }
}
