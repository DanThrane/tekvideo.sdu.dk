package dk.sdu.tekvideo

import grails.test.mixin.Mock
import grails.util.Holders
import org.codehaus.groovy.grails.commons.DefaultGrailsDomainClass
import spock.lang.Specification

/**
 * @author Dan Thrane
 */
@Mock([Course, Semester, Teacher])
class DomainServiceUpdaterSpec extends Specification {
    def "test domain validation with invalid nested domain"() {
        given: "A valid domain, with an invalid nested domain"
        def course = new Course(name: "Foo", fullName: "Foobar", description: "A description")
        course.semester = semester

        and: "A domain service updater"
        def updater = new DomainServiceUpdater(new CRUDCommand(domain: course, isEditing: false))

        when: "Validation is performed"
        def result = updater.domainValidation()

        then: "The validation fails"
        !result.success

        where:
        semester                                            ||  placeholder
        new Semester(spring: false, year: 100000)           ||  null
        null                                                ||  null
    }

    def "test domain validation succeeds with a valid domain"() {
        given: "A valid domain, with a valid nested domain"
        def course = getValidCourse()

        and: "A domain service updater"
        def updater = new DomainServiceUpdater(new CRUDCommand(domain: course, isEditing: false))

        when: "Validation is performed"
        def result = updater.domainValidation()

        then: "The validation succeeds"
        course.validate() // Sanity check to ensure that the models actually validate
        course.semester.validate()
        result.success
    }

    private Course getValidCourse() {
        def course = new Course(name: "Foo", fullName: "Foobar", description: "A description")
        def semester = new Semester(spring: false, year: 2015)
        def teacher = Mock(Teacher)
        course.semester = semester
        course.teacher = teacher
        return course
    }
}
