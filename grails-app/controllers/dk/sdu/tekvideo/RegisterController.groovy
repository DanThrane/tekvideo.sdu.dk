package dk.sdu.tekvideo

import grails.plugin.springsecurity.SpringSecurityUtils
import org.springframework.security.access.annotation.Secured

/**
 * Imported from Spring Security, it overwrites the actual register method.
 * Purpose is do disable the confirmation mail. Can be deleted to reverse changes.
 *
 * Note: Deleting this would break the ability to create a new user, without a
 * mail server.
 */
@Secured("permitAll")
class RegisterController extends grails.plugin.springsecurity.ui.RegisterController {

    @SuppressWarnings("Instanceof")
    def register(RegisterCommand command) {
        // Verify input is correct:
        if (command.hasErrors()) {
            render view: 'index', model: [command: command]
            return
        }

        // Create a new user:
        def user = new User([
                email        : command.email,
                username     : command.username,
                password     : command.password,
                elearnId     : (command.elearnId == "") ? null : command.elearnId,
                realName     : command.realName,
                accountLocked: false,
                enabled      : true
        ]).save(flush: true, failOnError: true)

        new Student(user: user).save(flush: true)
        UserRole.create user, Role.findByAuthority("ROLE_STUDENT"), true
        flash.success = "Account created successfully!"
        SpringSecurityUtils.reauthenticate(command.username, command.password)
        redirect uri: "/"
    }
}

class RegisterCommand {
    String username
    String email
    String password
    String password2
    String elearnId
    String realName

    def grailsApplication

    static constraints = {
        username blank: false, validator: { value, command ->
            if (value) {
                def User = command.grailsApplication.getDomainClass(
                        SpringSecurityUtils.securityConfig.userLookup.userDomainClassName).clazz
                if (User.findByUsername(value)) {
                    return 'registerCommand.username.unique'
                }
            }
        }
        email blank: false, email: true
        password blank: false, validator: RegisterController.passwordValidator
        password2 validator: RegisterController.password2Validator
        elearnId nullable: true, blank: true
        realName nullable: true, blank: true
    }
}
