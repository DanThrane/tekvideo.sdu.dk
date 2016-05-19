package tekvideo

import dk.sdu.tekvideo.ImportCourseCommand
import dk.sdu.tekvideo.ServiceResult
import dk.sdu.tekvideo.Video
import dk.sdu.tekvideo.data.CourseData
import dk.sdu.tekvideo.data.SubjectData
import dk.sdu.tekvideo.data.UserData
import dk.sdu.tekvideo.data.VideoData
import spock.lang.Specification
import spock.lang.Unroll

class DashboardIntegrationSpec extends Specification {
    def dashboardService

    @Unroll("test node from identifier (#identifier, #success)")
    def "test node from identifier"() {
        given: "a tree"
        def course = CourseData.buildTestCourse()
        def subject = SubjectData.buildTestSubject("Subject", course)
        def video = VideoData.buildTestVideo("Video", subject)

        when: "we retrieve a node from an identifier"
        def call = dashboardService.nodeFromIdentifier(
                identifier.replace("[CID]", course.id.toString())
                        .replace("[SID]", subject.id.toString())
                        .replace("[VID]", video.id.toString())
        )

        then: "the call might succeed"
        call.success == success

        where:
        identifier       | success
        "course/[CID]"   | true
        "subject/[SID]"  | true
        "video/[VID]"    | true
        "course/1000000" | false
        "subject/100000" | false
        "video/1000000"  | false
        "course/a"       | false
        "subject/a"      | false
        "video/a"        | false
        "asdqwe"         | false
        ""               | false
        "course/1/1"     | false
        "subject/1/1"    | false
        "video/1/1"      | false
    }

    @Unroll("test finding leaves from a node (#authenticateAs, #node, #leaves, #success)")
    def "test finding leaves from a node"() {
        given: "some users"
        def users = [:]
        users.teacher1 = UserData.buildTestTeacher("teacher1")
        users.teacher2 = UserData.buildTestTeacher("teacher2")
        users.student = UserData.buildStudent("student")

        and: "a tree"
        def tree = [:]
        tree.course = CourseData.buildTestCourse("Course", users.teacher1)
        tree.subject1 = SubjectData.buildTestSubject("Subject1", tree.course)
        tree.subject2 = SubjectData.buildTestSubject("Subject2", tree.course)
        tree.video1a = VideoData.buildTestVideo("Video1a", tree.subject1)
        tree.video2a = VideoData.buildTestVideo("Video2a", tree.subject1)
        tree.video3a = VideoData.buildTestVideo("Video3a", tree.subject1)
        tree.video1b = VideoData.buildTestVideo("Video1b", tree.subject2)
        tree.video2b = VideoData.buildTestVideo("Video2b", tree.subject2)
        tree.video3b = VideoData.buildTestVideo("Video3b", tree.subject2)

        when: "we authenticate"
        if (authenticateAs) {
            UserData.authenticateAsUser(users[authenticateAs].user)
        }

        and: "we make the call"
        def call = dashboardService.findLeaves(tree[node])

        then: "the call might succeed"
        call.success == success

        and: "if it does, we retrieve the correct leaves"
        !success || call.result.toSet() == (leaves.collect { tree[it] }).toSet()

        where:
        authenticateAs | node       | leaves                                                             | success
        "teacher1"     | "course"   | ["video1a", "video2a", "video3a", "video1b", "video2b", "video3b"] | true
        "teacher1"     | "subject1" | ["video1a", "video2a", "video3a"]                                  | true
        "teacher1"     | "subject2" | ["video1b", "video2b", "video3b"]                                  | true
        "teacher1"     | "video1a"  | ["video1a"]                                                        | true
        "teacher1"     | "video2a"  | ["video2a"]                                                        | true
        "teacher1"     | "video3a"  | ["video3a"]                                                        | true
        "teacher1"     | "video1b"  | ["video1b"]                                                        | true
        "teacher1"     | "video2b"  | ["video2b"]                                                        | true
        "teacher1"     | "video3b"  | ["video3b"]                                                        | true
        "teacher2"     | "course"   | ["video1a", "video2a", "video3a", "video1b", "video2b", "video3b"] | false
        "teacher2"     | "subject1" | ["video1a", "video2a", "video3a"]                                  | false
        "teacher2"     | "subject2" | ["video1b", "video2b", "video3b"]                                  | false
        "teacher2"     | "video1a"  | ["video1a"]                                                        | false
        "teacher2"     | "video2a"  | ["video2a"]                                                        | false
        "teacher2"     | "video3a"  | ["video3a"]                                                        | false
        "teacher2"     | "video1b"  | ["video1b"]                                                        | false
        "teacher2"     | "video2b"  | ["video2b"]                                                        | false
        "teacher2"     | "video3b"  | ["video3b"]                                                        | false
        "student"      | "course"   | ["video1a", "video2a", "video3a", "video1b", "video2b", "video3b"] | false
        "student"      | "subject1" | ["video1a", "video2a", "video3a"]                                  | false
        "student"      | "subject2" | ["video1b", "video2b", "video3b"]                                  | false
        "student"      | "video1a"  | ["video1a"]                                                        | false
        "student"      | "video2a"  | ["video2a"]                                                        | false
        "student"      | "video3a"  | ["video3a"]                                                        | false
        "student"      | "video1b"  | ["video1b"]                                                        | false
        "student"      | "video2b"  | ["video2b"]                                                        | false
        "student"      | "video3b"  | ["video3b"]                                                        | false
    }
}
