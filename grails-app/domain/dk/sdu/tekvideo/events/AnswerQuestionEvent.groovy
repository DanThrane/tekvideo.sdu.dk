package dk.sdu.tekvideo.events

class AnswerQuestionEvent extends Event {
    String answer
    Boolean correct

    Long video

    static constraints = {
        answer blank: true, maxSize: 512
    }
}
