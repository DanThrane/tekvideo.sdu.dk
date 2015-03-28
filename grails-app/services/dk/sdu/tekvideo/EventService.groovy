package dk.sdu.tekvideo

import grails.transaction.Transactional

@Transactional
class EventService {
    final static Map<String, Class<? extends Event>> KIND_TO_CLASS = [
            "TEST_EVENT": TestEvent
    ]

    void saveJSONEvents(events) {
        events.each {
            Event event = parseEvent(it)
            // Add user
            event.save(flush: true)
            println event
        }
    }

    Event parseEvent(event) {
        Class<? extends Event> clazz = KIND_TO_CLASS[event.kind]
        if (clazz == null) {
            throw new IllegalArgumentException("Unknown event kind")
        }
        return clazz.newInstance(event)
    }
}
