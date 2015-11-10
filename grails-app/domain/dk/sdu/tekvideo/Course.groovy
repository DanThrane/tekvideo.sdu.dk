package dk.sdu.tekvideo

import java.util.stream.Collectors

class Course implements Node {
    String name
    String fullName
    String description
    Boolean spring
    Integer year

    NodeStatus localStatus = NodeStatus.VISIBLE

    List<Subject> subjects // ordered
    static belongsTo = [teacher: Teacher]
    static hasMany = [subjects: Subject]

    static constraints = {
        name nullable: false, blank: false, unique: ["teacher", "spring", "year"]
        fullName nullable: false, blank: false
        year min: 1900, max: 9999 // Lets assume we don't have anything outside of this range
    }

    static mapping = {
        description type: "text"
        subjects cascade: "all-delete-orphan"
    }

    List<Subject> getActiveSubjects() {
        subjects.stream().filter { it.localStatus != NodeStatus.TRASH }.collect(Collectors.toList())
    }

    List<Subject> getVisibleSubjects() {
        subjects.stream().filter { it.localStatus == NodeStatus.VISIBLE }.collect(Collectors.toList())
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
