package tekvideo

import dk.sdu.tekvideo.NodeStatus
import dk.sdu.tekvideo.Video
import dk.sdu.tekvideo.data.VideoData
import spock.lang.Specification

class NodeIntegrationSpec extends Specification {
    def "test that nodes have a recursively defined status"() {
        given: "some data"
        Video video = VideoData.buildTestVideo() // Automatically builds the associated subject, and course

        when: "we mark the subject as 'trash'"
        video.subject.localStatus = NodeStatus.TRASH
        video.subject.save(flush: true)

        then: "the status of the video should be 'trash'"
        Video.get(video.id).localStatus == NodeStatus.VISIBLE
        Video.get(video.id).status == NodeStatus.TRASH
    }

    def "test that nodes have a recursively defined status (2 layers)"() {
        given: "some data"
        Video video = VideoData.buildTestVideo() // Automatically builds the associated subject, and course

        when: "we mark the course as 'trash'"
        video.subject.course.localStatus = NodeStatus.TRASH
        video.subject.course.save(flush: true)

        then: "the status of the video should be 'trash'"
        Video.get(video.id).localStatus == NodeStatus.VISIBLE
        Video.get(video.id).status == NodeStatus.TRASH
        Video.get(video.id).subject.status == NodeStatus.TRASH
        Video.get(video.id).subject.localStatus == NodeStatus.VISIBLE
        Video.get(video.id).subject.course.status == NodeStatus.TRASH
    }
}
