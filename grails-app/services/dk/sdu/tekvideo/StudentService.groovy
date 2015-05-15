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
            course.addToStudents(student).save()
            ok(null, "Du er nu blevet tilmeldt ${course.fullName}!")
        }
    }

    boolean isInCourse(Student student, Course course) {
        // TODO Check if this looks up all students in the course, or if it does something smart (which I hope)
        return student in course.students
    }

    def signoffForCourse(Student student, Course course) {
        if (!isInCourse(student, course)) {
            fail("Du var ikke tilmeldt dette fag til at starte med!", false)
        } else {
            course.removeFromStudents(student)
            ok(null, "Du er nu blevet afmeldt ${course.fullName}!")
        }
    }
}
