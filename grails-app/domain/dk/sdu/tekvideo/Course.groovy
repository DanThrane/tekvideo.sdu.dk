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
        Collections.unmodifiableList(CourseSubject.findAllByCourse(this, [sort: 'weight']).subject)
    }

    @Override
    String toString() {
        return name
    }

    @Override
    Node getParent() {
        null
    }

    @Override
    NodeIdentifier getIdentifier() {
        return new NodeIdentifier("course", id, name)
    }

    def beforeDelete() {
        CourseStudent.withNewSession {
            def join = CourseStudent.findAllByCourse(this)
            join.each { it.delete(flush: true) }
        }
    }
}
