package dk.sdu.tekvideo

class Course {
    String name
    String description

    List subjects // ordered
    static belongsTo = [teacher: Teacher]
    static hasMany = [subjects: Subject]

    static constraints = {
        name nullable: false, blank: false
    }

    static mapping = {
        description type: "text"
    }
}
