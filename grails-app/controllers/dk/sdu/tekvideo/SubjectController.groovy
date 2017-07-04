package dk.sdu.tekvideo

import org.springframework.security.access.annotation.Secured

class SubjectController {
    def urlMappingService
    def studentService
    def nodeService

    @Secured("permitAll")
    def viewByTeacherAndCourse(String teacherName, String courseName, String subjectName, Integer year, Boolean spring) {
        Subject subject = urlMappingService.getSubject(teacherName, courseName, subjectName, year, spring)
        def course = subject.course
        Student student = studentService.authenticatedStudent
        if (nodeService.canView(subject)) {
            render view: "view", model: [
                    data      : nodeService.listVisibleChildrenForBrowser(subject),
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
