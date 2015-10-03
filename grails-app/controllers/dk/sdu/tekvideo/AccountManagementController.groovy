package dk.sdu.tekvideo

import org.springframework.security.access.annotation.Secured

class AccountManagementController {
    def accountManagementService

    @Secured("ROLE_TEACHER")
    def manage() {
        accountManagementService.findAccountInformation()
    }
}
