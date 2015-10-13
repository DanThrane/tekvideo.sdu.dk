package dk.sdu.tekvideo

import java.util.stream.Collectors

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
