package dk.sdu.tekvideo.data

import dk.sdu.tekvideo.Role
import dk.sdu.tekvideo.Student
import dk.sdu.tekvideo.Teacher
import dk.sdu.tekvideo.User
import dk.sdu.tekvideo.UserRole
import grails.plugin.springsecurity.SpringSecurityUtils

import java.util.concurrent.atomic.AtomicInteger

class UserData {
    private static AtomicInteger idx = new AtomicInteger(0)

    static Teacher buildTestTeacher(String name = "Teacher") {
        def teacherRole = Role.findByAuthority("ROLE_TEACHER") ?: new Role(authority: "ROLE_TEACHER")
                .save(flush: true, failOnError: true)
        def teacherUser = new User(username: name, password: "password").save(flush: true, failOnError: true)
        def teacher = new Teacher(user: teacherUser, alias: "teach").save(flush: true, failOnError: true)
        UserRole.create teacherUser, teacherRole, true
        return teacher
    }

    static void authenticateAsUser(User user) {
        SpringSecurityUtils.reauthenticate(user.username, null)
    }

    static void authenticateAsTestTeacher(Teacher teacher = null) {
        if (teacher == null) teacher = Teacher.findAll()[0]
        SpringSecurityUtils.reauthenticate(teacher.user.username, null)
    }

    static User buildUser(Map attrs) {
        new User(
                username: attrs.username ?: (attrs.prefix ?: "User") + "_" + idx.incrementAndGet(),
                password: attrs.password ?: "password",
                realName: attrs.realName ?: "Real name",
                elearnId: attrs.elearnid ?: "E-learn",
                email:  attrs.email ?: "E-mail"
        ).save(flush: true, failOnError: true)
    }

    static Student buildStudent(String prefix = "Student") {
        new Student(
                user: buildUser(prefix: prefix)
        ).save(flush: true, failOnError: true)
    }
}
