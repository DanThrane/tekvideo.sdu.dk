package dk.sdu.tekvideo

class Video {
    String name
    String youtubeId
    String timelineJson
    /**
     * Indicates the placement of this video in the subject's series
     */
    Integer weight

    static belongsTo = [subject: Subject]
    static constraints = {
        name            nullable: false, blank: false
        youtubeId       nullable: false, blank: false
        weight          validator: { obj, val, errors ->
            def video = Video.where { subject == val.subject && weight == val.weight && id != val.id }.find()
            if (video != null) {
                errors.reject("Weight must be unique for the subject!")
                return false
            }
            return true
        }
        timelineJson    blank: false
    }

    static mapping = {
        timelineJson type: "text"
    }

    Video getNextVideo() {
        def videos = Video.findAllByWeightGreaterThanAndSubject(weight, subject, [sort: "weight", max: 1, order: "asc"])
        videos ? videos.first() : null
    }
}
