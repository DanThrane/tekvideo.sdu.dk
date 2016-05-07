package dk.sdu.tekvideo

class Course implements Node {
    String name
    String fullName
    String description
    Boolean spring
    Integer year

    NodeStatus localStatus = NodeStatus.VISIBLE

    static belongsTo = [teacher: Teacher]

    static constraints = {
        name nullable: false, blank: false, unique: ["teacher", "spring", "year"]
        fullName nullable: false, blank: false
        year min: 1900, max: 9999 // Lets assume we don't have anything outside of this range
    }

    static mapping = {
        description type: "text"
    }

    List<Subject> getActiveSubjects() {
        subjects.findAll { it != null && it.localStatus != NodeStatus.TRASH }
    }

    List<Subject> getVisibleSubjects() {
        subjects.findAll { it != null && it.localStatus == NodeStatus.VISIBLE }
    }

    List<Subject> getSubjects() {
        def join = CourseSubject.findAllByCourse(this)
        Subject.findAllByIdInList(join.subject.id, [sort: 'weight']) // TODO a bit worried this might cause load of all subjects
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
