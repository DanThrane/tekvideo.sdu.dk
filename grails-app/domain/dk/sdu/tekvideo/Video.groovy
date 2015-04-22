package dk.sdu.tekvideo

class Video {
    String name
    String youtubeId
    String timelineJson
    Date dateCreated

    static constraints = {
        name            nullable: false, blank: false
        youtubeId       nullable: false, blank: false
        timelineJson    blank: false
    }

    static hasMany = [subjects: Subject]
    static belongsTo = Subject

    static mapping = {
        timelineJson type: "text"
    }

}
