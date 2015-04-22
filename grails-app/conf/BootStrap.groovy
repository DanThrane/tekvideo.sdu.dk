import dk.sdu.tekvideo.Course
import dk.sdu.tekvideo.Role
import dk.sdu.tekvideo.Student
import dk.sdu.tekvideo.Subject
import dk.sdu.tekvideo.Teacher
import dk.sdu.tekvideo.User
import dk.sdu.tekvideo.UserRole
import dk.sdu.tekvideo.Video
import dk.sdu.tekvideo.events.AnswerQuestionEvent
import dk.sdu.tekvideo.events.VisitVideoEvent

class BootStrap {

    def eventService

    def init = { servletContext ->
        environments {
            development {
                def teacherRole = new Role(authority: "ROLE_TEACHER").save(flush: true, failOnError: true)
                def instructorRole = new Role(authority: "ROLE_INSTRUCTOR").save(flush: true, failOnError: true)
                def taRole = new Role(authority: "ROLE_TA").save(flush: true, failOnError: true)
                def studentRole = new Role(authority: "ROLE_STUDENT").save(flush: true, failOnError: true)

                def teacherUser = new User(username: "Teacher", password: "password").save(flush: true, failOnError: true)
                def instructorUser = new User(username: "Instructor", password: "password").save(flush: true, failOnError: true)
                def taUser = new User(username: "TA", password: "password").save(flush: true, failOnError: true)
                def studentUser = new User(username: "Student", password: "password").save(flush: true, failOnError: true)

                def teacher = new Teacher(user: teacherUser).save(flush: true, failOnError: true)
                def student = new Student(user: studentUser).save(flush: true, failOnError: true)

                assert teacher != null

                UserRole.create teacherUser, teacherRole, true
                UserRole.create instructorUser, instructorRole, true
                UserRole.create taUser, taRole, true
                UserRole.create studentUser, studentRole, true

                def course = new Course(name: "XX123", fullName: "Fag 1", description: "Test beskrivelse")
                def course2 = new Course(name: "XX124", fullName: "Fag 2", description: "Test beskrivelse")
                def course3 = new Course(name: "XX125", fullName: "Fag 2", description: "Test beskrivelse")
                teacher.addToCourses(course)
                teacher.addToCourses(course2)
                course.addToStudents(student)
                course2.addToStudents(student)
                def subject1 = new Subject(name: "Emne 1")
                def subject2 = new Subject(name: "Emne 2")
                course.addToSubjects(subject1).addToSubjects(subject2)
                def video = new Video(name: "Test video", youtubeId: "eiSfEP7gTRw", timelineJson: """[
      {
        title: "Introduktion",
        timecode: 0,
        questions: []
      },
      {
        title: "Addition",
        timecode: 25,
        questions: [
          {
            title: "Simpel addition",
            timecode: 130, // 2:10 - 6
            fields: [
              {
                name: "firstq",
                answer: {
                  type: "custom",
                  validator: function(val) { return(val == 6); }
                },
                topoffset: 300,
                leftoffset: 265
              },
              {
                name: "secondq",
                answer: {
                  type: "between",
                  min: 0,
                  max: 42
                },
                topoffset: 0,
                leftoffset: 0
              },
              {
                name: "thirdq",
                answer: {
                  type: "equal",
                  value: "thisstring"
                },
                topoffset: 0,
                leftoffset: 100
              },
              {
                name: "fourthq",
                answer: {
                  type: "in-list",
                  value: ["thisstring", "orthis"]
                },
                topoffset: 0,
                leftoffset: 200
              },
              {
                name: "fifthq",
                answer: {
                  type: "expression",
                  value: "2x^2/4 - 5 + 3 + sqrt(y)",
                  options: {
                    form: false,
                    simplify: false
                  }
                },
                topoffset: 0,
                leftoffset: 300
              },
              {
                name: "sixthq",
                answer: {
                  type: "in-expression-list",
                  value: ["2x - 5 + 3", "5y - 2"]
                },
                topoffset: 0,
                leftoffset: 400
              }
            ]
          }
        ]
      },
      {
        title: "Subtraktion",
        timecode: 140,
        questions: [
          {
            title: "Subtraktion med tal",
            timecode: 155, // 2:35 - 5
            fields: [
              {
                name: "secondq",
                answer: {
                  type: "custom",
                  validator: function(val) { return(val == 5); }
                },
                topoffset: 170,
                leftoffset: 290
              }
            ]
          }
        ]
      }
    ]""")
                course.save(failOnError: true, flush: true)
                course2.save(failOnError: true, flush: true)
                subject1.addToVideos(video).save(failOnError: true, flush: true)

                new VisitVideoEvent(timestamp: System.currentTimeMillis(), user: studentUser, teacher: teacher,
                        course: course, subject: subject1, video: video).save(flush: true, failOnError: true)
                new VisitVideoEvent(timestamp: System.currentTimeMillis(), user: studentUser, teacher: teacher,
                        course: course, subject: subject1, video: video).save(flush: true, failOnError: true)
                new VisitVideoEvent(timestamp: System.currentTimeMillis(), user: studentUser, teacher: teacher,
                        course: course, subject: subject1, video: video).save(flush: true, failOnError: true)
                new VisitVideoEvent(timestamp: System.currentTimeMillis(), user: null, teacher: teacher,
                        course: course, subject: subject1, video: video).save(flush: true, failOnError: true)
                new VisitVideoEvent(timestamp: System.currentTimeMillis(), user: null, teacher: teacher,
                        course: course, subject: subject1, video: video).save(flush: true, failOnError: true)
                new VisitVideoEvent(timestamp: System.currentTimeMillis(), user: null, teacher: teacher,
                        course: course, subject: subject1, video: video).save(flush: true, failOnError: true)

                new AnswerQuestionEvent(timestamp: System.currentTimeMillis(), user: studentUser, teacher: teacher,
                        course: course, subject: subject1, video: video, answer: "42", correct: true)
                        .save(flush: true, failOnError: true)
                new AnswerQuestionEvent(timestamp: System.currentTimeMillis(), user: null, teacher: teacher,
                        course: course, subject: subject1, video: video, answer: "42", correct: true)
                        .save(flush: true, failOnError: true)
                new AnswerQuestionEvent(timestamp: System.currentTimeMillis(), user: null, teacher: teacher,
                        course: course, subject: subject1, video: video, answer: "1", correct: false)
                        .save(flush: true, failOnError: true)
                new AnswerQuestionEvent(timestamp: System.currentTimeMillis(), user: studentUser, teacher: teacher,
                        course: course, subject: subject1, video: video, answer: "1", correct: false)
                        .save(flush: true, failOnError: true)
                new AnswerQuestionEvent(timestamp: System.currentTimeMillis(), user: null, teacher: teacher,
                        course: course, subject: subject1, video: video, answer: "2", correct: false)
                        .save(flush: true, failOnError: true)

                println "Course students at start:"
                println course.students
            }
        }
    }

    def destroy = { /* Not used */ }
}
