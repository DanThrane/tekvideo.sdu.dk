package dk.sdu.tekvideo

/**
 * Provides services related to users (e.g. logged in or any arbitrary user)
 */
class UserService {
    def springSecurityService

    User getAuthenticatedUser() {
        return (User) springSecurityService.currentUser
    }

    Teacher getAuthenticatedTeacher() {
        def user = (User) springSecurityService.currentUser
        return Teacher.findByUser(user)
    }
}
