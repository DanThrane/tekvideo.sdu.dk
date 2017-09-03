package dk.sdu.tekvideo

/**
 * Provides services related to users (e.g. logged in or any arbitrary user)
 */
class UserService {
    def springSecurityService

    Teacher getAuthenticatedTeacher() {
        def user = (User) springSecurityService.currentUser
        return Teacher.findByUser(user)
    }
}
