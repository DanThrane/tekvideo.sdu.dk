package dk.sdu.tekvideo.data

import dk.sdu.tekvideo.Subject
import dk.sdu.tekvideo.SubjectVideo
import dk.sdu.tekvideo.Video

import java.util.concurrent.atomic.AtomicInteger

class VideoData {
    private static AtomicInteger idx = new AtomicInteger(0)

    static Video buildTestVideo(String prefix = "Video", Subject subject = null, boolean includeIdSuffix = false,
                                boolean save = true) {
        if (subject == null) subject = SubjectData.buildTestSubject()

        String name = prefix
        if (includeIdSuffix) name += idx.getAndIncrement()

        def video = new Video([
                name        : name,
                youtubeId   : "f2J9N7wgYas",
                timelineJson: "[]"
        ])

        if (save) {
            video.save(failOnError: true, flush: true)
            SubjectVideo.create(subject, video, [save: true, failOnError: true, flush: true])
        }

        return video
    }
}
