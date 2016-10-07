package tekvideo

import dk.sdu.tekvideo.Comment
import dk.sdu.tekvideo.ImportCourseCommand
import dk.sdu.tekvideo.ServiceResult
import dk.sdu.tekvideo.User
import dk.sdu.tekvideo.Video
import dk.sdu.tekvideo.data.CourseData
import dk.sdu.tekvideo.data.SubjectData
import dk.sdu.tekvideo.data.UserData
import dk.sdu.tekvideo.data.VideoData
import dk.sdu.tekvideo.events.VisitVideoEvent
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

    @Unroll("test viewing statistics (#authenticateAs, #nodes, #views, #since, #expectedViews, #success)")
    def "test viewing statistics"() {
        given: "some users"
        def users = [:]
        users.teacher1 = UserData.buildTestTeacher("teacher1")
        users.teacher2 = UserData.buildTestTeacher("teacher2")
        users.student = UserData.buildStudent("student")

        and: "adapted periods"
        long periodFrom = (since == 0) ? 0 : System.currentTimeMillis() - 1000 * 60 * 60 * 24 * since
        long periodTo = System.currentTimeMillis()

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
        tree.course2 = CourseData.buildTestCourse("Course2", users.teacher2)
        tree.subject3 = SubjectData.buildTestSubject("Subject3", tree.course2)
        tree.video1c = VideoData.buildTestVideo("Video1c", tree.subject3)

        and: "some events"
        views.each { it ->
            (1..it[2]).each { i ->
                new VisitVideoEvent(
                        user: users.student.user,
                        timestamp: System.currentTimeMillis() - it[0],
                        videoId: tree[it[1]].id
                ).save(failOnError: true, flush: true)
            }
        }

        when: "we authenticate"
        if (authenticateAs) {
            UserData.authenticateAsUser(users[authenticateAs].user)
        }

        and: "we make the call"
        def call = dashboardService.findViewingStatistics(nodes.collect { tree[it] } as List<Video>,
                periodFrom, periodTo, false)

        then: "the call might succeed"
        call.success == success

        and: "the number of data points are correct"
        !success || call.result.labels.size() == 24
        !success || call.result.data.size() == 24

        and: "the number of views are correct"
        !success || call.result.data.sum() == views.findAll { it[1] in nodes && it[0] < since }.collect { it[2] }.sum()

        where:
        authenticateAs | nodes                  | views                                           | since | success
        "teacher1"     | ["video1a"]            | [[10000, "video1a", 10], [10000, "video2a", 5]] | 20000 | true
        "teacher1"     | ["video1a", "video2a"] | [[10000, "video1a", 10], [10000, "video2a", 5]] | 20000 | true
        "teacher2"     | ["video1a", "video2a"] | [[10000, "video1a", 10], [10000, "video2a", 5]] | 20000 | false
        "teacher2"     | ["video1c", "video2a"] | [[10000, "video1a", 10], [10000, "video2a", 5]] | 20000 | false
        "teacher2"     | ["video1c"]            | [[10000, "video1c", 10], [10000, "video2a", 5]] | 20000 | true
        "student"      | ["video1c"]            | [[10000, "video1c", 10], [10000, "video2a", 5]] | 20000 | false
        "student"      | ["video1a", "video2a"] | [[10000, "video1a", 10], [10000, "video2a", 5]] | 20000 | false
    }

    @Unroll("test finding popular videos (#authenticateAs, #nodes, #views, #since, #success)")
    def "test finding popular videos"() {
        given: "some users"
        def users = [:]
        users.teacher1 = UserData.buildTestTeacher("teacher1")
        users.teacher2 = UserData.buildTestTeacher("teacher2")
        users.student = UserData.buildStudent("student")

        and: "adapted periods"
        long periodFrom = (since == 0) ? 0 : System.currentTimeMillis() - 1000 * 60 * 60 * 24 * since
        long periodTo = System.currentTimeMillis()

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
        tree.course2 = CourseData.buildTestCourse("Course2", users.teacher2)
        tree.subject3 = SubjectData.buildTestSubject("Subject3", tree.course2)
        tree.video1c = VideoData.buildTestVideo("Video1c", tree.subject3)

        and: "some events"
        views.each { it ->
            (1..it[2]).each { i ->
                new VisitVideoEvent(
                        user: users.student.user,
                        timestamp: System.currentTimeMillis() - (24 * 60 * 60 * 1000 * (it[0] as long)) as long,
                        videoId: tree[it[1]].id
                ).save(failOnError: true, flush: true)

            }
        }

        when: "we authenticate"
        if (authenticateAs) {
            UserData.authenticateAsUser(users[authenticateAs].user)
        }

        and: "we make the call"
        def call = dashboardService.findPopularVideos(nodes.collect { tree[it] } as List<Video>,
                periodFrom, periodTo)

        then: "the call might succeed"
        call.success == success

        and: "we do not get too many results"
        !success || call.result.size() <= 5

        and: "we get them out in the correct order"
        !success || call.result.collect { it.videoId } == nodes.subList(0, result).collect { tree[it].id }

        where:
        authenticateAs | result | nodes                             | views                                                            | since | success
        "teacher1"     | 2      | ["video1a", "video2a"]            | [[10, "video1a", 10], [10, "video2a", 5]]                        | 20    | true
        "teacher1"     | 2      | ["video1a", "video2a"]            | [[10, "video1a", 10], [10, "video2a", 5]]                        | 20    | true
        "teacher2"     | 2      | ["video1a", "video2a"]            | [[10, "video1a", 10], [10, "video2a", 5]]                        | 20    | false
        "student"      | 2      | ["video1a", "video2a"]            | [[10, "video1a", 10], [10, "video2a", 5]]                        | 20    | false
        null           | 2      | ["video1a", "video2a"]            | [[10, "video1a", 10], [10, "video2a", 5]]                        | 20    | false
        "teacher1"     | 2      | ["video1a", "video2a"]            | [[10, "video1a", 10], [10, "video2a", 5], [10, "video3b", 2]]    | 20    | true
        "teacher1"     | 3      | ["video1a", "video2a", "video3b"] | [[10, "video1a", 10], [10, "video2a", 5], [10, "video3b", 2]]    | 20    | true
        "teacher1"     | 2      | ["video1a", "video2a", "video3b"] | [[10, "video1a", 10], [10, "video2a", 5], [100, "video3b", 100]] | 20    | true
    }

    // find recent comments
    @Unroll("test finding recent comments (#authenticateAs, #nodes, #comments, #periodFrom, #periodTo, #success)")
    def "test finding recent comments"() {
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
        tree.course2 = CourseData.buildTestCourse("Course2", users.teacher2)
        tree.subject3 = SubjectData.buildTestSubject("Subject3", tree.course2)
        tree.video1c = VideoData.buildTestVideo("Video1c", tree.subject3)

        and: "some comments"
        (comments as List<List>).eachWithIndex { commentData, index ->
            def video = tree[commentData[0] as String] as Video
            def when = new Date((commentData[1] as long) * 1000)
            def commenter = users[commentData[2] as String].user

            def comment = new Comment(
                    user: commenter,
                    contents: "${index}"
            )
            video.addToComments(comment).save(failOnError: true, flush: true)
            comment.dateCreated = when // Working around the automatic time-stamping
            comment.save(failOnError: true, flush: true)
        }

        when: "we authenticate"
        if (authenticateAs) {
            UserData.authenticateAsUser(users[authenticateAs].user)
        }

        and: "we make the call"
        def call = dashboardService.findRecentComments(nodes.collect { tree[it] } as List<Video>,
                periodFrom * 1000, periodTo * 1000)

        then: "the call might succeed"
        call.success == success

        and: "we get the correct comments back"
        !success || call.result.collect { it.comment } == result.collect { "${it}".toString() }

        where:
        authenticateAs | result | nodes                             | comments                                                 | periodFrom | periodTo | success
        "teacher1"     | [0, 1] | ["video1b", "video2b", "video3b"] | [["video1b", 0, "teacher1"], ["video3b", 1, "teacher1"]] | 0          | 10       | true
        "teacher1"     | [0, 1] | ["video1b", "video2b", "video3b"] | [["video1b", 0, "student"], ["video3b", 1, "student"]]   | 0          | 10       | true
        "teacher1"     | [0]    | ["video1b", "video2b", "video3b"] | [["video1b", 0, "student"], ["video3b", 20, "student"]]  | 0          | 10       | true
        "student"      | []     | ["video1b", "video2b", "video3b"] | [["video1b", 0, "teacher1"], ["video3b", 1, "teacher1"]] | 0          | 10       | false

    }

    // find students

    // get answers

    // find student activity


}
