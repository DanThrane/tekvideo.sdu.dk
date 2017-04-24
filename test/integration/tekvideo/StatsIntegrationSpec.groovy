package tekvideo

import dk.sdu.tekvideo.Video
import dk.sdu.tekvideo.WrittenExerciseGroup
import dk.sdu.tekvideo.data.EventData
import dk.sdu.tekvideo.data.SubjectData
import dk.sdu.tekvideo.data.UserData
import dk.sdu.tekvideo.data.VideoData
import dk.sdu.tekvideo.data.WrittenExerciseData
import dk.sdu.tekvideo.stats.ProgressionStatus
import spock.lang.Specification

class StatsIntegrationSpec extends Specification {
    def statsEventService
    def statsService

    def "test course progress only guest users"() {
        given: "some content"
        def N = 5
        def subject = SubjectData.buildTestSubject()
        def course = subject.course
        def teacher = course.teacher
        def videos = (0..<N).collect { VideoData.buildTestVideo("Video", subject, true, true) }
        def written = (0..<N).collect { WrittenExerciseData.buildTestExercise("Written", subject, true) }

        and: "some events"
        def guestsIds = (0..N).collect { "guest-token-id-$it" }
        (0..<N).each {
            def id = guestsIds[it]

            if (it != N - 1) {
                statsEventService.handle EventData.visitExerciseEvent(videos[it], null, id)

                statsEventService.handle EventData.visitExerciseEvent(written[it], null, id)
                statsEventService.handle EventData.answerWrittenExercise(written[it],
                        written[it].exercises.first(), it % 2 == 1, null, id)
            }
        }

        when: "authenticated as the teacher"
        UserData.authenticateAsUser(teacher.user)

        and: "he asks for the subject progress"
        def progressResult = statsService.courseProgress(course)
        def table = progressResult.result

        then: "he got a successful result"
        progressResult.success
        table != null

        and: "the header contains every exercise"
        table.head.size() == 2 * N
        table.head.findAll { it.node instanceof Video }.size() == N
        table.head.findAll { it.node instanceof WrittenExerciseGroup }.size() == N

        and: "there is a row for every user with activity"
        table.body.size() == N - 1
        !table.body.user.collect { it.toString() }.contains(guestsIds.last())

        and: "the table matches the expected result"
        table.body.each { def entry ->
            def i = guestsIds.findIndexOf { it == entry.user.toString() }
            assert entry.cells.size() == 2 * N

            def expectedVideo = ProgressionStatus.PERFECT
            def expectedWritten = i % 2 == 1 ? ProgressionStatus.PERFECT : ProgressionStatus.STARTED_LITTLE_PROGRESS

            def visitedVideo = table.head.findIndexOf { it.node == videos[i] }
            def visitedWritten = table.head.findIndexOf { it.node == written[i] }

            assert visitedVideo != -1
            assert visitedWritten != -1

            entry.cells.eachWithIndex { def cell, def j ->
                if (j == visitedVideo) {
                    assert cell.status == expectedVideo
                } else if (j == visitedWritten) {
                    assert cell.status == expectedWritten
                } else {
                    assert cell.status == ProgressionStatus.NOT_STARTED
                }
            }
        }
    }

    def "test subject progress only guest users"() {
        given: "some content"
        def N = 5
        def subject = SubjectData.buildTestSubject()
        def course = subject.course
        def teacher = course.teacher
        def videos = (0..<N).collect { VideoData.buildTestVideo("Video", subject, true, true) }
        def written = (0..<N).collect { WrittenExerciseData.buildTestExercise("Written", subject, true) }

        and: "some events"
        def guestsIds = (0..N).collect { "guest-token-id-$it" }
        (0..<N).each {
            def id = guestsIds[it]

            if (it != N - 1) {
                statsEventService.handle EventData.visitExerciseEvent(videos[it], null, id)

                statsEventService.handle EventData.visitExerciseEvent(written[it], null, id)
                statsEventService.handle EventData.answerWrittenExercise(written[it],
                        written[it].exercises.first(), it % 2 == 1, null, id)
            }
        }

        when: "authenticated as the teacher"
        UserData.authenticateAsUser(teacher.user)

        and: "he asks for the subject progress"
        def progressResult = statsService.subjectProgress(subject)
        def table = progressResult.result

        then: "he got a successful result"
        progressResult.success
        table != null

        and: "the header contains every exercise"
        table.head.size() == 2 * N
        table.head.findAll { it.node instanceof Video }.size() == N
        table.head.findAll { it.node instanceof WrittenExerciseGroup }.size() == N

        and: "there is a row for every user with activity"
        table.body.size() == N - 1
        !table.body.user.collect { it.toString() }.contains(guestsIds.last())

        and: "the table matches the expected result"
        table.body.each { def entry ->
            def i = guestsIds.findIndexOf { it == entry.user.toString() }
            assert entry.cells.size() == 2 * N

            def expectedVideo = ProgressionStatus.PERFECT
            def expectedWritten = i % 2 == 1 ? ProgressionStatus.PERFECT : ProgressionStatus.STARTED_LITTLE_PROGRESS

            def visitedVideo = table.head.findIndexOf { it.node == videos[i] }
            def visitedWritten = table.head.findIndexOf { it.node == written[i] }

            assert visitedVideo != -1
            assert visitedWritten != -1

            entry.cells.eachWithIndex { def cell, def j ->
                if (j == visitedVideo) {
                    assert cell.status == expectedVideo
                } else if (j == visitedWritten) {
                    assert cell.status == expectedWritten
                } else {
                    assert cell.status == ProgressionStatus.NOT_STARTED
                }
            }
        }
    }

    def "test subject progress with no activity"() {
        given: "some content"
        def N = 5
        def subject = SubjectData.buildTestSubject()
        def course = subject.course
        def teacher = course.teacher
        def videos = (0..<N).collect { VideoData.buildTestVideo("Video", subject, true, true) }
        def written = (0..<N).collect { WrittenExerciseData.buildTestExercise("Written", subject, true) }

        when: "authenticated as the teacher"
        UserData.authenticateAsUser(teacher.user)

        and: "he asks for the subject progress"
        def progressResult = statsService.subjectProgress(subject)
        def table = progressResult.result

        then: "he got a successful result"
        progressResult.success
        table != null

        and: "the header contains every exercise"
        table.head.size() == 2 * N
        table.head.findAll { it.node instanceof Video }.size() == N
        table.head.findAll { it.node instanceof WrittenExerciseGroup }.size() == N

        and: "there are no rows"
        table.body.size() == 0
    }

    def "test subject progress with no exercises"() {
        given: "some content"
        def subject = SubjectData.buildTestSubject()
        def course = subject.course
        def teacher = course.teacher

        when: "authenticated as the teacher"
        UserData.authenticateAsUser(teacher.user)

        and: "he asks for the subject progress"
        def progressResult = statsService.subjectProgress(subject)
        def table = progressResult.result

        then: "he got a successful result"
        progressResult.success
        table != null

        and: "the header contains every exercise"
        table.head.size() == 0

        and: "there are no rows"
        table.body.size() == 0
    }
}
