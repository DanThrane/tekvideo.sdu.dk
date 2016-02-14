class UrlMappings {

	static mappings = {
        "/"(controller: "Home", action: "index")
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }
        name teaching: "/t/$teacher/$course?/$year?/$fall?/$subject?/$vidid?" {
            controller = "teaching"
            constraints {
                vidid minSize: 0
            }
        }
        "500"(controller:'error')
	}
}
