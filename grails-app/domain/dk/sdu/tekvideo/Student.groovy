package dk.sdu.tekvideo

class Student {
    User user

    static constraints = {
    }

    static jsonMarshaller = { Student it ->
        [
                id      : it.id,
                username: it.user.username
        ]
    }

    def beforeDelete() {
        CourseStudent.withNewSession {
            def join = CourseStudent.findAllByStudent(this)
            join.each { it.delete(flush: true) }
        }
    }

}
