package dk.sdu.tekvideo

import grails.validation.Validateable

class UpdateUserCommand implements Validateable {
    String username
    Boolean isCas
    String realName
    String email
    String elearnId
    List<String> roles

    static constraints = {
        realName nullable: true, blank: true
        email nullable: true, blank: true
        elearnId nullable: true, blank: true
        username validator: { value, command ->
            User user = User.findByUsernameAndIsCas(command.username, command.isCas)
            return user != null
        }
    }

}
