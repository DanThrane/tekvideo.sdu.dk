package dk.sdu.tekvideo.data

import dk.sdu.tekvideo.v2.Subject2
import dk.sdu.tekvideo.v2.Video2

import java.util.concurrent.atomic.AtomicInteger

class VideoData {
    private static AtomicInteger idx = new AtomicInteger(0)

    static Video2 buildTestVideo(String prefix = "Video", Subject2 subject = null, boolean includeIdSuffix = false) {
        if (subject == null) subject = SubjectData.buildTestSubject()

        String name = prefix
        if (includeIdSuffix) name += idx.getAndIncrement()

        def video = new Video2([
                name     : name,
                youtubeId: "f2J9N7wgYas",
                subject  : subject
        ])
        video.save(failOnError: true, flush: true)
    }
}
