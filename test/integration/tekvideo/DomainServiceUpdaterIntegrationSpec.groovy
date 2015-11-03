package tekvideo

import dk.sdu.tekvideo.CRUDCommand
import dk.sdu.tekvideo.Course
import dk.sdu.tekvideo.DomainServiceUpdater
import dk.sdu.tekvideo.Teacher
import dk.sdu.tekvideo.User
import grails.test.spock.IntegrationSpec

class DomainServiceUpdaterIntegrationSpec extends IntegrationSpec {

    def setup() {
    }

    def cleanup() {
    }

    void "test dispatch"() {
        given: "a valid domain object"
        def course = getValidCourse()

        and: "a service updater"
        def updater = new DomainServiceUpdater(new CRUDCommand(domain: course))

        when: "the updater has not yet been called"
        then:
        Course.list().isEmpty()

        when: "the dispatch method is called"
        updater.dispatch()

        then: "the course is saved"
        Course.list().size() == 1
        Course.list()[0].name == "Foo"
    }

    private Course getValidCourse() {
        def course = new Course(name: "Foo", fullName: "Foobar", description: "A description", year: 2015, spring: true)
        def user = new User(username: "Teacher", password: "Foo", email: "foo@foo.dk")
        def teacher = new Teacher(user: user)
        user.save(flush: true)
        teacher.save(flush: true)
        course.teacher = teacher
        return course
    }
}
