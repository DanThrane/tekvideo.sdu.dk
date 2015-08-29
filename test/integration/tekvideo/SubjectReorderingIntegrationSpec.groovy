package tekvideo

import dk.sdu.tekvideo.Course
import dk.sdu.tekvideo.CourseManagementService
import dk.sdu.tekvideo.UpdateSubjectsCommand
import dk.sdu.tekvideo.data.CourseData
import dk.sdu.tekvideo.data.SubjectData
import dk.sdu.tekvideo.data.UserData
import grails.test.spock.IntegrationSpec

class SubjectReorderingIntegrationSpec extends IntegrationSpec {
    CourseManagementService courseManagementService

    void "test re-ordering of subjects"() {
        given: "A teacher"
        def teacher = UserData.buildTestTeacher()

        and: "a course"
        def course = CourseData.buildTestCourse("Name", teacher)

        and: "some subjects"
        def subject1 = SubjectData.buildTestSubject("Foobar", course, true)
        def subject2 = SubjectData.buildTestSubject("Foobar", course, true)
        def subject3 = SubjectData.buildTestSubject("Foobar", course, true)

        when: "we perform a sanity check"
        then:
        Course.findAll().size() == 1
        Course.findAll()[0].name == "Name"
        Course.findAll()[0].subjects.name == [subject1.name, subject2.name, subject3.name]

        when: "we authenticate the user as the teacher"
        UserData.authenticateAsTestTeacher()

        and: "we perform a re-ordering of the subjects"
        def command = new UpdateSubjectsCommand(course: course, order: [subject2, subject3, subject1])
        def reply = courseManagementService.updateSubjects(command)

        then: "the list should have re-ordered itself"
        reply.success
        reply.result.subjects.name == [subject2.name, subject3.name, subject1.name]

        Course.findAll().size() == 1
        Course.findAll()[0].name == "Name"
        Course.findAll()[0].subjects.name == [subject2.name, subject3.name, subject1.name]
    }

    void "test deletion of subjects"() {
        given: "A teacher"
        def teacher = UserData.buildTestTeacher()

        and: "a course"
        def course = CourseData.buildTestCourse("Name", teacher)

        and: "some subjects"
        def subject1 = SubjectData.buildTestSubject("Foobar", course, true)
        def subject2 = SubjectData.buildTestSubject("Foobar", course, true)
        def subject3 = SubjectData.buildTestSubject("Foobar", course, true)

        when: "we perform a sanity check"
        then:
        Course.findAll().size() == 1
        Course.findAll()[0].name == "Name"
        Course.findAll()[0].subjects.name == [subject1.name, subject2.name, subject3.name]

        when: "we authenticate the user as the teacher"
        UserData.authenticateAsTestTeacher()

        and: "we perform a re-ordering of the subjects"
        def command = new UpdateSubjectsCommand(course: course, order: [subject1, subject2])
        def reply = courseManagementService.updateSubjects(command)

        then: "the list should have re-ordered itself"
        reply.success
        reply.result.subjects.name == [subject1.name, subject2.name]

        Course.findAll().size() == 1
        Course.findAll()[0].name == "Name"
        Course.findAll()[0].subjects.name == [subject1.name, subject2.name]
    }
}
