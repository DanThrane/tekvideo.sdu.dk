package dk.sdu.tekvideo

import grails.validation.Validateable

class UpdatePasswordCommand implements Validateable {
    String username
    String currentPassword
    String newPassword
    String newPassword2

    static constraints = {
        currentPassword blank: true, nullable: true
        newPassword blank: false, validator: grails.plugin.springsecurity.ui.RegisterController.passwordValidator
        newPassword2 blank: false, validator: { value, command ->
            if (command.newPassword != command.newPassword2) {
                return "password.mismatch"
            }
        }
    }
}
