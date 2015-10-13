package dk.sdu.tekvideo

import grails.converters.JSON
import org.springframework.security.access.annotation.Secured

class VideoController {
    TeachingService teachingService
    VideoService videoService

    @Secured("permitAll")
    def view(String teacherName, String courseName, String subjectName, Integer videoId) {
        Video video = teachingService.getVideo(teacherName, courseName, subjectName, videoId)
        if (videoService.canAccess(video)) {
            [video: video]
        } else {
            render status: 404, text: "Unable to find video!"
        }
    }

    @Secured("permitAll")
    def viewV(Video video) {
        if (videoService.canAccess(video)) {
            render view: "view", model: [video: video]
        } else {
            render status: 404, text: "Unable to find video!"
        }
    }

    @Secured(["ROLE_STUDENT", "ROLE_TEACHER"])
    def postComment(Video video, String comment) {
        def result = videoService.createComment(new CreateVideoCommentCommand(id: video, comment: comment))
        if (result.success) {
            flash.success = "Din kommentar er blevet tilføjet til videoen"
        } else {
            flash.error = "Der skete en fejl!"
        }
        redirect action: "viewV", id: video.id
    }

    @Secured("ROLE_TEACHER")
    def deleteComment(Video video, Long comment) {
        println comment
        def result = videoService.deleteComment(video, Comment.get(comment))
        render result as JSON
    }

}
