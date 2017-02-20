package dk.sdu.tekvideo.stats

import dk.sdu.tekvideo.User
import dk.sdu.tekvideo.Video

class VideoProgress {
    Video video
    User user
    String uuid

    static constraints = {
        user nullable: true
        uuid nullable: true
    }

    static mapping = {
        version false
    }
}
