package dk.sdu.tekvideo

import org.springframework.security.access.annotation.Secured

class VideoController {
    ExerciseService exerciseService

    @Secured("permitAll")
    def viewV(Video video) {
        if (exerciseService.canAccess(video)) {
            render view: "view", model: [video: video]
        } else {
            render status: 404, text: "Unable to find video!"
        }
    }

}
