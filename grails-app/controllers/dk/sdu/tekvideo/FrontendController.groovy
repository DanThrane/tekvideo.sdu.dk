package dk.sdu.tekvideo

import grails.util.Environment
import org.springframework.security.access.annotation.Secured

@Secured("permitAll")
class FrontendController {
    static defaultAction = "serve"
    private static ROOT_DIR = new File("frontend")

    def serve(String path) {
        if (Environment.current != Environment.PRODUCTION) {
            def result = new File(ROOT_DIR, path)
            if (!result.absolutePath.startsWith(ROOT_DIR.absolutePath)) {
                render status: 404
                return
            }

            if (!result.exists()) {
                render status: 404
                return
            }

            render file: result
        } else {
            throw new IllegalStateException("Should not be called in prod")
        }
    }
}
