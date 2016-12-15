package dk.sdu.tekvideo

import org.springframework.security.access.annotation.Secured

class SubjectController {
    UrlMappingService urlMappingService
    SubjectService subjectService
    StudentService studentService

    @Secured("permitAll")
    def viewByTeacherAndCourse(String teacherName, String courseName, String subjectName, Integer year, Boolean spring) {
        Subject subject = urlMappingService.getSubject(teacherName, courseName, subjectName, year, spring)
        def course = subject.course
        Student student = studentService.authenticatedStudent
        if (subjectService.canAccess(subject)) {
            render view: "view", model: [
                    data      : subjectService.subjectForBrowser(subject),
                    subject   : subject,
                    course    : course,
                    showSignup: student != null,
                    inCourse  : studentService.isInCourse(student, course),
            ]
        } else {
            render status: 404, text: "Unable to find subject!"
        }
    }

    @Secured("permitAll")
    def view2(Subject subject) {
        [subject: subject]
    }
}
