package dk.sdu.tekvideo.stats

import dk.sdu.tekvideo.Video

class AnswerVideoCommand {
    Video video
    Integer subject
    Integer question
    Boolean correct
    List<AnswerVideoDetails> details
}

class AnswerVideoDetails {
    String answer
    Boolean correct
    Integer field
}
