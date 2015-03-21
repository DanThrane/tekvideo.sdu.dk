package dk.sdu.tekvideo

class UserTagLib {
    static namespace = "tek"
    def springSecurityService

    def username = { attrs, body ->
        out << springSecurityService.currentUser.username
    }

}
