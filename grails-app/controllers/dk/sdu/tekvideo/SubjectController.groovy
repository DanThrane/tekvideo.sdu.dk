package dk.sdu.tekvideo

import org.springframework.security.access.annotation.Secured

class SubjectController {
    UrlMappingService urlMappingService
    SubjectService subjectService

    @Secured("permitAll")
    def viewByTeacherAndCourse(String teacherName, String courseName, String subjectName, Integer year, Boolean spring) {
        Subject subject = urlMappingService.getSubject(teacherName, courseName, subjectName, year, spring)
        if (subjectService.canAccess(subject)) {
            render view: "view", model: [subject: subject]
        } else {
            render status: 404, text: "Unable to find subject!"
        }
    }

    @Secured("permitAll")
    def view2(Subject subject) {
        [subject: subject]
    }
}
