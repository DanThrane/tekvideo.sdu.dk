package dk.sdu.tekvideo

class Subject {
    String name
    String description = "Ingen beskrivelse"

    List<Video> videos // The subject will order its own videos
    static hasMany = [videos: Video]

    static constraints = {
        name nullable: false, blank: false
    }

    static mapping = {
        description type: "text"
    }

    static belongsTo = [course: Course]

    @Override
    String toString() {
        return name
    }
}
