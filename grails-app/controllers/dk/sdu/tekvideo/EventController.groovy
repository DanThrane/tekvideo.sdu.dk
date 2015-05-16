package dk.sdu.tekvideo

import org.springframework.security.access.annotation.Secured

class EventController {
    def eventService

    @Secured("permitAll")
    def register() {
        def json = request.JSON
        eventService.saveJSONEvents(json.events)
        render json
    }

}
