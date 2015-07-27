package dk.sdu.tekvideo

class Subject {
    String name
    String description = "Ingen beskrivelse"

    List<Video> videos // The subject will order its own videos
    static hasMany = [videos: Video]

    static constraints = {
        name nullable: false, blank: false
        description nullable: true
    }

    static mapping = {
        description type: "text"
    }

    static belongsTo = [course: Course]

    String getDescription() {
        if (description == null) return "Ingen beskrivelse"
        return description
    }

    @Override
    String toString() {
        return name
    }
}
