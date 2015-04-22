package dk.sdu.tekvideo.events

import dk.sdu.tekvideo.Teacher
import dk.sdu.tekvideo.Course
import dk.sdu.tekvideo.Subject
import dk.sdu.tekvideo.Video
import dk.sdu.tekvideo.TeachingService
import grails.transaction.Transactional

class VisitVideoEvent extends Event {
    Teacher teacher
    Course course
    Subject subject
    Video video

    static constraints = {
    }

}
