package dk.sdu.tekvideo

import dk.sdu.tekvideo.events.*
import grails.transaction.Transactional

@Transactional
class EventService {
    def springSecurityService

    final static Map<String, Class<? extends Event>> KIND_TO_CLASS = [
            ANSWER_QUESTION: AnswerQuestionEvent,
            EXIT_SITE: ExitSiteEvent,
            PAUSE_VIDEO: PauseVideoEvent,
            SKIP_TO_CONTENT: SkipToContentEvent,
            VISIT_SITE: VisitSiteEvent
    ]

    void saveJSONEvents(events) {
        events.each {
            Event event = parseEvent(it)
            def user = springSecurityService.currentUser
            event.user = user as User
            event.save()
            log.info event
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
