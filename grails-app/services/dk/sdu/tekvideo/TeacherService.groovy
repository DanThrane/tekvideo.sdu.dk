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
            ok Course.findAllByTeacher(teacher)
        } else {
            fail "teacherservice.no_teacher"
        }
    }

    ServiceResult<Subject> createSubject(Course course, CreateSubjectCommand command) {
        if (command.subject.validate()) {
            course.addToSubjects(command.subject).save(flush: true)
            ok command.subject
        } else {
            fail "teacherservice.field_errors"
        }
    }

    ServiceResult<Course> createCourse(CreateCourseCommand command) {
        def teacher = getAuthenticatedTeacher()
        if (teacher) {
            command.course.teacher = teacher
            def courseValid = command.course.validate()
            def semesterValid = command.course.semester.validate()
            if (courseValid && semesterValid) {
                command.course.semester.save()
                teacher.addToCourses(command.course).save(flush: true)
                ok command.course
            } else {
                fail "teacherservice.field_errors"
            }
        } else {
            fail "teacherservice.no_teacher"
        }
    }

    ServiceResult<Video> createVideo(CreateVideoCommand command) {
        def teacher = getAuthenticatedTeacher()
        if (teacher && canAccess(command.subject.course)) {
            def video = new Video(name: command.name, youtubeId: command.youtubeId, timelineJson: command.timelineJson)
            if (video.validate()) {
                command.subject.addToVideos(video).save(flush: true)
                ok video
            } else {
                fail "teacherservice.field_errors"
            }
        } else {
            fail "teacherservice.not_allowed"
        }
    }

    boolean canAccess(Course course) {
        return course.teacher == getAuthenticatedTeacher()
    }
}
