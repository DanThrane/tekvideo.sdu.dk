package dk.sdu.tekvideo

class Teacher {
    User user
    String alias

    static hasMany = [courses: Course]
    static constraints = {
        alias nullable: true
    }

    @Override
    String toString() {
        if (alias != null) return alias
        return user.username
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        Teacher teacher = (Teacher) o

        if (id != teacher.id) return false

        return true
    }

    int hashCode() {
        return (id != null ? id.hashCode() : 0)
    }
}
