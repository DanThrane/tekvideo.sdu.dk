package dk.sdu.tekvideo

import static dk.sdu.tekvideo.ServiceResult.*

import grails.transaction.Transactional

@Transactional
class StudentService {

    def springSecurityService

    Student getAuthenticatedStudent() {
        User user = (User) springSecurityService.currentUser
        if (user) {
            return Student.findByUser(user)
        }
        return null
    }

    ServiceResult<Void> signupForCourse(Student student, Course course) {
        if (isInCourse(student, course)) {
            fail("Du har er allerede tilmeldt dette kursus!", false)
        } else {
            new CourseStudent(course: course, student: student).save()
            ok(null, "Du er nu blevet tilmeldt ${course.fullName}!")
        }
    }

    boolean isInCourse(Student student, Course course) {
        CourseStudent.findByStudentAndCourse(student, course) != null
    }

    Set<Course> getAllCourses(Student student) {
        CourseStudent.findAllByStudent(student).course
    }

    def signoffForCourse(Student student, Course course) {
        if (!isInCourse(student, course)) {
            fail("Du var ikke tilmeldt dette fag til at starte med!", false)
        } else {
            def studentAndCourse = CourseStudent.findByStudentAndCourse(student, course)
            if (studentAndCourse) {
                studentAndCourse.delete()
                ok(null, "Du er nu blevet afmeldt ${course.fullName}!")
            } else {
                fail("Noget gik galt - Du var ikke tilmeldt dette fag til at starte med!", false)
            }
        }
    }
}
