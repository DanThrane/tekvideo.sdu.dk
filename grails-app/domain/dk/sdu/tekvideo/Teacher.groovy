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
}
