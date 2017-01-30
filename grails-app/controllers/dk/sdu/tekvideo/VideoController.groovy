package dk.sdu.tekvideo

import org.springframework.security.access.annotation.Secured

class VideoController {
    def nodeService

    @Secured("permitAll")
    def viewV(Video video) {
        if (nodeService.canView(video)) {
            render view: "view", model: [video: video]
        } else {
            render status: 404, text: "Unable to find video!"
        }
    }

}
