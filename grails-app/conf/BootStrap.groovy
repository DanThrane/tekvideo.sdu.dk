import dk.sdu.tekvideo.*
import dk.sdu.tekvideo.data.CourseData
import dk.sdu.tekvideo.data.SubjectData
import dk.sdu.tekvideo.data.VideoData
import dk.sdu.tekvideo.events.AnswerQuestionEvent
import dk.sdu.tekvideo.events.VisitVideoEvent

class BootStrap {

    def init = { servletContext ->
        environments {
            development {
                createNewTestData()
            }
            production {

            }
        }
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
                    def vid = VideoData.buildTestVideo("Video", subject, true)
                    def video = vid.id

                    new VisitVideoEvent([
                            timestamp: System.currentTimeMillis() - (1000 * 60 * 35),
                            user     : studentUser,
                            video    : video]).save(flush: true, failOnError: true)
                    new VisitVideoEvent([
                            timestamp: System.currentTimeMillis() - (1000 * 60 * 90),
                            user     : studentUser,
                            video    : video]).save(flush: true, failOnError: true)
                    new VisitVideoEvent([
                            timestamp: System.currentTimeMillis() - (1000 * 60 * 300),
                            user     : studentUser,
                            video    : video]).save(flush: true, failOnError: true)
                    new VisitVideoEvent([
                            timestamp: System.currentTimeMillis(),
                            user     : null,
                            video    : video]).save(flush: true, failOnError: true)
                    new VisitVideoEvent([
                            timestamp: System.currentTimeMillis(),
                            user     : null,
                            video    : video]).save(flush: true, failOnError: true)
                    new VisitVideoEvent([
                            timestamp: System.currentTimeMillis(),
                            user     : null,
                            video    : video]).save(flush: true, failOnError: true)

                    new AnswerQuestionEvent([
                            timestamp: System.currentTimeMillis(),
                            user     : studentUser,
                            video    : video,
                            answer   : "42",
                            correct  : true
                    ]).save(flush: true, failOnError: true)
                    new AnswerQuestionEvent([
                            timestamp: System.currentTimeMillis(),
                            user     : null,
                            video    : video,
                            answer   : "42",
                            correct  : true
                    ]).save(flush: true, failOnError: true)
                    new AnswerQuestionEvent([
                            timestamp: System.currentTimeMillis(),
                            user     : null,
                            video    : video,
                            answer   : "1",
                            correct  : false
                    ]).save(flush: true, failOnError: true)
                    new AnswerQuestionEvent([
                            timestamp: System.currentTimeMillis(),
                            user     : studentUser,
                            video    : video,
                            answer   : "1",
                            correct  : false
                    ]).save(flush: true, failOnError: true)
                    new AnswerQuestionEvent([
                            timestamp: System.currentTimeMillis(),
                            user     : null,
                            video    : video,
                            answer   : "2",
                            correct  : false
                    ]).save(flush: true, failOnError: true)
                }
            }
        }

    }

    void createUsers() {
        def teacherRole = new Role(authority: "ROLE_TEACHER").save(flush: true, failOnError: true)
        def instructorRole = new Role(authority: "ROLE_INSTRUCTOR").save(flush: true, failOnError: true)
        def taRole = new Role(authority: "ROLE_TA").save(flush: true, failOnError: true)
        def studentRole = new Role(authority: "ROLE_STUDENT").save(flush: true, failOnError: true)

        def teacherUser = new User(username: "Teacher", password: "password").save(flush: true, failOnError: true)
        def instructorUser = new User(username: "Instructor", password: "password").save(flush: true, failOnError: true)
        def taUser = new User(username: "TA", password: "password").save(flush: true, failOnError: true)
        def studentUser = new User(username: "Student", password: "password").save(flush: true, failOnError: true)
        def lazyUser = new User(username: "Lazy Student", password: "password").save(flush: true, failOnError: true)

        def teacher = new Teacher(user: teacherUser, alias: "teach").save(flush: true, failOnError: true)
        def student = new Student(user: studentUser).save(flush: true, failOnError: true)
        def lazyStudent = new Student(user: lazyUser).save(flush: true, failOnError: true)

        assert teacher != null

        UserRole.create teacherUser, teacherRole, true
        UserRole.create instructorUser, instructorRole, true
        UserRole.create taUser, taRole, true
        UserRole.create studentUser, studentRole, true
        UserRole.create lazyUser, studentRole, true
    }

    def destroy = { /* Not used */ }
}
