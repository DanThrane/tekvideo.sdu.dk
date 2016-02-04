package dk.sdu.tekvideo

import grails.transaction.Transactional
import org.codehaus.groovy.grails.web.mapping.LinkGenerator

@Transactional
class TeachingService {
    def springSecurityService

    LinkGenerator grailsLinkGenerator

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

    private Map prepareAttributes(String teacher, Map attrs) {
        def base = [
                mapping: "teaching",
                params : [teacher: teacher]
        ]

        base.putAll(attrs)
        return base
    }

    private Map prepareCourseAttributes(Course course, Map attrs) {
        def res = prepareAttributes(course.teacher.toString(), attrs)
        res.params.course = course.name
        res.params.year = course.year
        res.params.fall = course.spring ? "0" : "1"
        return res
    }

    private Map prepareSubjectAttributes(Subject subject, Map attrs) {
        def res = prepareCourseAttributes(subject.course, attrs)
        res.params.subject = subject.name
        return res
    }

    private Map prepareVideoAttributes(Video video, Map attrs) {
        def res = prepareSubjectAttributes(video.subject, attrs)
        res.params.vidid = video.videos_idx
        return res
    }

    String generateLinkToTeacher(Teacher teacher, Map additionalAttrs = [:]) {
        grailsLinkGenerator.link(prepareAttributes(teacher.alias, additionalAttrs))
    }

    String generateLinkToCourse(Course course, Map additionalAttrs = [:]) {
        grailsLinkGenerator.link(prepareCourseAttributes(course, additionalAttrs))
    }

    String generateLinkToSubject(Subject subject, Map additionalAttrs = [:]) {
        grailsLinkGenerator.link(prepareSubjectAttributes(subject, additionalAttrs))
    }

    String generateLinkToVideo(Video video, Map additionalAttrs = [:]) {
        grailsLinkGenerator.link(prepareVideoAttributes(video, additionalAttrs))
    }

}
