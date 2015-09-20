package dk.sdu.tekvideo.events

class AnswerQuestionEvent extends Event {
    String answer
    Boolean correct

    Long videoId

    static constraints = {
        answer blank: true, maxSize: 512
    }
}
