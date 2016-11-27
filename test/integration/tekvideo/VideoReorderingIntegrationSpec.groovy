package tekvideo

import dk.sdu.tekvideo.Course
import dk.sdu.tekvideo.CourseManagementService
import dk.sdu.tekvideo.UpdateSubjectsCommand
import dk.sdu.tekvideo.UpdateExercisesCommand
import dk.sdu.tekvideo.data.CourseData
import dk.sdu.tekvideo.data.SubjectData
import dk.sdu.tekvideo.data.UserData
import dk.sdu.tekvideo.data.VideoData
import grails.test.spock.IntegrationSpec

class VideoReorderingIntegrationSpec extends IntegrationSpec {
    CourseManagementService courseManagementService

    void "test re-ordering of videos"() {
        given: "A teacher"
        def teacher = UserData.buildTestTeacher()

        and: "a course"
        def course = CourseData.buildTestCourse("Name", teacher)

        and: "a subjects"
        def subject = SubjectData.buildTestSubject("Foobar", course, true)

        and: "some videos"
        def video0 = VideoData.buildTestVideo("Video", subject, true)
        def video1 = VideoData.buildTestVideo("Video", subject, true)
        def video2 = VideoData.buildTestVideo("Video", subject, true)
        def video3 = VideoData.buildTestVideo("Video", subject, true)

        when: "we perform a sanity check"
        then:
        Course.findAll().size() == 1
        Course.findAll()[0].name == "Name"
        Course.findAll()[0].subjects.size() == 1
        Course.findAll()[0].subjects[0].videos.size() == 4
        Course.findAll()[0].subjects[0].videos.name == [video0.name, video1.name, video2.name, video3.name]

        when: "we authenticate the user as the teacher"
        UserData.authenticateAsTestTeacher()

        and: "we perform a re-ordering of the videos"
        def command = new UpdateExercisesCommand(subject: subject, order: [video0, video2, video1])
        def reply = courseManagementService.updateExercises(command)

        then: "the list should have re-ordered itself"
        reply.success
        reply.result.activeVideos.name == [video0.name, video2.name, video1.name]

        Course.findAll().size() == 1
        Course.findAll()[0].name == "Name"
        Course.findAll()[0].subjects.size() == 1
        Course.findAll()[0].subjects[0].videos.size() == 4
        Course.findAll()[0].subjects[0].videos.name == [video0.name, video2.name, video1.name, video3.name]
    }

    void "test re-ordering of subjects"() {
        given: "A teacher"
        def teacher = UserData.buildTestTeacher()

        and: "a course"
        def course = CourseData.buildTestCourse("Name", teacher)

        and: "some subjects"
        def subject0 = SubjectData.buildTestSubject("Foobar", course, true)
        def subject1 = SubjectData.buildTestSubject("Foobar", course, true)
        def subject2 = SubjectData.buildTestSubject("Foobar", course, true)
        def subject3 = SubjectData.buildTestSubject("Foobar", course, true)
        def subject4 = SubjectData.buildTestSubject("Foobar", course, true)

        when: "we perform a sanity check"
        then:
        Course.findAll().size() == 1
        Course.findAll()[0].name == "Name"
        Course.findAll()[0].subjects.size() == 5
        Course.findAll()[0].subjects.name == [subject0, subject1, subject2, subject3, subject4].name

        when: "we authenticate the user as the teacher"
        UserData.authenticateAsTestTeacher()

        and: "we perform a re-ordering of the videos"
        def command = new UpdateSubjectsCommand(course: course, order: [subject3, subject2, subject1])
        def reply = courseManagementService.updateSubjects(command)

        then: "the list should have re-ordered itself"
        reply.success
        reply.result.activeSubjects.name == [subject3, subject2, subject1].name

        Course.findAll().size() == 1
        Course.findAll()[0].name == "Name"
        Course.findAll()[0].subjects.size() == 5
        Course.findAll()[0].activeSubjects.size() == 3
    }
}
