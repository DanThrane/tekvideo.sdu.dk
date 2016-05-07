package dk.sdu.tekvideo

class SubjectVideo {
    Subject subject
    Video video
    Integer weight

    static SubjectVideo create(Subject subject, Video video, Map params) {
        Integer weight = params.weight
        Boolean save = params.save ?: false
        Boolean flush = params.flush ?: true
        Boolean failOnError = params.failOnError ?: false

        if (weight == null) {
            weight = countBySubject(subject)
        }

        def result = new SubjectVideo(subject: subject, video: video, weight: weight)
        if (save) {
            result.save(flush: flush, failOnError: failOnError)
        }
        return result
    }

    static List<Long> findAllVideoIds(Subject subject) {
        findAllBySubject(subject, [sort: 'weight']).video.id
    }

    static constraints = {
        subject nullable: false
        video nullable: false
        weight nullable: false, min: 0
    }
}
