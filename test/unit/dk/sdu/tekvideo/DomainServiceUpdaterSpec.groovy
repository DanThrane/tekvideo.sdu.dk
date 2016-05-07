package dk.sdu.tekvideo

import grails.test.mixin.Mock
import grails.util.Holders
import org.codehaus.groovy.grails.commons.DefaultGrailsDomainClass
import spock.lang.Specification

/**
 * @author Dan Thrane
 */
@Mock([Course, Teacher])
class DomainServiceUpdaterSpec extends Specification {
    def "test domain validation succeeds with a valid domain"() {
        given: "A valid domain, with a valid nested domain"
        def course = getValidCourse()

        and: "A domain service updater"
        def updater = new DomainServiceUpdater<CRUDCommand, Course>(new Command(domain: course, isEditing: false))

        when: "Validation is performed"
        def result = updater.domainValidation()

        then: "The validation succeeds"
        course.validate() // Sanity check to ensure that the models actually validate
        result.success
    }

    private Course getValidCourse() {
        def course = new Course(name: "Foo", fullName: "Foobar", description: "A description", year: 2000, spring: true)
        def teacher = Mock(Teacher)
        course.teacher = teacher
        return course
    }

    static class Command implements CRUDCommand<Course> {}
}
