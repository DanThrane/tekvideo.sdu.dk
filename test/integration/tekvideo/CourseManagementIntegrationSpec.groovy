package tekvideo

import dk.sdu.tekvideo.Course
import dk.sdu.tekvideo.NodeStatus
import dk.sdu.tekvideo.data.CourseData
import dk.sdu.tekvideo.data.SubjectData
import dk.sdu.tekvideo.data.UserData
import dk.sdu.tekvideo.data.VideoData
import spock.lang.Specification
import spock.lang.Unroll

class CourseManagementIntegrationSpec extends Specification {
    def courseManagementService

    @Unroll("test retrieving of courses owned by teacher as = '#authenticateAs'")
    def "test retrieving courses of status"() {
        given: "a teacher"
        def users = [:]
        users.teacher = UserData.buildTestTeacher("Teacher")
        users.teacher2 = UserData.buildTestTeacher("Teacher2")

        and: "a student"
        users.student = UserData.buildStudent()

        and: "some courses"
        List<Course> courses = (1..10).collect { CourseData.buildTestCourse("Course", users.teacher, true) }

        and: "some different local statuses"
        (0..2).collect { courses[it] }.forEach { it.localStatus = NodeStatus.VISIBLE }
        (3..5).collect { courses[it] }.forEach { it.localStatus = NodeStatus.INVISIBLE }
        (6..9).collect { courses[it] }.forEach { it.localStatus = NodeStatus.TRASH  }
        courses.each { it.save(failOnError: true, flush: true) }

        when: "there is no user authenticated"
        if (authenticateAs) {
            UserData.authenticateAsUser(users[authenticateAs].user)
        }

        and: "we attempt to retrieve the courses"
        def visibleCourses = courseManagementService.getCourses(NodeStatus.VISIBLE)
        def invisibleCourses = courseManagementService.getCourses(NodeStatus.INVISIBLE)
        def trashCourses = courseManagementService.getCourses(NodeStatus.TRASH)

        then: "we retrieve nothing"
        visibleCourses.success == success
        invisibleCourses.success == success
        trashCourses.success == success

        and: "the data is returned correctly"
        if (success) {
            visibleCourses.result.collect { it.id }.containsAll(visResult)
            invisibleCourses.result.collect { it.id }.containsAll(invisResult)
            trashCourses.result.collect { it.id }.containsAll(trashResult)
        }

        where:
        authenticateAs | success | visResult | invisResult | trashResult
        "teacher"      | true    | [0, 1, 2] | [3, 4, 5]   | [6, 7, 8, 9]
        "teacher2"     | true    | []        | []          | []
        "student"      | false   | null      | null        | null
        null           | false   | null      | null        | null
    }

    @Unroll("test retrieving of subjects owned by teacher, as user = '#authenticateAs'")
    def "test retrieving subjects of status"() {
        given: "a teacher"
        def users = [:]
        users.teacher = UserData.buildTestTeacher("Teacher")
        users.teacher2 = UserData.buildTestTeacher("Teacher2")

        and: "a student"
        users.student = UserData.buildStudent()

        and: "some subjects"
        def course = CourseData.buildTestCourse("Course", users.teacher)
        def subjects = (1..10).collect { SubjectData.buildTestSubject("Subject", course, true) }

        and: "some different local statuses"
        (0..2).collect { subjects[it] }.forEach { it.localStatus = NodeStatus.VISIBLE }
        (3..5).collect { subjects[it] }.forEach { it.localStatus = NodeStatus.INVISIBLE }
        (6..9).collect { subjects[it] }.forEach { it.localStatus = NodeStatus.TRASH  }
        subjects.each { it.save(failOnError: true, flush: true) }

        when: "there is no user authenticated"
        if (authenticateAs) {
            UserData.authenticateAsUser(users[authenticateAs].user)
        }

        and: "we attempt to retrieve the subjects"
        def visibleCourses = courseManagementService.getSubjects(NodeStatus.VISIBLE, course)
        def invisibleCourses = courseManagementService.getSubjects(NodeStatus.INVISIBLE, course)
        def trashCourses = courseManagementService.getSubjects(NodeStatus.TRASH, course)

        then: "we retrieve nothing"
        visibleCourses.success == success
        invisibleCourses.success == success
        trashCourses.success == success

        and: "the data is returned correctly"
        if (success) {
            visibleCourses.result.collect { it.id }.containsAll(visResult)
            invisibleCourses.result.collect { it.id }.containsAll(invisResult)
            trashCourses.result.collect { it.id }.containsAll(trashResult)
        }

        where:
        authenticateAs | success | visResult | invisResult | trashResult
        "teacher"      | true    | [0, 1, 2] | [3, 4, 5]   | [6, 7, 8, 9]
        "teacher2"     | false   | []        | []          | []
        "student"      | false   | null      | null        | null
        null           | false   | null      | null        | null
    }

    @Unroll("test retrieving of videos owned by teacher, as user = '#authenticateAs'")
    def "test retrieving videos of status"() {
        given: "a teacher"
        def users = [:]
        users.teacher = UserData.buildTestTeacher("Teacher")
        users.teacher2 = UserData.buildTestTeacher("Teacher2")

        and: "a student"
        users.student = UserData.buildStudent()

        and: "some videos"
        def course = CourseData.buildTestCourse("Course", users.teacher)
        def subject = SubjectData.buildTestSubject("Subject", course)
        def videos = (1..10).collect { VideoData.buildTestVideo("Video", subject, true) }

        and: "some different local statuses"
        (0..2).collect { videos[it] }.forEach { it.localStatus = NodeStatus.VISIBLE }
        (3..5).collect { videos[it] }.forEach { it.localStatus = NodeStatus.INVISIBLE }
        (6..9).collect { videos[it] }.forEach { it.localStatus = NodeStatus.TRASH  }
        videos.each { it.save(failOnError: true, flush: true) }

        when: "there is no user authenticated"
        if (authenticateAs) {
            UserData.authenticateAsUser(users[authenticateAs].user)
        }

        and: "we attempt to retrieve the subjects"
        def visibleCourses = courseManagementService.getSubjects(NodeStatus.VISIBLE, course)
        def invisibleCourses = courseManagementService.getSubjects(NodeStatus.INVISIBLE, course)
        def trashCourses = courseManagementService.getSubjects(NodeStatus.TRASH, course)

        then: "we retrieve nothing"
        visibleCourses.success == success
        invisibleCourses.success == success
        trashCourses.success == success

        and: "the data is returned correctly"
        if (success) {
            visibleCourses.result.collect { it.id }.containsAll(visResult)
            invisibleCourses.result.collect { it.id }.containsAll(invisResult)
            trashCourses.result.collect { it.id }.containsAll(trashResult)
        }

        where:
        authenticateAs | success | visResult | invisResult | trashResult
        "teacher"      | true    | [0, 1, 2] | [3, 4, 5]   | [6, 7, 8, 9]
        "teacher2"     | false   | []        | []          | []
        "student"      | false   | null      | null        | null
        null           | false   | null      | null        | null
    }

}
