package dk.sdu.tekvideo

import grails.util.Environment

class FrontendTagLib {
    static namespace = "sdu"

    def appResourceImport = { attrs, body ->
        String resource = attrs.remove("href")
        out << "<link rel=\"import\" href=\"${pathToResource(resource)}\">"
    }

    def appResourceJs = { attrs, body ->
        String resource = attrs.remove("href")
        out << "<script type='text/javascript' src='${pathToResource(resource)}'></script>"
    }

    def appResourceCss = { attrs, body ->
        String resource = attrs.remove("href")
        out << "<link rel=\"stylesheet\" type=\"text/css\" href=\"${pathToResource(resource)}\">"
    }

    def appResource = { attrs, body ->
        String resource = attrs.remove("href")
        out << pathToResource(resource)
    }

    private String pathToResource(String resource) {
        if (Environment.current != Environment.PRODUCTION) {
            return "/app/${resource}"
        } else {
            return asset.assetPath(src: resource)
        }
    }
}
