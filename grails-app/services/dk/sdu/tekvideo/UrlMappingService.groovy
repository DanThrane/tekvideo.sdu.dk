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
     * @param teacherName The teacher's alias/username
     * @return The teacher or null
     */
    Teacher getTeacher(String teacherName) {
        def byAlias = Teacher.findByAlias(teacherName)
        return byAlias ?: Teacher.findByUser(User.findByUsername(teacherName))
    }

    /**
     * Returns a course from its textual representation.
     *
     * @param teacherName The teacher's name/alias
     * @param courseName The course name
     * @param year The year
     * @param spring true if in the spring semester, otherwise false
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
     * @param teacherName The teacher's name/alias
     * @param courseName The course name
     * @param subject The subject
     * @param year The year
     * @param spring true if in the spring semester, otherwise false
     * @return The subject or null
     */
    Subject getSubject(String teacherName, String courseName, String subject, Integer year = null,
                       Boolean spring = null) {
        def course = getCourse(teacherName, courseName, year, spring)
        course?.subjects?.find { it.name == subject } // TODO Performance
    }

    /**
     * Returns a exercise from its textual representation.
     *
     * @param teacherName The teacher's name/alias
     * @param courseName The course name
     * @param subjectName The subject
     * @param index The index (in the subject)
     * @param year The year
     * @param spring true if in the spring semester, otherwise false
     * @return The exercise or null
     */
    Exercise getExercise(String teacherName, String courseName, String subjectName, Integer index,
                         Integer year = null, Boolean spring = null) {
        Subject subject = getSubject(teacherName, courseName, subjectName, year, spring)
        if (subject) {
            def exercises = subject.allVisibleExercises
            if (index < exercises.size()) {
                return exercises[index]
            }
        }
        return null
    }

    /**
     * Generates a "/t/" link to the teacher
     * @param teacher The teacher
     * @param additionalAttrs Additional attributes to pass to
     * {@link asset.pipeline.grails.LinkGenerator#link(java.util.Map)}.
     * @return The link
     */
    String generateLinkToTeacher(Teacher teacher, Map additionalAttrs = [:]) {
        grailsLinkGenerator.link(prepareAttributes(teacher.toString(), additionalAttrs))
    }

    /**
     * Generates a "/t/" link to the teacher
     * @param course The course
     * @param additionalAttrs Additional attributes to pass to
     * {@link asset.pipeline.grails.LinkGenerator#link(java.util.Map)}.
     * @return The link
     */
    String generateLinkToCourse(Course course, Map additionalAttrs = [:]) {
        grailsLinkGenerator.link(prepareCourseAttributes(course, additionalAttrs))
    }

    /**
     * Generates a "/t/" link to the teacher
     * @param subject The subject
     * @param additionalAttrs Additional attributes to pass to
     * {@link asset.pipeline.grails.LinkGenerator#link(java.util.Map)}.
     * @return The link
     */
    String generateLinkToSubject(Subject subject, Map additionalAttrs = [:]) {
        grailsLinkGenerator.link(prepareSubjectAttributes(subject, additionalAttrs))
    }

    /**
     * Generates a "/t/" link to the video
     * @param exercise The exercise
     * @param additionalAttrs Additional attributes to pass to
     * {@link asset.pipeline.grails.LinkGenerator#link(java.util.Map)}.
     * @return The link
     */
    String generateLinkToExercise(Exercise exercise, Map additionalAttrs = [:]) {
        grailsLinkGenerator.link(prepareExerciseAttributes(exercise, additionalAttrs))
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

    private Map prepareExerciseAttributes(Exercise exercise, Map attrs) {
        // TODO FIXME This is going to blow up at some point, since the video list is no longer guaranteed to be
        // well-formed (not sure it even was before)
        def res = prepareSubjectAttributes(exercise.subject, attrs)
        res.params.vidid = SubjectExercise.findByExercise(exercise).weight
        return res
    }

}
