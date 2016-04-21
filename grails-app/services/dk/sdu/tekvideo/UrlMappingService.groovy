package dk.sdu.tekvideo

import grails.transaction.Transactional
import org.codehaus.groovy.grails.web.mapping.LinkGenerator

/**
 * Provides URL Mapping services for the application. It is designed to help provide services for the "/t/*" URLs.
 */
@Transactional
class UrlMappingService {
    def springSecurityService
    LinkGenerator grailsLinkGenerator

    /**
     * Retrieves a teacher by alias or username, can return null.
     * @param teacherName    The teacher's alias/username
     * @return The teacher or null
     */
    Teacher getTeacher(String teacherName) {
        def byAlias = Teacher.findByAlias(teacherName)
        return byAlias ?: Teacher.findByUser(User.findByUsername(teacherName))
    }

    /**
     * Returns a course from its textual representation.
     *
     * @param teacherName    The teacher's name/alias
     * @param courseName     The course name
     * @param year           The year
     * @param spring         true if in the spring semester, otherwise false
     * @return The course or null
     */
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

    /**
     * Returns a subject from its textual representation.
     *
     * @param teacherName    The teacher's name/alias
     * @param courseName     The course name
     * @param subject        The subject
     * @param year           The year
     * @param spring         true if in the spring semester, otherwise false
     * @return The subject or null
     */
    Subject getSubject(String teacherName, String courseName, String subject, Integer year = null,
                       Boolean spring = null) {
        Subject.findByNameAndCourse(subject, getCourse(teacherName, courseName, year, spring))
    }

    /**
     * Returns a video from its textual representation.
     *
     * @param teacherName    The teacher's name/alias
     * @param courseName     The course name
     * @param subjectName    The subject
     * @param videoId        The video ID (in the subject)
     * @param year           The year
     * @param spring         true if in the spring semester, otherwise false
     * @return The video or null
     */
    Video getVideo(String teacherName, String courseName, String subjectName, Integer videoId,
                   Integer year = null, Boolean spring = null) {
        Subject subject = getSubject(teacherName, courseName, subjectName, year, spring)
        if (subject && videoId < subject.videos.size()) {
            return subject.videos[videoId]
        } else {
            return null
        }
    }

    /**
     * Generates a "/t/" link to the teacher
     * @param teacher            The teacher
     * @param additionalAttrs    Additional attributes to pass to
     *                           {@link asset.pipeline.grails.LinkGenerator#link(java.util.Map)}.
     * @return The link
     */
    String generateLinkToTeacher(Teacher teacher, Map additionalAttrs = [:]) {
        grailsLinkGenerator.link(prepareAttributes(teacher.toString(), additionalAttrs))
    }

    /**
     * Generates a "/t/" link to the teacher
     * @param course             The course
     * @param additionalAttrs    Additional attributes to pass to
     *                           {@link asset.pipeline.grails.LinkGenerator#link(java.util.Map)}.
     * @return The link
     */
    String generateLinkToCourse(Course course, Map additionalAttrs = [:]) {
        grailsLinkGenerator.link(prepareCourseAttributes(course, additionalAttrs))
    }

    /**
     * Generates a "/t/" link to the teacher
     * @param subject            The subject
     * @param additionalAttrs    Additional attributes to pass to
     *                           {@link asset.pipeline.grails.LinkGenerator#link(java.util.Map)}.
     * @return The link
     */
    String generateLinkToSubject(Subject subject, Map additionalAttrs = [:]) {
        grailsLinkGenerator.link(prepareSubjectAttributes(subject, additionalAttrs))
    }

    /**
     * Generates a "/t/" link to the video
     * @param video              The videp
     * @param additionalAttrs    Additional attributes to pass to
     *                           {@link asset.pipeline.grails.LinkGenerator#link(java.util.Map)}.
     * @return The link
     */
    String generateLinkToVideo(Video video, Map additionalAttrs = [:]) {
        grailsLinkGenerator.link(prepareVideoAttributes(video, additionalAttrs))
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

}
