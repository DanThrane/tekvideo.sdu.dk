package dk.sdu.tekvideo

import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityUtils

import static dk.sdu.tekvideo.ServiceResult.fail
import static dk.sdu.tekvideo.ServiceResult.ok

class VideoService {
    def springSecurityService
    def videoStatisticsService
    def userService
    def externalVideoHostService

    boolean canAccess(Video video) {
        def status = video?.status

        video != null &&
                (status == NodeStatus.VISIBLE ||
                        (status == NodeStatus.INVISIBLE &&
                                userService.authenticatedTeacher == video.subject.course.teacher))
    }

    ServiceResult<Comment> createComment(CreateVideoCommentCommand command) {
        User user = springSecurityService.currentUser as User
        if (!command.id) {
            fail "Ukendt video"
        } else if (!user) {
            fail "Ukendt bruger"
        } else {
            def comment = new Comment(contents: command.comment, user: user)
            if (!comment.validate()) {
                fail "Kommentaren er ikke gyldig"
            } else {
                if (command.id.addToComments(comment)) {
                    ok comment
                } else {
                    fail "Kunne ikke gemme kommentar"
                }
            }
        }
    }

    ServiceResult<Void> deleteComment(Video video, Comment comment) {
        if (SpringSecurityUtils.ifAllGranted("ROLE_TEACHER")) {
            if (!video) {
                fail "Ukendt video"
            } else if (!comment) {
                fail "Ukendt kommentar"
            } else {
                if (!video.removeFromComments(comment)) {
                    fail("Kunne ikke fjerne kommentar", true)
                } else {
                    ok null
                }
            }
        } else {
            fail "Du har ikke rettigheder til at slette denne kommentar!"
        }
    }

    ServiceResult<List<VideoBreakdown>> findVideoBreakdown(List<Video> videos) {
        ok videos.collect {
            new VideoBreakdown(
                    video: it,
                    commentCount: it.comments.size(),
                    viewCount: videoStatisticsService.retrieveViewBreakdown(it).result?.visits
            )
        }
    }

    VideoMetaData getVideoMetaDataSafe(Video video) {
        int subjectCount = 0
        int questionCount = 0
        int fieldCount = 0
        try {
            List parse = JSON.parse(video.timelineJson)

            subjectCount = parse.size()

            def collectedQuestions = parse.collect { it.questions.size() }
            questionCount = collectedQuestions ? collectedQuestions.sum() : 0

            def collectedFields = parse.collect {
                def collectedFields = it.questions.collect { it.fields.size() }
                return collectedFields ? collectedFields.sum() : 0
            }
            fieldCount = collectedFields ? collectedFields.sum() : 0
        } catch (Exception ignored) { }

        def duration = "00:00"
        try {
            def info = externalVideoHostService.getVideoInformation(video.youtubeId, video.videoType)

            if (info.success) duration = info.result.duration
        } catch (Exception ignored) {
            ignored.printStackTrace()
        }
        return new VideoMetaData([
                subjectCount : subjectCount,
                questionCount: questionCount,
                fieldCount   : fieldCount,
                duration     : duration
        ])
    }

    VideoTimeline getTimeline(Video video) {
        return VideoTimeline.fromVideo(video)
    }

    List<Video> findFeaturedVideos() {
        def user = springSecurityService.currentUser as User
        List<Course> courses = null
        if (user) {
            def student = Student.findByUser(user)
            if (student) {
                // TODO Most of this should be done on the DB, not on the web-server
                courses = CourseStudent.findAllByStudent(student, [max: 25])
                        .course
                        .findAll { it.status == NodeStatus.VISIBLE }
            }
        }
        if (!courses) {
            // TODO Not easy to show current information (See issue #45)
            courses = Course.findAllByLocalStatus(NodeStatus.VISIBLE, [max: 25])
        }
        def subjectIds = CourseSubject.findAllByCourseInList(courses).subject.id // TODO Performance
        def subjects = Subject.findAllByIdInListAndLocalStatus(subjectIds, NodeStatus.VISIBLE, [max: 25])

        def videoIds = SubjectVideo.findAllBySubjectInList(subjects).video.id
        Video.findAllByIdInListAndLocalStatus(videoIds, NodeStatus.VISIBLE, [max: 25, sort: "dateCreated", order: "desc"])
    }
}
