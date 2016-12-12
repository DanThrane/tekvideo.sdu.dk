package dk.sdu.tekvideo

import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityUtils

import static dk.sdu.tekvideo.ServiceResult.fail
import static dk.sdu.tekvideo.ServiceResult.ok

class VideoService {
    def springSecurityService
    def videoStatisticsService
    def externalVideoHostService

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
        } catch (Exception ignored) {
            // The timeline may be corrupted in many ways. Better to be safe than sorry in this case.
            ignored.printStackTrace()
        }

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

        def videoIds = SubjectExercise.findAllBySubjectInList(subjects).exercise.id
        Video.findAllByIdInListAndLocalStatus(videoIds, NodeStatus.VISIBLE, [max: 25, sort: "dateCreated", order: "desc"])
    }

    String getThumbnail(Video video) {
        if (video != null && video.videoType) {
            return "http://img.youtube.com/vi/${video.youtubeId}/hqdefault.jpg"
        }
        return null
    }
}
