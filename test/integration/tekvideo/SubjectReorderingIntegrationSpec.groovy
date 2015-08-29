package tekvideo

import dk.sdu.tekvideo.UpdateSubjectsCommand2
import dk.sdu.tekvideo.data.CourseData
import dk.sdu.tekvideo.data.SubjectData
import dk.sdu.tekvideo.data.UserData
import dk.sdu.tekvideo.v2.Course2
import dk.sdu.tekvideo.v2.CourseManagementService
import grails.plugin.springsecurity.SpringSecurityUtils
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
        Course2.findAll().size() == 1
        Course2.findAll()[0].name == "Name"
        Course2.findAll()[0].subjects.name == ["Foobar0", "Foobar1", "Foobar2"]

        when: "we authenticate the user as the teacher"
        SpringSecurityUtils.reauthenticate("Teacher", null)

        and: "we perform a re-ordering of the subjects"
        def command = new UpdateSubjectsCommand2(course: course, order: [subject2, subject3, subject1])
        def reply = courseManagementService.updateSubjects(command)

        then: "the list should have re-ordered itself"
        reply.success
        reply.result.subjects.name == ["Foobar1", "Foobar2", "Foobar0"]

        Course2.findAll().size() == 1
        Course2.findAll()[0].name == "Name"
        Course2.findAll()[0].subjects.name == ["Foobar1", "Foobar2", "Foobar0"]
    }
}
