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
}
