package dk.sdu.tekvideo

import dk.sdu.tekvideo.events.*
import grails.transaction.Transactional

@Transactional
class EventService {
    def springSecurityService

    final Map<String, Class<? extends Event>> KIND_TO_CLASS = [
            ANSWER_QUESTION          : AnswerQuestionEvent,
            EXIT_SITE                : ExitSiteEvent,
            PAUSE_VIDEO              : PauseVideoEvent,
            SKIP_TO_CONTENT          : SkipToContentEvent,
            VISIT_SITE               : VisitSiteEvent,
            VISIT_VIDEO              : VisitVideoEvent,
            VISIT_WRITTEN_EXERCISE   : VisitWrittenExercise,
            COMPLETE_WRITTEN_EXERCISE: CompletedWrittenExercise
    ]

    final Map<String, Closure> CUSTOM_MAPPER = [
            VISIT_VIDEO    : {
                def video = it.video
                if (video) {
                    VisitVideoEvent event = new VisitVideoEvent(videoId: video)
                    return event
                }
                return null
            },
            ANSWER_QUESTION: {
                def video = it.video

                if (video) {
                    return new AnswerQuestionEvent(
                            videoId: video,
                            answer: it.answer,
                            correct: it.correct,
                            subject: it.subject,
                            question: it.question,
                            field: it.field
                    )
                }
                return null
            }
    ]

    void saveJSONEvents(events) {
        events.each {
            Event event = parseEvent(it)
            User user = (User) springSecurityService.currentUser
            event.user = user
            event.timestamp = System.currentTimeMillis()
            event.save()
        }
    }

    Event parseEvent(event) {
        Class<? extends Event> clazz = KIND_TO_CLASS[event.kind]
        if (clazz == null) {
            throw new IllegalArgumentException("Unknown event kind")
        }
        Closure mapper = CUSTOM_MAPPER[event.kind]
        return (mapper != null) ? mapper(event) : clazz.newInstance(event)
    }
}
