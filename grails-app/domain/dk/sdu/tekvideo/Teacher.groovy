package dk.sdu.tekvideo

class Teacher {
    User user

    static hasMany = [courses: Course]
    static constraints = {}
}
