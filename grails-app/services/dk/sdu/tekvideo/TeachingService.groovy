package dk.sdu.tekvideo

import grails.transaction.Transactional

@Transactional
class TeachingService {
    def springSecurityService

    Teacher getAuthenticatedTeacher() {
        def user = (User) springSecurityService.currentUser
        return Teacher.findByUser(user)
    }

    Teacher getTeacher(String teacherName) {
        def byAlias = Teacher.findByAlias(teacherName)
        return byAlias ?: Teacher.findByUser(User.findByUsername(teacherName))
    }

    Course getCourse(String teacherName, String courseName) {
        Course.findByNameAndTeacher(courseName, getTeacher(teacherName))
    }

    Subject getSubject(String teacherName, String courseName, String subject) {
        Subject.findByNameAndCourse(subject, getCourse(teacherName, courseName))
    }

    Video getVideo(String teacherName, String courseName, String subjectName, Integer videoId) {
        Subject subject = getSubject(teacherName, courseName, subjectName)
        if (subject && videoId < subject.videos.size()) {
            return subject.videos[videoId]
        } else {
            return null
        }
    }

    Map getCompleteData(String teacherName, String courseName, String subjectName, Integer videoId) {
        def teacher = Teacher.findByUser(User.findByUsername(teacherName))
        def course = Course.findByNameAndTeacher(courseName, teacher)
        def subject = Subject.findByNameAndCourse(subjectName, course)
        if (subject && videoId < subject.videos.size()) {
            def video = subject.videos[videoId]
            return [teacher: teacher, course: course, subject: subject, video: video]
        } else {
            return null
        }
    }
}
