package dk.sdu.tekvideo

import static dk.sdu.tekvideo.ServiceResult.ok
import static dk.sdu.tekvideo.ServiceResult.fail

/**
 * Provides services for changing basic information about a user's account.
 */
class AccountManagementService {
    def springSecurityService

    /**
     * Updates the user's e-learn ID.
     *
     * @param command    The update command
     * @return A service result, indicating success or failure along with an appreciate message.
     */
    ServiceResult<Void> updateElearn(UpdateElearnCommand command) {
        def user = springSecurityService.currentUser as User
        if (user) {
            if (command.validate()) {
                user.elearnId = command.elearn
                user.save(flush: true)
                ok message: "e-learn ID opdateret successfuldt!"
            } else {
                fail message: "e-learn ID kan ikke være blankt!"
            }
        } else {
            fail message: "Ukendt bruger", suggestedHttpStatus: 401
        }
    }

    /**
     * Updates the user's password. This will validate the command, to require that the password is correctly
     * listed twice, and that the new password is valid according to the security module's constraints.
     *
     * @param command    The update command
     * @return A service result, indicating success or failure along with an appreciate message.
     */
    ServiceResult<Void> updatePassword(UpdatePasswordCommand command) {
        def user = springSecurityService.currentUser as User
        if (user) {
            command.username = user.username // The password validator expects a username property
            if (command.validate()) {
                if (command.currentPassword != null && checkPassword(user.password, command.currentPassword)) {
                    user.password = command.newPassword
                    user.save(flush: true)
                    springSecurityService.reauthenticate(user.username)
                    ok message: "Dit kodeord er blevet successfuldt opdateret!"
                } else {
                    fail "Forkert kodeord angivet"
                }
            } else {
                if (command.errors.getFieldError("newPassword")) {
                    fail "Kodeordet er ikke gyldtigt. Det skal indeholde tal, bogstaver og special tegn"
                } else {
                    print command.errors
                    fail "De to kodeord matcher ikke"
                }
            }
        } else {
            fail message: "Ukendt bruger", suggestedHttpStatus: 401
        }
    }

    private boolean checkPassword(String current, String newPassword) {
        springSecurityService.passwordEncoder.isPasswordValid(current, newPassword, null)
    }
}
