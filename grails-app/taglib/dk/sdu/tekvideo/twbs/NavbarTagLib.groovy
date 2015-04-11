package dk.sdu.tekvideo.twbs

/**
 * A tag lib for the navbar component of Bootstrap. Doesn't support anywhere near all the features that it actually
 * has.
 */
class NavbarTagLib {
    // Should components, like this, have its own namespace instead of one global twbs namespace? (Dan)
    static namespace = "twbs"

    def navbar = { attrs, body ->
        String title = attrs.title ? attrs.title : ""
        String image = attrs.image ? "<img src='$attrs.image' alt='Logo'/>" : ""
        // TODO Add more features here
        out << """
        <nav class=\"navbar navbar-default\">
            <div class=\"container-fluid\">
                <div class=\"navbar-header\">
                    <button type=\"button\" class=\"navbar-toggle collapsed\" 
                        data-toggle=\"collapse\" data-target=\"#navbar\" aria-expanded=\"false\" 
                        aria-controls=\"navbar\">
                        <span class=\"sr-only\">Toggle navigation</span>
                        <span class=\"icon-bar\"></span>
                        <span class=\"icon-bar\"></span>
                        <span class=\"icon-bar\"></span>
                    </button>
                    <a class=\"navbar-brand\" href=\"/Gruppe1\">$image $title</a>
                </div>
                <div id=\"navbar\" class=\"navbar-collapse collapse\">
                    ${body()}
                </div>
            </div>
        </nav>
        """
    }

    def navcontainer = { attrs, body ->
        def location = attrs.location ? "navbar-$attrs.location" : ""
        out << """
        <ul class=\"nav navbar-nav $location\">
            ${body()}
        </ul>
        """
    }

    def navform = { attrs, body ->
        def location = attrs.location ? "navbar-$attrs.location" : ""
        out << """
        <form class=\"navbar-form $location\">
            ${body()}
        </form>
        """
    }

    def subNavBar = { attrs, body ->
        out << """<ul class=\"nav nav-tabs\">
                    ${body()}
                </ul>"""
    }

    def navitem = { attrs, body ->
        def role = attrs.role ? attrs.role : ""
        out << "<li"
        if (attrs.active) {
            out << " class=\"active\""
        }
        out << " role = \"${role}\">${body()}</li>"
    }

}
