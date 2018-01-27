package dk.sdu.tekvideo

import grails.converters.JSON
import org.springframework.security.access.annotation.Secured

class SubjectController {
    def urlMappingService
    def studentService
    def nodeService

    @Secured("permitAll")
    def viewByTeacherAndCourse(String teacherName, String courseName, String subjectName, Integer year,
                               Boolean spring, String format) {
        Subject subject = urlMappingService.getSubject(teacherName, courseName, subjectName, year, spring)
        def course = subject.course
        Student student = studentService.authenticatedStudent
        if (nodeService.canView(subject)) {
            def breadcrumbs = [
                    [link: g.createLinkTo(uri: "/"), title: "Hjem"],
                    [link: sdu.createLinkToTeacher(teacher: course.teacher), title: course.teacher.toString()],
                    [link: sdu.createLinkToCourse(course: course), title: course.fullName + "(" + course.name + ")"],
                    [title: subject.name]
            ]

            def model = [
                    data       : nodeService.listVisibleChildrenForBrowser(subject),
                    title      : subject.name,
                    breadcrumbs: breadcrumbs,
                    subtitle   : "",
                    node       : subject,
                    course     : course,
                    showSignup : student != null,
                    inCourse   : studentService.isInCourse(student, course),
            ]
            if (format != null) {
                if (format != "json") render404(format)
                else render model as JSON
            } else {
                render view: "/course/view.gsp", model: model
            }
        } else {
            render404(format)
        }
    }

    @Secured("permitAll")
    def view2(Subject subject) {
        [subject: subject]
    }

    private render404(String format) {
        if (format == "json") {
            response.status = 404
            render([message: "Not found"] as JSON)
        } else {
            render(status: 404, message: "Not found")
        }
    }
}
