class UrlMappings {

	static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }
        name teaching: "/t/$teacher/$course?/$subject?/$vidid?" {
            controller = "teaching"
            constraints {
                vidid minSize: 0
            }
        }
        "/"(view:"/index")
        "500"(view:'/error')
	}
}
