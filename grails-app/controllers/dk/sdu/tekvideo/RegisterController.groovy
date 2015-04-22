package dk.sdu.tekvideo

import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.ui.RegisterCommand

/**
 * Imported from Spring Security, it overwrites the actual register method.
 * Purpose is do disable the confirmation mail. Can be deleted to reverse changes.
 *
 * Note: Deleting this would break the ability to create a new user, without a
 * mail server.
 */
class RegisterController extends grails.plugin.springsecurity.ui.RegisterController {

    @Override
    @SuppressWarnings("Instanceof")
    def register(RegisterCommand command) {
        // Verify input is correct:
        if (command.hasErrors()) {
            render view: 'index', model: [command: command]
            return
        }

        // Create a new user:
        def user = new User(email: command.email, username: command.username, password: command.password,
                accountLocked: false, enabled: true).save(flush: true, failOnError: true)

        new Student(user: user).save(flush: true)
        UserRole.create user, Role.findByAuthority("ROLE_USER"), true
        flash.success = "Account created successfully!"
        SpringSecurityUtils.reauthenticate(command.username, command.password)
        redirect uri: "/"
    }
}
