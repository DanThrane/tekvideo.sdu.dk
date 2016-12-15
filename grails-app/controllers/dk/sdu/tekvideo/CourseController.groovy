package dk.sdu.tekvideo

import org.springframework.security.access.annotation.Secured

class CourseController {
    static defaultAction = "list"

    UrlMappingService urlMappingService
    StudentService studentService
    CourseService courseService
    def springSecurityService

    @Secured("permitAll")
    def list() {
        [data: courseService.visibleCoursesForBrowser()]
    }

    @Secured("permitAll")
    def viewByTeacher(String teacherName, String courseName, Integer year, Boolean spring) {
        Course course = urlMappingService.getCourse(teacherName, courseName, year, spring)
        Student student = studentService.authenticatedStudent
        if (courseService.canAccess(course)) {
            render(view: "view", model: [
                    course    : course,
                    data      : courseService.visibleSubjectsForBrowser(course),
                    showSignup: student != null,
                    inCourse  : studentService.isInCourse(student, course),
            ])
        } else {
            render status: "404", text: "Course not found!"
        }
    }

    @Secured(["ROLE_STUDENT"])
    def signup(Course course) {
        Student student = studentService.authenticatedStudent
        [course      : course,
         studentCount: courseService.getStudentCount(course),
         inCourse    : studentService.isInCourse(student, course),
         student     : student] // TODO Check if inCourse loads in all students
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
}
