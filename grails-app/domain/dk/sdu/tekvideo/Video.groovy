package dk.sdu.tekvideo

class Video implements Node {
    String name
    String youtubeId
    String timelineJson
    String description = "Ingen beskrivelse"
    Boolean videoType = true
    Date dateCreated
    NodeStatus localStatus = NodeStatus.VISIBLE
    Long videos_idx

    static constraints = {
        name            nullable: false, blank: false
        youtubeId       nullable: false, blank: false
        timelineJson    nullable: true
        description     nullable: true
    }

    static belongsTo = [subject: Subject]
    static hasMany = [comments: Comment]

    static mapping = {
        timelineJson type: "text"
        videoType defaultValue: true
        subject nullable: true
        videos_idx updateable: false, insertable: false
    }

    String getDescription() {
        if (description == null) return "Ingen beskrivelse"
        return description
    }

    @Override
    Node getParent() {
        subject
    }

}
