package dk.sdu.tekvideo

import java.time.LocalDateTime

class Course implements Node, Comparable<Course> {
    String name
    String fullName
    String description
    Boolean spring
    Integer year

    NodeStatus localStatus = NodeStatus.VISIBLE

    static belongsTo = [teacher: Teacher]
    static transients = ['eagerlyLoadedParent']

    static constraints = {
        name nullable: false, blank: false, unique: ["teacher", "spring", "year"]
        fullName nullable: false, blank: false
        year min: 1900, max: 9999 // Lets assume we don't have anything outside of this range
    }

    static mapping = {
        description type: "text"
    }

    List<Subject> getActiveSubjects() {
        List<Subject> result = Subject.executeQuery("""
            SELECT cs.subject
            FROM CourseSubject cs
            WHERE cs.course = :course AND cs.subject.localStatus != 'TRASH'
            ORDER BY cs.weight
        """, [course: this])
        result.each { it.eagerlyLoadedParent = this }
        return result
    }

    List<Subject> getVisibleSubjects() {
        List<Subject> result = Subject.executeQuery("""
            SELECT cs.subject
            FROM CourseSubject cs
            WHERE cs.course = :course AND cs.subject.localStatus = 'VISIBLE'
            ORDER BY cs.weight
        """, [course: this])
        result.each { it.eagerlyLoadedParent = this }
        return result
    }

    List<Subject> getSubjects() {
        List<Subject> result = Subject.executeQuery("""
            SELECT cs.subject
            FROM CourseSubject cs
            WHERE cs.course = :course
            ORDER BY cs.weight
        """, [course: this])
        result.each { it.eagerlyLoadedParent = this }
        return result
    }

    @Override
    String toString() {
        return name
    }

    @Override
    Node loadParent() {
        return null
    }

    def beforeDelete() {
        CourseStudent.withNewSession {
            def join = CourseStudent.findAllByCourse(this)
            join.each { it.delete(flush: true) }
        }
    }

    String getShortWhen() {
        String season = spring ? "F" : "E"
        return "$season $year"
    }

    @Override
    int compareTo(Course other) {
        def whenThis = whenIsCourseActiveToWeight()
        def whenOther = other.whenIsCourseActiveToWeight()

        def whenCompare = whenThis <=> whenOther
        if (whenCompare != 0) return whenCompare

        return fullName <=> other.fullName
    }

    int whenIsCourseActive() {
        def now = LocalDateTime.now()
        def currentlySpring = now.monthValue >= 1 && now.monthValue <= 6

        if (year > now.year) return 1
        if (year < now.year) return -1
        if (currentlySpring == spring) return 0
        if (currentlySpring && !spring) return 1
        return -1 // !currentlySpring && spring
    }

    private whenIsCourseActiveToWeight() {
        switch (whenIsCourseActive()) {
            case 0: return 1 // Present
            case 1: return 2 // Future
            case -1: return 3 // Past
            default: throw new IllegalStateException("Bad whenIsCourseActive() return value")
        }
    }
}
