package dk.sdu.tekvideo

import org.springframework.security.access.annotation.Secured

class AccountManagementController {
    def accountManagementService
    def springSecurityService

    static allowedMethods = [updatePassword: "POST", updateElearn: "POST"]

    @Secured(["ROLE_TEACHER", "ROLE_STUDENT"])
    def index() {
        [user: springSecurityService.currentUser as User]
    }

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

    @Secured("ROLE_TEACHER")
    def manage() {
        accountManagementService.findAccountInformation()
    }
}
