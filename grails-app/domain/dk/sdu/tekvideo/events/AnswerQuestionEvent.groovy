package dk.sdu.tekvideo.events

class AnswerQuestionEvent extends Event {
    String answer
    Boolean correct
    Integer subject
    Integer question
    Integer field

    Long videoId

    static constraints = {
        answer blank: true, maxSize: 512
    }

    static jsonMarshaller = { AnswerQuestionEvent it ->
        [
                answer: it.answer,
                correct: it.correct,
                subject: it.subject,
                question: it.question,
                field: it.field,
                videoId: it.videoId,
                timestamp: it.timestamp,
                user: it.user?.username,
                userId: it.user ? it.user.id : -1
        ]
    }
}
