package dk.sdu.tekvideo.events

import dk.sdu.tekvideo.Video

class AnswerQuestionEvent extends Event {
    String answer
    Boolean correct

    Video video

    static constraints = {
        answer blank: true, maxSize: 512
    }
}
