package dk.sdu.tekvideo

class Subject {
    String name

    List videos // The subject will order its own videos
    static hasMany = [videos: Video]

    static constraints = {
        name nullable: false, blank: false
    }

    static belongsTo = Course
}
