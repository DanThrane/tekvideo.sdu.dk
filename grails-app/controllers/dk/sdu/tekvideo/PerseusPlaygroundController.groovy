package dk.sdu.tekvideo

import org.springframework.security.access.annotation.Secured

@Secured("ROLE_TEACHER")
class PerseusPlaygroundController {
    def index() {}
    def frame() {}
    def renderer() {}
}
