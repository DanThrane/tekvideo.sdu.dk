package dk.sdu.tekvideo

import grails.validation.Validateable

class RegisterCommand implements Validateable {
    String username
    String email
    String password
    String password2
    String elearnId
    String realName

    static constraints = {
        username blank: false, validator: { value, command ->
            if (value) {
                if (User.findByUsernameAndIsCas(value, false)) {
                    return 'registerCommand.username.unique'
                }
            }
        }
        email blank: false, email: true
        password blank: false, validator: RegisterController.passwordValidator
        password2 validator: RegisterController.password2Validator
        elearnId nullable: true, blank: false
        realName nullable: true, blank: true
    }
}
