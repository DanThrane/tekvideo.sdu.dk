package dk.sdu.tekvideo

import grails.converters.JSON
import org.springframework.security.access.annotation.Secured

class CourseController {
    static defaultAction = "list"

    UrlMappingService urlMappingService
    StudentService studentService
    CourseService courseService
    def springSecurityService
    def nodeService

    @Secured("permitAll")
    def list() {
        def collect = nodeService.listVisibleChildrenForBrowser(NodeService.ROOT)
        [data: collect]
    }

    @Secured("permitAll")
    def viewByTeacher(String teacherName, String courseName, Integer year, Boolean spring, String format) {
        Course course = urlMappingService.getCourse(teacherName, courseName, year, spring)
        Student student = studentService.authenticatedStudent
        if (nodeService.canView(course)) {
            // TODO This should obviously be refactored
            def breadcrumbs = [
                    [link: g.createLinkTo(uri: "/"), title: "Hjem"],
                    [link: sdu.createLinkToTeacher(teacher: course.teacher), title: course.teacher.toString()],
                    [title: course.fullName + "(" + course.name + ")"]
            ]
            def model = [
                    node       : course,
                    course     : course,
                    title      : course.fullName + "(" + course.name + ")",
                    subtitle   : course.shortWhen,
                    breadcrumbs: breadcrumbs,
                    data       : nodeService.listVisibleChildrenForBrowser(course),
                    showSignup : student != null,
                    inCourse   : studentService.isInCourse(student, course),
            ]

            if (format != null) {
                if (format != "json") render404(format)
                else render model as JSON
            } else {
                render(view: "view", model: model)
            }
        } else {
            render404(format)
        }
    }

    @Secured(["ROLE_STUDENT"])
    def signup(Course course) {
        Student student = studentService.authenticatedStudent
        [course      : course,
         studentCount: courseService.getStudentCount(course),
         inCourse    : studentService.isInCourse(student, course),
         student     : student]
    }

    @Secured(["ROLE_STUDENT"])
    def completeSignup(Course course) {
        if (course) {
            def result = studentService.signupForCourse(studentService.authenticatedStudent, course)
            result.updateFlashMessage(flash)
        } else {
            flash.error = "Kunne ikke finde kursus!"
        }
        redirect url: urlMappingService.generateLinkToCourse(course, [absolute: true])
    }

    @Secured(["ROLE_STUDENT"])
    def completeSignoff(Course course) {
        if (course) {
            def result = studentService.signoffForCourse(studentService.authenticatedStudent, course)
            result.updateFlashMessage(flash)
        } else {
            flash.error = "Kunne ikke finde kursus!"
        }
        redirect url: urlMappingService.generateLinkToCourse(course, [absolute: true])
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
