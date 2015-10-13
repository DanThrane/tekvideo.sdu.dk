package dk.sdu.tekvideo

class Course implements Node {
    String name
    String fullName
    String description
    Semester semester
    NodeStatus localStatus = NodeStatus.VISIBLE

    List<Subject> subjects // ordered
    static belongsTo = [teacher: Teacher]
    static hasMany = [subjects: Subject]

    static constraints = {
        name nullable: false, blank: false
        fullName nullable: false, blank: false
    }

    static mapping = {
        description type: "text"
        subjects cascade: "all-delete-orphan"
    }

    @Override
    String toString() {
        return name
    }

    @Override
    Node getParent() {
        null
    }
}
