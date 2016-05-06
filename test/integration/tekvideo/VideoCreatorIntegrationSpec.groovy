package tekvideo

import dk.sdu.tekvideo.CreateOrUpdateVideoCommand
import dk.sdu.tekvideo.Subject
import dk.sdu.tekvideo.data.SubjectData
import dk.sdu.tekvideo.data.UserData
import dk.sdu.tekvideo.data.VideoData
import grails.test.spock.IntegrationSpec

class VideoCreatorIntegrationSpec extends IntegrationSpec {
    def courseManagementService

    void "test moving a video from one subject to another"() {
        given: "a test video"
        def video = VideoData.buildTestVideo("The real video name")
        def oldSubject = video.subject

        and: "an additional subject which we can move it to"
        def newSubject = SubjectData.buildTestSubject("New Subject", video.subject.course)

        when: "we perform a sanity check"
        then:
        Subject.get(oldSubject.id).videos.size() == 1
        !Subject.get(newSubject.id).videos
        Subject.get(oldSubject.id).videos[0].name == "The real video name"

        when: "we authenticate and send the update command"
        UserData.authenticateAsTestTeacher(video.subject.course.teacher)

        def command = new CreateOrUpdateVideoCommand([
                isEditing   : true,
                subject     : newSubject,
                editing     : video,
                youtubeId   : video.youtubeId,
                name        : video.name,
                timelineJson: video.timelineJson,
                description : video.description,
                videoType   : video.videoType
        ])

        def result = courseManagementService.createOrEditVideo(command)

        then:
        result.success
        !Subject.get(oldSubject.id).videos
        Subject.get(newSubject.id).videos.size() == 1
        Subject.get(newSubject.id).videos[0].name == "The real video name"
    }

    void "test creating a new video"() {
        given: "a test subject"
        def subject = SubjectData.buildTestSubject()

        when: "we authenticate as the teacher"
        UserData.authenticateAsTestTeacher(subject.course.teacher)

        and: "we create and send the update command"
        def command = new CreateOrUpdateVideoCommand([
                isEditing   : false,
                subject     : subject,
                editing     : null,
                youtubeId   : "12345678",
                name        : "Video name",
                timelineJson: "[]",
                description : "A video description",
                videoType   : true
        ])

        def result = courseManagementService.createOrEditVideo(command)

        then:
        result.success
        Subject.get(subject.id).videos.size() == 1
        Subject.get(subject.id).videos[0].name == "Video name"
        Subject.get(subject.id).videos[0].youtubeId == "12345678"
        Subject.get(subject.id).videos[0].timelineJson == "[]"
        Subject.get(subject.id).videos[0].description == "A video description"
        Subject.get(subject.id).videos[0].videoType
    }
}
