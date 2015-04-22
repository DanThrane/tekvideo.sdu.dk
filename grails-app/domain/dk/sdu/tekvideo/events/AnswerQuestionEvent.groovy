package dk.sdu.tekvideo.events

import dk.sdu.tekvideo.Course
import dk.sdu.tekvideo.Subject
import dk.sdu.tekvideo.Teacher
import dk.sdu.tekvideo.Video

class AnswerQuestionEvent extends Event {
    String answer
    Boolean correct

    Teacher teacher
    Course course
    Subject subject
    Video video

    static constraints = {
        answer blank: true, maxSize: 512
    }
}
