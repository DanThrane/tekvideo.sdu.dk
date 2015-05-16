package dk.sdu.tekvideo

class UserTagLib {
    static namespace = "sdu"
    def springSecurityService

    def username = { attrs, body ->
        out << springSecurityService.currentUser.username
    }

}
