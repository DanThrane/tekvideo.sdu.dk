package dk.sdu.tekvideo

import org.springframework.security.access.annotation.Secured

/**
 * Controller for displaying the about page.
 */
@Secured("permitAll")
class AboutController {
    def index() {}
}
