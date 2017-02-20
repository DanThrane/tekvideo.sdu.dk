package dk.sdu.tekvideo

import grails.converters.JSON
import org.springframework.security.access.annotation.Secured

class EventController {
    def eventService

    @Secured("permitAll")
    def register() {
        def json = request.JSON
        eventService.processEvents(json.events)
        render([message: "OK"] as JSON)
    }

}
