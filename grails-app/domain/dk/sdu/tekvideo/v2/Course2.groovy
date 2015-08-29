package dk.sdu.tekvideo.v2

import dk.sdu.tekvideo.Semester
import dk.sdu.tekvideo.Teacher

class Course2 {
    String name
    String fullName
    String description
    Semester semester

    List<Subject2> subjects // ordered
    static belongsTo = [teacher: Teacher]
    static hasMany = [subjects: Subject2]

    static constraints = {
        name nullable: false, blank: false
        fullName nullable: false, blank: false
    }

    static mapping = {
        description type: "text"
    }

    @Override
    String toString() {
        return name
    }
}
