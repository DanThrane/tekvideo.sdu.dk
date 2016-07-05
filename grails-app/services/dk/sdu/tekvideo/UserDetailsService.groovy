package dk.sdu.tekvideo

import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.userdetails.GormUserDetailsService
import grails.plugin.springsecurity.userdetails.GrailsUser
import grails.transaction.Transactional
import org.springframework.dao.DataAccessException
import org.springframework.security.core.authority.GrantedAuthorityImpl
import org.springframework.security.core.context.SecurityContextHolder
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
    @Transactional(readOnly=true,
            noRollbackFor=[IllegalArgumentException, UsernameNotFoundException])
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        def user = User.findByUsername(username)

        if (!user) {
            println SecurityContextHolder.getContext().getAuthentication()
            println "Hello, creating a new user for $username"
            return new GrailsUser(username, '', true, true, true, true, NO_ROLES, 999)
        }

        def authorities = user.authorities.collect {
            new GrantedAuthorityImpl(it.authority)
        }

        return new GrailsUser(user.username, user.password,
                user.enabled, !user.accountExpired, !user.passwordExpired,
                !user.accountLocked, authorities ?: NO_ROLES, user.id)
    }
}
