class UrlMappings {

	static mappings = {
        "$teacher/$course?/$subject?/$vidid?"(controller: "teaching")
        "/a/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }
        "/"(view:"/index")
        "500"(view:'/error')
	}
}
