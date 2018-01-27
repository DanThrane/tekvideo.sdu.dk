package dk.sdu.tekvideo

import grails.plugin.springsecurity.annotation.Secured

class SsoController {
    @Secured(["ROLE_TEACHER", "ROLE_STUDENT"])
    def index() {
        // TODO Right now the CAS system takes over as the default "you must login".
        // This serves as a dummy entry point will trigger that and redirect the user back after we have
        // authenticated. Ideally this should be, such that we don't automatically redirect to the CAS system.
        // But that might take some time, given complete lack of documentation.
        redirect uri: "/"
    }
}
