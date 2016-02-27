package dk.sdu.tekvideo

class User {
    transient springSecurityService

    String username
    String password
    String email
    boolean enabled = true
    boolean accountExpired
    boolean accountLocked
    boolean passwordExpired

    String elearnId
    String realName

    static transients = ['springSecurityService']

    static constraints = {
        username    blank: false, unique: true
        password    blank: false
        email       nullable: true, blank: false
        elearnId    nullable: true, blank: false
        realName    nullable: true, blank: true
    }

    static mapping = {
        table 'myusers'
        password column: '`password`'
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
