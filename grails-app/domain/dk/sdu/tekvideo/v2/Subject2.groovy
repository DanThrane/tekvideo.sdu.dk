package dk.sdu.tekvideo.v2

class Subject2 {
    String name
    String description = "Ingen beskrivelse"

    List<Video2> videos // The subject will order its own videos
    static hasMany = [videos: Video2]

    static constraints = {
        name nullable: false, blank: false
        description nullable: true
    }

    static mapping = {
        description type: "text"
    }

    static belongsTo = [course: Course2]

    String getDescription() {
        if (description == null) return "Ingen beskrivelse"
        return description
    }

    @Override
    String toString() {
        return name
    }
}
