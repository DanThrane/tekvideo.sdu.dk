package dk.sdu.tekvideo.data

import dk.sdu.tekvideo.Role
import dk.sdu.tekvideo.Teacher
import dk.sdu.tekvideo.User
import dk.sdu.tekvideo.UserRole
import grails.plugin.springsecurity.SpringSecurityUtils

class UserData {
    static Teacher buildTestTeacher(String name = "Teacher") {
        def teacherRole = Role.findByAuthority("ROLE_TEACHER") ?: new Role(authority: "ROLE_TEACHER")
                .save(flush: true, failOnError: true)
        def teacherUser = new User(username: name, password: "password").save(flush: true, failOnError: true)
        def teacher = new Teacher(user: teacherUser, alias: "teach").save(flush: true, failOnError: true)
        UserRole.create teacherUser, teacherRole, true
        return teacher
    }

    static void authenticateAsTestTeacher(Teacher teacher = null) {
        if (teacher == null) teacher = Teacher.findAll()[0]
        SpringSecurityUtils.reauthenticate(teacher.user.username, null)
    }
}
