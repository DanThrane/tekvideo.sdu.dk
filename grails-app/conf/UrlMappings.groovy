class UrlMappings {

    @SuppressWarnings("GroovyAssignabilityCheck")
    static mappings = {
        "/"(controller: "Home", action: "index")

        name stats: "/stats/$type?/$id?/$act?(.$format)?" {
            controller = "stats"
        }

        name teaching: "/t/$teacher/$course?/$year?/$fall?/$subject?/$vidid?" {
            controller = "teaching"
        }

        "/$controller/$action?/$id?(.$format)?" {
            constraints {
                // apply constraints here
            }
        }
        "500"(controller: 'error')
    }
}
