package dk.sdu.tekvideo

import grails.plugin.springsecurity.SpringSecurityUtils
import org.springframework.security.access.annotation.Secured

import javax.servlet.http.HttpServletResponse

@Secured('permitAll')
class LogoutController {

    /**
     * Index action. Redirects to the Spring security logout uri.
     */
    def index() {

        // commented out, does not seem to work while running locally.
        // Should be enabled when live, as this is there to prevent
        // an exploit that lets people log each other out forcefully.
        /*
		if (!request.post && SpringSecurityUtils.getSecurityConfig().logout.postOnly) {
			response.sendError HttpServletResponse.SC_METHOD_NOT_ALLOWED // 405
			return
		}*/
        // TODO put any pre-logout code here
        redirect uri: SpringSecurityUtils.securityConfig.logout.filterProcessesUrl // '/j_spring_security_logout'
    }
}
