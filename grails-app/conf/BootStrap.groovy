import dk.sdu.tekvideo.*
import dk.sdu.tekvideo.data.CourseData
import dk.sdu.tekvideo.data.EventData
import dk.sdu.tekvideo.data.SubjectData
import dk.sdu.tekvideo.data.VideoData
import dk.sdu.tekvideo.events.*
import dk.sdu.tekvideo.events.AnswerQuestionEvent
import grails.converters.JSON

class BootStrap {

    def init = { servletContext ->
        JSON.registerObjectMarshaller(ServiceResult, ServiceResult.jsonMarshaller)
        JSON.registerObjectMarshaller(AnswerQuestionEvent, AnswerQuestionEvent.jsonMarshaller)
        JSON.registerObjectMarshaller(Video, Video.jsonMarshaller)
        JSON.registerObjectMarshaller(Student, Student.jsonMarshaller)
        JSON.registerObjectMarshaller(User, User.jsonMarshaller)

        environments {
            development {
                createNewTestData()
            }
            production {

            }
        }
    }

    void truncateTestData() {
        Comment.executeUpdate('delete from Comment')
        Video.executeUpdate('delete from Video')
        Subject.executeUpdate('delete from Subject')
        Course.executeUpdate('delete from Course')

        CourseStudent.executeUpdate('delete from CourseStudent')
        Event.executeUpdate('delete from Event')

        Student.executeUpdate('delete from Student')
        Teacher.executeUpdate('delete from Teacher')

        UserRole.executeUpdate('delete from UserRole')
        User.executeUpdate('delete from User')
        Role.executeUpdate('delete from Role')
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
        def studentUser = Student.findAll()[0].user

        (1..10).each {
            def course = CourseData.buildTestCourse("Course", teacher, true)
            (1..3).each {
                def subject = SubjectData.buildTestSubject("Subject", course, true)
                (1..3).each {
                    def video = VideoData.buildTestVideo("Video", subject, true)
                    (1..10).each { EventData.buildVisitVideoEvent(video) }
                    (1..10).each { EventData.buildAnswerEvent(video) }
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

    def destroy = { /* Not used */ }
}
