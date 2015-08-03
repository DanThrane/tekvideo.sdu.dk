package dk.sdu.tekvideo

import org.springframework.security.access.annotation.Secured

class VideoController {

    TeachingService teachingService

    @Secured("permitAll")
    def view(String teacherName, String courseName, String subjectName, Integer videoId) {
        Video video = teachingService.getVideo(teacherName, courseName, subjectName, videoId)
        if (video) {
            [video: video]
        } else {
            render status: 404, text: "Unable to find video!"
        }
    }

    @Secured("permitAll")
    def viewV(Video video) {
        render view: "view", model: [video: video]
    }

}
