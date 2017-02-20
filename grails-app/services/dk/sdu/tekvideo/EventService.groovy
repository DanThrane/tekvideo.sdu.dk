package dk.sdu.tekvideo

import dk.sdu.tekvideo.stats.EventHandler
import grails.transaction.Transactional
import org.apache.http.HttpStatus

@Transactional
class EventService {
    def springSecurityService
    private List<EventHandler> handlers = []

    // Put handlers in Bootstrap.groovy
    void addHandler(EventHandler handler) {
        handlers.add(handler)
    }

    ServiceResult<Void> processEvents(events) {
        for (def it : events) {
            Event event = parseEvent(it)
            if (event != null) {
                def handler = handlers.find { it.canHandle(event.kind) }
                if (handler != null) {
                    def result = handler.handle(event)
                    if (!result.success) {
                        return result
                    }
                }
            } else {
                return ServiceResult.fail(message: "Bad request", suggestedHttpStatus: HttpStatus.SC_BAD_REQUEST)
            }
        }
        return ServiceResult.ok()
    }

    Event parseEvent(event) {
        if (!event.kind || !(event.kind instanceof String)) {
            return null
        }

        if (!event.uuid || !(event.uuid instanceof String)) {
            return null
        }

        User user = (User) springSecurityService.currentUser

        def result = new Event()
        result.kind = event.kind
        result.timestamp = System.currentTimeMillis()
        result.user = user
        result.uuid = event.uuid
        result.additionalData = event
        return result
    }

}
