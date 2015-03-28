package dk.sdu.tekvideo

class EventController {
    def eventService

    def register() {
        def json = request.JSON
        eventService.saveJSONEvents(json.events)
        render json
    }

}
