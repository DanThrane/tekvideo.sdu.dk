import dk.sdu.tekvideo.Course
import dk.sdu.tekvideo.Role
import dk.sdu.tekvideo.Subject
import dk.sdu.tekvideo.User
import dk.sdu.tekvideo.UserRole
import dk.sdu.tekvideo.Video

class BootStrap {

    def init = { servletContext ->
        environments {
            development {
                def instructorRole = new Role(authority: "ROLE_INSTRUCTOR").save(flush: true)
                def taRole = new Role(authority: "ROLE_TA").save(flush: true)
                def studentRole = new Role(authority: "ROLE_STUDENT").save(flush: true)

                def instructorUser = new User(username: "Instructor", password: "password").save(flush: true)
                def taUser = new User(username: "TA", password: "password").save(flush: true)
                def studentUser = new User(username: "Student", password: "password").save(flush: true)

                UserRole.create instructorUser, instructorRole, true
                UserRole.create taUser, taRole, true
                UserRole.create studentUser, studentRole, true

                def course = new Course(name: "Fag 1", description: "Test beskrivelse")
                def course2 = new Course(name: "Fag 2", description: "Test beskrivelse")
                def subject1 = new Subject(name: "Emne 1")
                def subject2 = new Subject(name: "Emne 2")
                course.addToSubjects(subject1).addToSubjects(subject2)
                def video = new Video(name: "Test video", youtubeId: "eiSfEP7gTRw", weight: 0, timelineJson: """[
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
            }
        }
    }

    def destroy = { /* Not used */ }
}
