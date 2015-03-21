package dk.sdu.tekvideo

import dk.sdu.tekvideo.Video

class VideoController {

    def view(Video video) {
        [video: video]
    }

}
