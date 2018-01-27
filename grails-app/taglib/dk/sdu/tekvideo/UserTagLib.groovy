package dk.sdu.tekvideo

class UserTagLib {
    static namespace = "sdu"
    def springSecurityService

    def username = { attrs, body ->
        out << springSecurityService.currentUser.username
    }

    def userEmail = { attrs, body ->
        User user = springSecurityService.currentUser
        String mail = user ? user.email : null
        out << (mail ?: "nomail@example.com")
    }

}
