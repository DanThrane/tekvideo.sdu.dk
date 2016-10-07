package dk.sdu.tekvideo

import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.userdetails.GormUserDetailsService
import grails.plugin.springsecurity.userdetails.GrailsUser
import grails.transaction.Transactional
import org.codehaus.groovy.grails.web.util.WebUtils
import org.springframework.dao.DataAccessException
import org.springframework.security.core.authority.GrantedAuthorityImpl
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException

class UserDetailsService extends GormUserDetailsService {

    static final List NO_ROLES =
            [new GrantedAuthorityImpl(SpringSecurityUtils.NO_ROLE)]


    @Override
    UserDetails loadUserByUsername(String username, boolean loadRoles)
            throws UsernameNotFoundException, DataAccessException {
        return loadUserByUsername(username)
    }

    @Override
    @Transactional(readOnly=false,
            noRollbackFor=[IllegalArgumentException, UsernameNotFoundException])
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO I fear that it might be possible to fake this information, making it possible to bypass the entire
        // security check. Although this depends on whether or not the information is validated before or after this.
        def isCas = WebUtils.retrieveGrailsWebRequest().request.servletPath == "/j_spring_cas_security_check"

        if (isCas && username == "_cas_stateful_") {
            // Username sometimes becomes "_cas_stateful_" not sure why. This is all done by the layers below.
            throw new UsernameNotFoundException("User not found")
        }

        def user = User.findByUsernameAndIsCas(username, isCas)
        if (isCas && user == null) {
            user = new User(
                    username: username,
                    password: "NO_PASSWORD", // Doesn't matter, will never be used
                    email: "$username@student.sdu.dk", // Not always true
                    elearnId: "$username",
                    realName: username,
                    isCas: true
            )
            user.save(flush: true, failOnError: true)
            def student = new Student(user: user)
            student.save(flush: true)
            def defaultRole = Role.findByAuthority("ROLE_STUDENT")
            UserRole.create(user, defaultRole, true)
        }

        def authorities = user.authorities.collect {
            new GrantedAuthorityImpl(it.authority)
        }

        return new GrailsUser(user.username, user.password,
                user.enabled, !user.accountExpired, !user.passwordExpired,
                !user.accountLocked, authorities ?: NO_ROLES, user.id)
    }
}
