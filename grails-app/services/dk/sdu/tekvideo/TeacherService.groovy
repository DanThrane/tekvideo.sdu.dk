package dk.sdu.tekvideo

import static dk.sdu.tekvideo.ServiceResult.*

/**
 * @author Dan Thrane
 */
class TeacherService {
    def springSecurityService

    Teacher getAuthenticatedTeacher() {
        def user = (User) springSecurityService.currentUser
        return Teacher.findByUser(user)
    }

    ServiceResult<List<Course>> getActiveCourses() {
        def teacher = getAuthenticatedTeacher()
        if (teacher) {
            ok(Course.findAllByTeacher(teacher))
        } else {
            fail("teacherservice.no_teacher", false)
        }
    }

    ServiceResult<Subject> createSubject(Course course, CreateSubjectCommand command) {
        if (command.subject.validate()) {
            course.addToSubjects(command.subject).save(flush: true)
            ok(command.subject)
        } else {
            fail("teacherservice.field_errors", false)
        }
    }

    boolean canAccess(Course course) {
        return course.teacher == getAuthenticatedTeacher()
    }
}
