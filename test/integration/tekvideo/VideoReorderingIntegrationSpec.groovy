package tekvideo

import dk.sdu.tekvideo.Course
import dk.sdu.tekvideo.TeacherService
import dk.sdu.tekvideo.UpdateVideosCommand
import dk.sdu.tekvideo.data.CourseData
import dk.sdu.tekvideo.data.SubjectData
import dk.sdu.tekvideo.data.UserData
import dk.sdu.tekvideo.data.VideoData
import grails.test.spock.IntegrationSpec

class VideoReorderingIntegrationSpec extends IntegrationSpec {
    TeacherService teacherService

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
        Course.findAll()[0].subjects[0].videos.name == ["Video0", "Video1", "Video2", "Video3"]

        when: "we authenticate the user as the teacher"
        UserData.authenticateAsTestTeacher()

        and: "we perform a re-ordering of the videos"
        def command = new UpdateVideosCommand(subject: subject, order: [video0, video2, video1, video3])
        def reply = teacherService.updateVideos(command)

        then: "the list should have re-ordered itself"
        reply.success
        reply.result.videos.name == ["Video0", "Video2", "Video1", "Video3"]

        Course.findAll().size() == 1
        Course.findAll()[0].name == "Name"
        Course.findAll()[0].subjects.size() == 1
        Course.findAll()[0].subjects[0].videos.size() == 4
        Course.findAll()[0].subjects[0].videos.name == ["Video0", "Video2", "Video1", "Video3"]
    }
}
