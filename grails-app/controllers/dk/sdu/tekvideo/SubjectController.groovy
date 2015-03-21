package dk.sdu.tekvideo

import dk.sdu.tekvideo.Subject

class SubjectController {

    def view(Subject subject) {
        def videos = subject.videos.sort(false, Comparator.comparingInt { it.weight })
        [subject: subject, videos: videos]
    }
}
