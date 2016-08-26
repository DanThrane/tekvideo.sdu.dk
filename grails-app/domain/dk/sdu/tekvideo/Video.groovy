package dk.sdu.tekvideo

import dk.sdu.tekvideo.events.AnswerQuestionEvent

class Video implements Node {
    String name
    String youtubeId
    String timelineJson
    String description = "Ingen beskrivelse"
    Boolean videoType = true
    Date dateCreated
    NodeStatus localStatus = NodeStatus.VISIBLE

    static constraints = {
        name            nullable: false, blank: false, maxSize: 255
        youtubeId       nullable: false, blank: false
        timelineJson    nullable: true
        description     nullable: true
    }

    static hasMany = [comments: Comment]

    static mapping = {
        timelineJson type: "text"
        description type: "text"
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

    String getDescription() {
        if (description == null) return "Ingen beskrivelse"
        return description
    }

    String getTimelineJson() {
        if (timelineJson == null) return "[]"
        return timelineJson
    }

    Subject getSubject() {
        return getParent() as Subject
    }

    @Override
    Node getParent() {
        SubjectVideo.findByVideo(this).subject
    }

    @Override
    NodeIdentifier getIdentifier() {
        return new NodeIdentifier("video", id)
    }

}
