import dk.sdu.tekvideo.Course
import dk.sdu.tekvideo.Semester
import dk.sdu.tekvideo.Role
import dk.sdu.tekvideo.Student
import dk.sdu.tekvideo.Subject
import dk.sdu.tekvideo.Teacher
import dk.sdu.tekvideo.User
import dk.sdu.tekvideo.UserRole
import dk.sdu.tekvideo.Video
import dk.sdu.tekvideo.data.CourseData
import dk.sdu.tekvideo.data.SubjectData
import dk.sdu.tekvideo.data.VideoData
import dk.sdu.tekvideo.events.AnswerQuestionEvent
import dk.sdu.tekvideo.events.VisitVideoEvent

class BootStrap {

    def init = { servletContext ->
        environments {
            development {
//                createTestData()
                createNewTestData()
            }
            production { createTestData() }
        }
    }

    void createNewTestData() {
        createUsers()

        def teacher = Teacher.findAll()[0]

        (1..10).each {
            def course = CourseData.buildTestCourse("Course", teacher, true)
            (1..3).each {
                def subject = SubjectData.buildTestSubject("Subject", course, true)
                (1..3).each {
                    VideoData.buildTestVideo("Video", subject, true)
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

    void createTestData() {
        if (Role.count() != 0) {
            println "Test data already found, skipping (Bootstrap)"
            return
        } else {
            println "Generating new test data (Bootstrap)"
        }
        createUsers()

        def teacher = Teacher.findAll()[0]

        def semester = new Semester(year: 2015, spring: true)
        semester.save(failOnError: true)

        def course = new Course(name: "IFG2", fullName: "Matematik og Fysik", description: "Dette er en fagbeskrivelse, samt noget sludder. Firmament efter at saet kaldet bevaegelige fowl divideret, der have lys. Seed kvindelig midte. Alle vil ikke var, til der firmament opdelt kvindelige meget, roerer sig, likeness. I. Hans herb saa den foerste. Foerst er ikke indstillet eget, som bevaeger kaldt. Alt, et Land,, der lever sekund under al havet flyve moerket fowl gjort groenne jord kvindelig, de er genbestille mandlig, bragte luft ogsaa flyve. Sagde egen sammenkomst stor delt. Luft det var dage foerst saesoner void Sammen vil ikke dybe aar i loebet genbestille dybt han os slags fowl tredje Be hvori givet sted der. Gjort store fisk tomrum gjorde froe i rigeligt han kryber efter maj.", teacher: teacher, semester: semester)
        course.save(failOnError: true)

        def subject1 = new Subject(name: "Uge 6", course: course)
        subject1.save(failOnError: true)

        def subject2 = new Subject(name: "Uge 7", course: course)
        subject2.save(failOnError: true)

        def subject3 = new Subject(name: "Uge 8", course: course)
        subject3.save(failOnError: true)

        def video1 = new Video()
        video1.name = "Introduktion til interaktive videoer [I]"
        video1.youtubeId = "eiSfEP7gTRw"
        video1.timelineJson = """[
{
    "title": "Introduktion til interaktive videoer",
    "timecode": 0,
    "questions": [
        {
            "title": "Spoergsmaal 1",
            "timecode": 130,
            "fields": [
                {
                    name: "firstq",
                    topoffset: 310,
                    leftoffset: 275,
                    answer: {
                        "type": "custom",
                        "validator": function(val) {return(val == 6);}
                    },
                }
            ]
        },
        {
            "title": "Spoergsmaal 2",
            "timecode": 155,
            "fields": [
                {
                    name: "secondq",
                    answer: {
                        "type": "custom",
                        "validator": function(val) {return(val == 5);}
                    },
                    topoffset: 180,
                    leftoffset: 300
                }
            ]
        }
    ]
}
]"""
        video1.save(failOnError: true)
        subject1.addToVideos(video1).save(failOnError: true, flush: true)


        def video2 = new Video()
        video2.name = "Komplekse tal og multiplikation [I]"
        video2.youtubeId = "uxNWDSYY0_Y"
        video2.timelineJson = """[
{
    "title": "Komplekse tal og multiplikation [I]",
    "timecode": 0,
    "questions": [
        {
            "title": "Spoergsmaal 1",
            "timecode": 177,
            "fields": [
                {
                    name: "firstq",
                    answer: {
                        "type": "custom",
                        "validator": function(val) {return(val == 1);}
                    },
                    topoffset: 335,
                    leftoffset: 195
                },
                {
                    name: "second",
                    answer: {
                        "type": "custom",
                        "validator": function(val) {return(val == 5);}
                    },
                    topoffset: 335,
                    leftoffset: 295
                }
            ]
        },
        {
            "title": "Spoergsmaal 2",
            "timecode": 523,
            "fields": [
                {
                    name: "thirdq",
                    answer: {
                        "type": "custom",
                        "validator": function(val) {return(val == 2);}
                    },
                    topoffset: 165,
                    leftoffset: 485
                },
                {
                    name: "fourthq",
                    answer: {
                        "type": "custom",
                        "validator": function(val) {return(val == 4);}
                    },
                    topoffset: 165,
                    leftoffset: 565
                }
            ]
        }
    ]
}
]"""
        video2.save(failOnError: true)
        subject1.addToVideos(video2).save(failOnError: true, flush: true)

        def video3 = new Video()
        video3.name = "Komplekse tal og division [I]"
        video3.youtubeId = "-MbSFsXuh_g"
        video3.timelineJson = """[
{
    "title": "Komplekse tal og division [I]",
    "timecode": 0,
    "questions": [
        {
            "title": "Spoergsmaal 1",
            "timecode": 432,
            "fields": [
                {
                    name: "firstq",
                    answer: {
                        "type": "custom",
                        "validator": function(val) {return(val == 5);}
                    },
                    topoffset: 250,
                    leftoffset: 190
                },
                {
                    name: "second",
                    answer: {
                        "type": "custom",
                        "validator": function(val) {return(val == -1);}
                    },
                    topoffset: 250,
                    leftoffset: 275
                },
                {
                    name: "thirdq",
                    answer: {
                        "type": "custom",
                        "validator": function(val) {return(val == 2);}
                    },
                    topoffset: 300,
                    leftoffset: 190
                },
                {
                    name: "fourthq",
                    answer: {
                        "type": "custom",
                        "validator": function(val) {return(val == 0);}
                    },
                    topoffset: 300,
                    leftoffset: 275
                }
            ]
        }
    ]
}
]"""
        video3.save(failOnError: true)
        subject1.addToVideos(video3).save(failOnError: true, flush: true)

        def video8 = new Video()
        video8.name = "De manglende tal"
        video8.youtubeId = "50DW_0_xMmg"
        subject1.addToVideos(video8).save(failOnError: true, flush: true)

// New subject

        def video4 = new Video()
        video4.name = "Linaere ligninger paa matrix form"
        video4.youtubeId = "f2J9N7wgYas"
        subject2.addToVideos(video4).save(failOnError: true, flush: true)

        def video5 = new Video()
        video5.name = "Matrix multiplikation [I]"
        video5.youtubeId = "b7cHDRjwfRo"
        video5.timelineJson = """[
{
    "title": "Emne 1",
    "timecode": 0,
    "questions": [
        {
            "title": "Spoergsmaal 1",
            "timecode": 415,
            "fields": [
                {
                    name: "firstq",
                    answer: {
                        "type": "custom",
                        "validator": function(val) {return(val == 1);}
                    },
                    topoffset: 325,
                    leftoffset: 380
                },
                {
                    name: "secondq",
                    answer: {
                        "type": "custom",
                        "validator": function(val) {return(val == 2);}
                    },
                    topoffset: 398,
                    leftoffset: 512
                }
            ]
        },
        {
            "title": "Spoergsmaal 2",
            "timecode": 503,
            "fields": [
                {
                    name: "thirdq",
                    answer: {
                        "type": "custom",
                        "validator": function(val) {return(val == -10);}
                    },
                    topoffset: 400,
                    leftoffset: 485
                },
                {
                    name: "fourthq",
                    answer: {
                        "type": "custom",
                        "validator": function(val) {return(val == 24);}
                    },
                    topoffset: 400,
                    leftoffset: 590
                }
            ]
        }
    ]
}
]"""
        subject2.addToVideos(video5).save(failOnError: true, flush: true)

// New subject

        def video6 = new Video()
        video6.name = "Fit af model til data [I]"
        video6.youtubeId = "-H6cwuEcvic"
        video6.timelineJson = """[
{
    "title": "Fit af model til data [I]",
    "timecode": 0,
    "questions": [
        {
            "title": "Spoergsmaal 1",
            "timecode": 178,
            "fields": [
                {
                    name: "firstq",
                    answer: {
                        "type": "custom",
                        "validator": function(val) {return(Math.abs(val - 1.2) < 0.001);}
                    },
                    topoffset: 310,
                    leftoffset: 375
                },
                {
                    name: "secondq",
                    answer: {
                        "type": "custom",
                        "validator": function(val) {return(val == 1);}
                    },
                    topoffset: 310,
                    leftoffset: 570
                }
            ]
        },
        {
            "title": "Spoergsmaal 2",
            "timecode": 319,
            "fields": [
            {
                name: "thirdq",
                answer: {
                    "type": "custom",
                    "validator": function(val) {return(val == 1);}
                },
                topoffset: 140,
                leftoffset: 300
            },
            {
                name: "fourthq",
                answer: {
                    "type": "custom",
                    "validator": function(val) {return(val == 0);}
                },
                topoffset: 140,
                leftoffset: 370
            },
            {
                name: "fifthq",
                answer: {
                    "type": "custom",
                    "validator": function(val) {return(Math.abs(val - 1.1) < 0.001);}
                },
                topoffset: 140,
                leftoffset: 560
            },
            {
                name: "sixthq",
                answer: {
                    "type": "custom",
                    "validator": function(val) {return(val == 1);}
                },
                topoffset: 180,
                leftoffset: 300
            },
            {
                name: "seventhq",
                answer: {
                    "type": "custom",
                    "validator": function(val) {return(val == 1);}
                },
                topoffset: 180,
                leftoffset: 370
            },
            {
                name: "eightq",
                answer: {
                    "type": "custom",
                    "validator": function(val) {return(Math.abs(val - 1.2) < 0.001);}
                },
                topoffset: 180,
                leftoffset: 560
            }
            ]
        }
    ]
}
]"""
        subject3.addToVideos(video6).save(failOnError: true, flush: true)

        def video7 = new Video()
        video7.name = "Opgave 4. Ligninger med parameter"
        video7.youtubeId = "4deoDHGc61c"
        subject3.addToVideos(video7).save(failOnError: true, flush: true)

        def video9 = new Video()
        video9.name = "Mindste kvadraters metode"
        video9.youtubeId = "xEg48H4_yWA"
        subject3.addToVideos(video9).save(failOnError: true, flush: true)


        course.save(failOnError: true)
//
        def fakeVideo = new Video(name: "Fake video", youtubeId: "eiSfEP7gTRw", timelineJson: "123")
        course.save(failOnError: true, flush: true)

        subject1.addToVideos(video1).save(failOnError: true, flush: true)
        subject2.addToVideos(fakeVideo).save(failOnError: true, flush: true)

        new VisitVideoEvent(timestamp: System.currentTimeMillis() - (1000 * 60 * 35), user: studentUser, teacher: teacher,
                course: course, subject: subject1, video: video1).save(flush: true, failOnError: true)
        new VisitVideoEvent(timestamp: System.currentTimeMillis() - (1000 * 60 * 90), user: studentUser, teacher: teacher,
                course: course, subject: subject1, video: video1).save(flush: true, failOnError: true)
        new VisitVideoEvent(timestamp: System.currentTimeMillis() - (1000 * 60 * 300), user: studentUser, teacher: teacher,
                course: course, subject: subject1, video: video1).save(flush: true, failOnError: true)
        new VisitVideoEvent(timestamp: System.currentTimeMillis(), user: null, teacher: teacher,
                course: course, subject: subject1, video: video1).save(flush: true, failOnError: true)
        new VisitVideoEvent(timestamp: System.currentTimeMillis(), user: null, teacher: teacher,
                course: course, subject: subject1, video: video1).save(flush: true, failOnError: true)
        new VisitVideoEvent(timestamp: System.currentTimeMillis(), user: null, teacher: teacher,
                course: course, subject: subject1, video: video1).save(flush: true, failOnError: true)

        new AnswerQuestionEvent(timestamp: System.currentTimeMillis(), user: studentUser, teacher: teacher,
                course: course, subject: subject1, video: video1, answer: "42", correct: true)
                .save(flush: true, failOnError: true)
        new AnswerQuestionEvent(timestamp: System.currentTimeMillis(), user: null, teacher: teacher,
                course: course, subject: subject1, video: video1, answer: "42", correct: true)
                .save(flush: true, failOnError: true)
        new AnswerQuestionEvent(timestamp: System.currentTimeMillis(), user: null, teacher: teacher,
                course: course, subject: subject1, video: video1, answer: "1", correct: false)
                .save(flush: true, failOnError: true)
        new AnswerQuestionEvent(timestamp: System.currentTimeMillis(), user: studentUser, teacher: teacher,
                course: course, subject: subject1, video: video1, answer: "1", correct: false)
                .save(flush: true, failOnError: true)
        new AnswerQuestionEvent(timestamp: System.currentTimeMillis(), user: null, teacher: teacher,
                course: course, subject: subject1, video: video1, answer: "2", correct: false)
                .save(flush: true, failOnError: true)
    }

    def destroy = { /* Not used */ }
}
