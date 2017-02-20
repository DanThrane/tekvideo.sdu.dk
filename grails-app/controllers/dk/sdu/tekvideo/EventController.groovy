package dk.sdu.tekvideo

import grails.converters.JSON
import org.springframework.security.access.annotation.Secured

class EventController {
    def eventService

    @Secured("permitAll")
    def register() {
        def json = request.JSON
        def events = eventService.processEvents(json.events)
        response.status = events.suggestedHttpStatus
        render events as JSON
    }

}
