import dk.sdu.ivids.Role
import dk.sdu.ivids.User
import dk.sdu.ivids.UserRole

class BootStrap {

    def init = { servletContext ->
        def instructorRole = new Role(authority: "ROLE_INSTRUCTOR").save(flush: true)
        def taRole = new Role(authority: "ROLE_TA").save(flush: true)
        def studentRole = new Role(authority: "ROLE_STUDENT").save(flush: true)

        def instructorUser = new User(username: "Instructor", password: "password").save(flush: true)
        def taUser = new User(username: "TA", password: "password").save(flush: true)
        def studentUser = new User(username: "Student", password: "password").save(flush: true)

        UserRole.create instructorUser, instructorRole, true
        UserRole.create taUser, taRole, true
        UserRole.create studentUser, studentRole, true
    }

    def destroy = {
    }
}
