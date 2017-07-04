package dk.sdu.tekvideo

import dk.sdu.tekvideo.*
import dk.sdu.tekvideo.data.CourseData
import dk.sdu.tekvideo.data.SubjectData
import dk.sdu.tekvideo.data.VideoData
import grails.converters.JSON

class BootStrap {
    def eventService
    def statsEventService

    def init = { servletContext ->
        JSON.registerObjectMarshaller(ServiceResult, ServiceResult.jsonMarshaller)
        JSON.registerObjectMarshaller(Video, Video.jsonMarshaller)
        JSON.registerObjectMarshaller(Student, Student.jsonMarshaller)
        JSON.registerObjectMarshaller(User, User.jsonMarshaller)

        eventService.addHandler(statsEventService)

        environments {
            development {
                createNewTestData()
            }
        }
    }

    def destroy = {
    }

    void createNewTestData() {
        if (Role.count() != 0) {
            println "Test data already found, skipping (Bootstrap)"
            return
        } else {
            println "Generating new test data (Bootstrap)"
        }

        createUsers()

        def teacher = Teacher.findAll()[0]

        (1..10).each {
            def course = CourseData.buildTestCourse("Course", teacher, true)
            (1..3).each {
                def subject = SubjectData.buildTestSubject("Subject", course, true)
                (1..3).each {
                    def video = VideoData.buildTestVideo("Video", subject, true)
                }
            }
        }

    }

    void createUsers() {
        def teacherRole = new Role(authority: "ROLE_TEACHER").save(flush: true, failOnError: true)
        def studentRole = new Role(authority: "ROLE_STUDENT").save(flush: true, failOnError: true)

        def teacherUser = new User(username: "Teacher", password: "password").save(flush: true, failOnError: true)
        def studentUser = new User(username: "Student", password: "password").save(flush: true, failOnError: true)

        def teacher = new Teacher(user: teacherUser, alias: "teach").save(flush: true, failOnError: true)
        def student = new Student(user: studentUser).save(flush: true, failOnError: true)

        assert teacher != null
        assert student != null

        UserRole.create teacherUser, teacherRole, true
        UserRole.create studentUser, studentRole, true
    }
}
