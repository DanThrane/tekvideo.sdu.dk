package dk.sdu.ivids

class Video {
    String name
    String youtubeId
    Timeline timeline
    /**
     * Indicates the placement of this video in the subject's series
     */
    Integer weight

    static belongsTo = [subject: Subject]
    static constraints = {
        name nullable: false, blank: false
        youtubeId nullable: false, blank: false
        timeline nullable: false
        // weight should be unique for the subject. This might require a custom validator
    }

    Video getNextVideo() {
        def videos = Video.findAllByWeightGreaterThanAndSubject(weight, subject, [sort: "weight", max: 1, order: "asc"])
        videos ? videos.first() : null
    }
}
