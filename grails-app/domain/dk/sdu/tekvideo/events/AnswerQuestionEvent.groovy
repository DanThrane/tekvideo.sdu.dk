package dk.sdu.tekvideo.events

class AnswerQuestionEvent extends Event {
    String answer;
    Boolean correct;

    static constraints = {
        answer blank: true, maxSize: 512
    }
}
