package dk.sdu.tekvideo

import grails.converters.JSON
import org.springframework.security.access.annotation.Secured

/**
 * Controls actions for the account management interface. This allows a user to change basic attributes about
 * themselves, such as updating their passwords. The services used by this controller are provided in
 * {@link AccountManagementService}.
 */
class AccountManagementController {
    def accountManagementService
    def springSecurityService

    static allowedMethods = [updatePassword: "POST", updateElearn: "POST", updateUser: "POST"]

    @Secured(["ROLE_TEACHER", "ROLE_STUDENT"])
    def index() {
        [user: springSecurityService.currentUser as User]
    }

    /**
     * Receives an update e-learn command. The user will be redirected back to the index page, and receive a
     * notification indiciating if the operation was successful.
     *
     * @param command    The update command
     */
    @Secured(["ROLE_TEACHER", "ROLE_STUDENT"])
    def updateElearn(UpdateElearnCommand command) {
        def result = accountManagementService.updateElearn(command)
        if (result.success) {
            flash.success = result.message
            redirect action: "index"
        } else {
            flash.error = result.message
            render view: "index", model: [user: springSecurityService.currentUser as User]
        }
    }

    /**
     * Receives an update password command. The user will be redirected back to the index page, and receive a
     * notification indiciating if the operation was successful.
     *
     * @param command    The update command
     */
    @Secured(["ROLE_TEACHER", "ROLE_STUDENT"])
    def updatePassword(UpdatePasswordCommand command) {
        def result = accountManagementService.updatePassword(command)
        if (result.success) {
            flash.success = result.message
            redirect action: "index"
        } else {
            flash.error = result.message
            render view: "index", model: [user: springSecurityService.currentUser as User]
        }
    }

    /**
     * Lists all users to a teacher.
     *
     * TODO More fine-grained permission management
     */
    @Secured("ROLE_TEACHER")
    def manage() {
        []
    }

    @Secured("ROLE_TEACHER")
    def users() {
        def result = accountManagementService.retrieveUserData()
        response.status = result.suggestedHttpStatus
        render result as JSON
    }

    @Secured("ROLE_TEACHER")
    def updateUser(UpdateUserCommand command) {
        def result = accountManagementService.updateUser(command)
        response.status = result.suggestedHttpStatus
        render result as JSON
    }
}
