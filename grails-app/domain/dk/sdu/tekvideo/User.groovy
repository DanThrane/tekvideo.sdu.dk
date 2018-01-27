package dk.sdu.tekvideo

class User {
    transient springSecurityService

    String username
    String password
    String email
    Boolean enabled = true
    Boolean accountExpired = false
    Boolean accountLocked = false
    Boolean passwordExpired = false
    Boolean isCas = false

    String elearnId
    String realName

    static transients = ['springSecurityService']

    static constraints = {
        username blank: false, unique: ["isCas"]
        password blank: false
        email nullable: true, blank: false
        elearnId nullable: true, blank: false
        realName nullable: true, blank: true
    }

    static mapping = {
        table 'myusers'
        password column: '`password`'
    }

    static jsonMarshaller = { User it ->
        List<String> roles = UserRole.findAllByUser(it).collect { it.role.authority }
        return [
                username: it.username,
                email   : it.email,
                isCas   : it.isCas,
                elearnId: it.elearnId,
                realName: it.realName,
                roles   : roles
        ]
    }


    Set<Role> getAuthorities() {
        UserRole.findAllByUser(this).collect { it.role }
    }

    def beforeInsert() {
        encodePassword()
    }

    def beforeUpdate() {
        if (isDirty('password')) {
            encodePassword()
        }
    }

    protected void encodePassword() {
        password = springSecurityService?.passwordEncoder ? springSecurityService.encodePassword(password) : password
    }
}
