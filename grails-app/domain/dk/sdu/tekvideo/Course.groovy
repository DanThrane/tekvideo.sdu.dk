package dk.sdu.tekvideo

class Course {
    String name
    String description

    static hasMany = [subjects: Subject]

    static constraints = {
        name nullable: false, blank: false
    }

    static mapping = {
        description type: "text"
    }
}
