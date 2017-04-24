package dk.sdu.tekvideo

class Video extends Exercise {
    String youtubeId
    String timelineJson
    Boolean videoType = true

    static constraints = {
        name            nullable: false, blank: false, maxSize: 255
        youtubeId       nullable: false, blank: false
        timelineJson    nullable: true
        description     nullable: true
    }

    static mapping = {
        timelineJson type: "text"
        videoType defaultValue: true
    }

    static jsonMarshaller = { Video it ->
        [
                id          : it.id,
                name        : it.name,
                youtubeId   : it.youtubeId,
                timelineJson: it.timelineJson,
                description : it.description,
                videoType   : it.videoType,
                dateCreated : it.dateCreated,
        ]
    }

    String getTimelineJson() {
        if (timelineJson == null) return "[]"
        return timelineJson
    }

    @Override
    Node getParent() {
        SubjectExercise.findByExercise(this).subject
    }

    @Override
    int getScoreToPass() {
        return 1
    }
}
