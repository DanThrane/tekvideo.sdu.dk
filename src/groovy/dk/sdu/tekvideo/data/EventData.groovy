package dk.sdu.tekvideo.data

import dk.sdu.tekvideo.User
import dk.sdu.tekvideo.Video
import dk.sdu.tekvideo.events.*

class EventData {
    private static Random random = new Random()
    private static final int MONTH = 1000 * 60 * 60 * 24 * 30

    private static Map buildBasicEvent(Long timestamp = System.currentTimeMillis(), User user = null) {
        [
                timestamp: timestamp,
                user     : user
        ]
    }

    static AnswerQuestionEvent buildAnswerEvent(Video video = VideoData.buildTestVideo(),
                                                Long timestamp = System.currentTimeMillis(),
                                                User user = null, boolean save = true,
                                                String answer = "${random.nextInt(500)}",
                                                Boolean correct = random.nextBoolean(),
                                                Integer subject = random.nextInt(5),
                                                Integer question = random.nextInt(5),
                                                Integer field = random.nextInt(5)) {
        def event = buildBasicEvent(timestamp, user)
        event.video = video
        event.correct = correct
        event.subject = subject
        event.question = question
        event.field = field
        event.videoId = video.id
        event.answer = answer

        def result = new AnswerQuestionEvent(event)
        return (save) ? result.save(failOnError: true, flush: true) : result
    }

    static VisitSiteEvent buildVisitSiteEvent(Long timestamp = System.currentTimeMillis(),
                                              User user = null, boolean save = true,
                                              String url = "http://tekvideo.sdu.dk/t/teach/Course5/2015/1/Subject15/0",
                                              String ua = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 " +
                                                      "(KHTML, like Gecko) Chrome/47.0.2526.73 Safari/537.36") {
        def event = buildBasicEvent(timestamp, user)
        event.url = url
        event.ua = ua
        def result = new VisitSiteEvent(event)
        return (save) ? result.save(failOnError: true, flush: true) : result
    }

    static ExitSiteEvent buildExitSiteEvent(Long timestamp = System.currentTimeMillis(),
                                            User user = null, boolean save = true,
                                            String url = "http://tekvideo.sdu.dk/t/teach/Course5/2015/1/Subject15/0",
                                            String ua = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, " +
                                                    "like Gecko) Chrome/47.0.2526.73 Safari/537.36",
                                            Long time = random.nextInt(1000 * 60 * 60)) {
        def event = buildBasicEvent(timestamp, user)
        event.url = url
        event.ua = ua
        event.time = time
        def result = new ExitSiteEvent(event)
        return (save) ? result.save(failOnError: true, flush: true) : result
    }

    static VisitVideoEvent buildVisitVideoEvent(Video video = VideoData.buildTestVideo(),
                                                Long timestamp = System.currentTimeMillis(),
                                                User user = null, boolean save = true) {
        def event = buildBasicEvent(timestamp, user)
        event.videoId = video.id
        def result = new VisitVideoEvent(event)
        return (save) ? result.save(failOnError: true, flush: true) : result
    }
}
