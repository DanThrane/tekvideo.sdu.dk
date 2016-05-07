package tekvideo

import dk.sdu.tekvideo.Comment
import dk.sdu.tekvideo.Course
import dk.sdu.tekvideo.CourseCRUDCommand
import dk.sdu.tekvideo.CreateOrUpdateVideoCommand
import dk.sdu.tekvideo.NodeStatus
import dk.sdu.tekvideo.Subject
import dk.sdu.tekvideo.SubjectCRUDCommand
import dk.sdu.tekvideo.Teacher
import dk.sdu.tekvideo.Video
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
        (6..9).collect { courses[it] }.forEach { it.localStatus = NodeStatus.TRASH }
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
        (6..9).collect { subjects[it] }.forEach { it.localStatus = NodeStatus.TRASH }
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
        (6..9).collect { videos[it] }.forEach { it.localStatus = NodeStatus.TRASH }
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

    @Unroll("test retrieving active courses owned by teacher as = '#authenticateAs'")
    def "test retrieving active courses"() {
        given: "a teacher"
        def users = [:]
        users.teacher = UserData.buildTestTeacher("Teacher")
        users.teacher2 = UserData.buildTestTeacher("Teacher2")

        and: "a student"
        users.student = UserData.buildStudent()

        and: "some courses"
        List<Course> courses = (1..10).collect { CourseData.buildTestCourse("Course", users.teacher, true) }
        List<Course> expectedActive = []
        for (int i : activeResults) {
            expectedActive += courses[i]
        }

        and: "some different local statuses"
        (0..2).collect { courses[it] }.forEach { it.localStatus = NodeStatus.VISIBLE }
        (3..5).collect { courses[it] }.forEach { it.localStatus = NodeStatus.INVISIBLE }
        (6..9).collect { courses[it] }.forEach { it.localStatus = NodeStatus.TRASH }
        courses.each { it.save(failOnError: true, flush: true) }

        when: "there is no user authenticated"
        if (authenticateAs) {
            UserData.authenticateAsUser(users[authenticateAs].user)
        }

        and: "we attempt to retrieve the courses"
        def active = courseManagementService.getActiveCourses()

        then: "we retrieve nothing"
        active.success == success

        and: "the data is returned correctly"
        !success || active.result.collect { it.name }.toSet() == expectedActive.collect { it.name }.toSet()
        where:
        authenticateAs | success | activeResults
        "teacher"      | true    | [0, 1, 2, 3, 4, 5]
        "teacher2"     | true    | []
        "student"      | false   | null
        null           | false   | null
    }

    @Unroll("testing creating and editing of subjects (#authenticateAs, #isEditing, #newName, #newDescription, #visible, #success)")
    def "test creating and editing of subjects"() {
        given: "some teachers"
        def users = [:]
        users.teacher = UserData.buildTestTeacher("Teacher")
        users.teacher2 = UserData.buildTestTeacher("Teacher2")

        and: "a student"
        users.student = UserData.buildStudent()

        and: "a course"
        def course = CourseData.buildTestCourse("Course", users.teacher, true)

        and: "an existing subject"
        def subject = SubjectData.buildTestSubject("Subject", course, false, !isEditing)

        and: "apply changes to subject"
        subject.name = newName
        subject.description = newDescription

        and: "an update command"
        def command = new SubjectCRUDCommand(domain: subject, isEditing: isEditing, visible: visible)

        when: "we possibly authenticate"
        if (authenticateAs) {
            UserData.authenticateAsUser(users[authenticateAs].user)
        }

        and: "we call the update"
        def result = courseManagementService.createOrEditSubject(course, command)

        then: "the success matches our expectations"
        result.success == success

        and: "if successful, our new subject is saved"
        !success || (success && Subject.get(result.result.id) == subject)

        and: "the fields matches the new fields"
        !success || (success && result.result.name == newName &&
                (newDescription != null && result.result.description == newDescription) || (newDescription == null &&
                result.result.description == Subject.DEFAULT_DESCRIPTION))

        and: "the visibility matches"
        !success || (success && result.result.localStatus == (visible ? NodeStatus.VISIBLE : NodeStatus.INVISIBLE))

        where:
        authenticateAs | isEditing | newName | newDescription | visible | success
        // Valid changes with and without editing
        "teacher"      | false     | "T"     | "T2"           | true    | true
        "teacher"      | false     | "T"     | "T2"           | false   | true
        "teacher"      | true      | "T"     | "T2"           | true    | true
        "teacher"      | true      | "T"     | "T2"           | false   | true
        "teacher"      | true      | "T"     | null           | false   | true
        "teacher"      | false     | "T"     | null           | false   | true
        "teacher"      | false     | "T"     | null           | false   | true
        // Invalid changes from a valid user
        "teacher"      | true      | null    | "T2"           | false   | false
        "teacher"      | true      | null    | null           | false   | false
        "teacher"      | false     | null    | "T2"           | false   | false
        "teacher"      | false     | null    | null           | false   | false
        // Valid and invalid changes from invalid users
        "teacher2"     | false     | "T"     | "T2"           | true    | false
        "teacher2"     | false     | "T"     | "T2"           | false   | false
        "student"      | false     | "T"     | "T2"           | true    | false
        "student"      | false     | "T"     | "T2"           | false   | false
        "student"      | false     | null    | null           | false   | false
        "student"      | false     | null    | "T2"           | false   | false
    }

    @Unroll("testing creating and editing of courses (#authenticateAs, #isEditing, #newName, #newDescription, #visible, #success)")
    def "test creating and editing of courses"() {
        given: "some teachers"
        def users = [:]
        users.teacher = UserData.buildTestTeacher("Teacher")
        users.teacher2 = UserData.buildTestTeacher("Teacher2")

        and: "a student"
        users.student = UserData.buildStudent()

        and: "a course"
        def course = CourseData.buildTestCourse("Course", users.teacher as Teacher, false, !isEditing)

        and: "apply changes to course"
        course.name = newName
        course.description = newDescription

        and: "an update command"
        def command = new CourseCRUDCommand(domain: course, isEditing: isEditing, visible: visible)

        when: "we possibly authenticate"
        if (authenticateAs) {
            UserData.authenticateAsUser(users[authenticateAs].user)
        }

        and: "we call the update"
        def result = courseManagementService.createOrEditCourse(command)

        then: "the success matches our expectations"
        result.success == success

        and: "if successful, our new subject is saved"
        !success || (success && Course.get(result.result.id) == course)

        and: "the fields matches the new fields"
        !success || (success && result.result.name == newName && result.result.description == newDescription)

        and: "the visibility matches"
        !success || (success && result.result.localStatus == (visible ? NodeStatus.VISIBLE : NodeStatus.INVISIBLE))

        where:
        authenticateAs | isEditing | newName | newDescription | visible | success
        // Valid changes from valid source
        "teacher"      | false     | "T"     | "T2"           | true    | true
        "teacher"      | false     | "T"     | "T2"           | false   | true

        // Invalid changes from valid source
        "teacher"      | false     | null    | "T2"           | true    | false
        "teacher"      | false     | null    | "T2"           | false   | false
        "teacher"      | false     | "T"     | null           | true    | false
        "teacher"      | false     | "T"     | null           | false   | false
        "teacher"      | false     | null    | null           | true    | false
        "teacher"      | false     | null    | null           | false   | false

        // Valid change from different valid source
        "teacher2"     | false     | null    | null           | false   | false

        // Valid and invalid changes from invalid source
        "student"      | false     | "T"     | "T2"           | true    | false
        "student"      | false     | "T"     | "T2"           | false   | false
        "student"      | false     | null    | null           | true    | false
        "student"      | false     | null    | null           | false   | false
        "teacher"      | false     | "T"     | "T2"           | true    | true
        "teacher"      | false     | "T"     | "T2"           | false   | true

        // Same tests as above, but while editing
        "teacher"      | true      | null    | "T2"           | true    | false
        "teacher"      | true      | null    | "T2"           | false   | false
        "teacher"      | true      | "T"     | null           | true    | false
        "teacher"      | true      | "T"     | null           | false   | false
        "teacher"      | true      | null    | null           | true    | false
        "teacher"      | true      | null    | null           | false   | false
        "teacher2"     | true      | null    | null           | false   | false
        "student"      | true      | "T"     | "T2"           | true    | false
        "student"      | true      | "T"     | "T2"           | false   | false
        "student"      | true      | null    | null           | true    | false
        "student"      | true      | null    | null           | false   | false
    }


    @Unroll("testing creating and editing of videos(#authenticateAs, #isEditing, #newName, #newDescription, #visible, #addComments, #move, #success)")
    def "test creating and editing of videos"() {
        given: "some teachers"
        def users = [:]
        users.teacher = UserData.buildTestTeacher("Teacher")
        users.teacher2 = UserData.buildTestTeacher("Teacher2")

        and: "a student"
        users.student = UserData.buildStudent()

        and: "a course"
        def course = CourseData.buildTestCourse("Course", users.teacher as Teacher)

        and: "two subjects"
        def subject1 = SubjectData.buildTestSubject("Subject1", course)
        def subject2 = SubjectData.buildTestSubject("Subject2", course)

        and: "a video"
        def video = VideoData.buildTestVideo("Video", subject1, false, isEditing)

        and: "apply changes to course"
        video.name = newName
        video.description = newDescription

        and: "an update command"
        def command = new CreateOrUpdateVideoCommand(
                subject: (move) ? subject2 : subject1,
                name: video.name,
                youtubeId: video.youtubeId,
                timelineJson: video.timelineJson,
                description: video.description,
                isEditing: isEditing,
                editing: (isEditing) ? video : null,
                videoType: video.videoType,
                visible: visible
        )

        and: "possibly comments"
        if (isEditing && addComments) {
            (0..<10).each {
                def comment = new Comment(
                        user: users.teacher.user,
                        contents: "Test comment $it",
                        dateCreated: new Date()
                ).save(failOnError: true, flush: true)

                video.addToComments(comment)
            }
            video.save(failOnError: true, flush: true)
            video = Video.get(video.id)
        }

        when: "we haven't called the command"
        then: "the video might have comments"
        !addComments || (addComments && video.comments?.size() == 10)

        when: "we possibly authenticate"
        if (authenticateAs) {
            UserData.authenticateAsUser(users[authenticateAs].user)
        }

        and: "we call the update"
        def result = courseManagementService.createOrEditVideo(command)

        then: "the success matches our expectations"
        result.success == success

        and: "the fields matches the new fields"
        !success || (success && result.result.name == newName && result.result.description == newDescription)

        and: "the visibility matches"
        !success || (success && result.result.localStatus == (visible ? NodeStatus.VISIBLE : NodeStatus.INVISIBLE))

        and: "the video still has comments attached"
        !success || !addComments || (success && addComments && video.comments?.size() == 10)

        where:
        authenticateAs | isEditing | newName | newDescription | visible | addComments | move  | success
        "teacher"      | false     | "T"     | "T2"           | false   | false       | false | true
        "teacher"      | true      | "T"     | "T2"           | false   | false       | false | true
        "teacher"      | true      | "T"     | "T2"           | false   | true        | false | true
        "teacher"      | true      | "T"     | "T2"           | true    | false       | false | true
        "teacher"      | true      | "T"     | "T2"           | true    | true        | true  | true
        "teacher"      | true      | "T"     | "T2"           | true    | false       | true  | true
        "teacher"      | true      | "T"     | "T2"           | true    | true        | false | true
        "student"      | false     | "T"     | "T2"           | false   | false       | false | false
        "student"      | true      | "T"     | "T2"           | false   | false       | false | false
        "student"      | true      | "T"     | "T2"           | true    | false       | false | false
        "student"      | true      | "T"     | "T2"           | true    | true        | false | false
        "teacher2"     | true      | "T"     | "T2"           | true    | true        | false | false
        "teacher2"     | false     | "T"     | "T2"           | true    | false       | false | false
    }

}
