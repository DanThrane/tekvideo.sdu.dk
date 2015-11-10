package tekvideo

import dk.sdu.tekvideo.Course
import dk.sdu.tekvideo.data.CourseData
import dk.sdu.tekvideo.data.UserData
import spock.lang.Specification

class CourseIntegrationSpec extends Specification {
    def "test uniqueness constraint on course names"() {
        given: "two teachers"
        def teacher = UserData.buildTestTeacher("T1")
        def teacher2 = UserData.buildTestTeacher("T2")
        
        and: "some unsaved courses"
        def t1Course = CourseData.buildTestCourse("Course", teacher, false, false)
        def t1Course2 = CourseData.buildTestCourse("Course2", teacher, false, false)
        def t1Course3 = CourseData.buildTestCourse("Course3", teacher, false, false)

        def t2Course = CourseData.buildTestCourse("Course", teacher2, false, false)
        def t2Course2 = CourseData.buildTestCourse("Course2", teacher2, false, false)
        def t2Course3 = CourseData.buildTestCourse("Course3", teacher2, false, false)

        when: "we save courses that collide in name and semester, but with different teachers"
        t1Course.save(flush: true, failOnError: true)
        t1Course2.save(flush: true, failOnError: true)
        t1Course3.save(flush: true, failOnError: true)

        t2Course.save(flush: true, failOnError: true)
        t2Course2.save(flush: true, failOnError: true)
        t2Course3.save(flush: true, failOnError: true)

        then: "we shouldn't crash"

        when: "we create a new course which collides entirely"
        def t1CourseCollide = CourseData.buildTestCourse("Course", teacher, false, false)
        t1CourseCollide.name = t1Course.name

        then: "the course doesn't validate"
        !t1CourseCollide.validate()

        when: "we change the semester"
        t1CourseCollide.year = t1Course.year + 1

        then: "the course validates"
        t1CourseCollide.validate()

        when: "we save this course"
        t1CourseCollide.save(flush: true, failOnError: true)

        then: "we should have 7 courses saved"
        Course.list().size() == 7
    }
}
