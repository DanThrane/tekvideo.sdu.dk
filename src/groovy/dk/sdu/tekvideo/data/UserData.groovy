package dk.sdu.tekvideo.data

import dk.sdu.tekvideo.Role
import dk.sdu.tekvideo.Teacher
import dk.sdu.tekvideo.User
import dk.sdu.tekvideo.UserRole

class UserData {
    static Teacher buildTestTeacher() {
        def teacherRole = new Role(authority: "ROLE_TEACHER").save(flush: true, failOnError: true)
        def teacherUser = new User(username: "Teacher", password: "password").save(flush: true, failOnError: true)
        def teacher = new Teacher(user: teacherUser, alias: "teach").save(flush: true, failOnError: true)
        UserRole.create teacherUser, teacherRole, true
        return teacher
    }
}
