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

    Course getCourse(String teacherName, String courseName, Integer year = null, Boolean spring = null) {
        if (year != null && spring == null) {
            throw new IllegalArgumentException("spring cannot be null, when year is != null")
        }

        if (year != null && spring != null) {
            Course.findByNameAndTeacherAndYearAndSpring(courseName, getTeacher(teacherName), year, spring)
        } else {
            Course.findByNameAndTeacher(courseName, getTeacher(teacherName))
        }
    }

    Subject getSubject(String teacherName, String courseName, String subject, Integer year = null,
                       Boolean spring = null) {
        Subject.findByNameAndCourse(subject, getCourse(teacherName, courseName, year, spring))
    }

    Video getVideo(String teacherName, String courseName, String subjectName, Integer videoId,
                   Integer year = null, Boolean spring = null) {
        Subject subject = getSubject(teacherName, courseName, subjectName, year, spring)
        if (subject && videoId < subject.videos.size()) {
            return subject.videos[videoId]
        } else {
            return null
        }
    }

}
