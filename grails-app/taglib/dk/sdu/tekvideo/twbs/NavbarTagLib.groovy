package dk.sdu.tekvideo.twbs

/**
 * A tag lib for the navbar component of Bootstrap. Doesn't support anywhere near all the features that it actually
 * has.
 */
class NavbarTagLib {
    // Should components, like this, have its own namespace instead of one global twbs namespace? (Dan)
    static namespace = "twbs"

    def navbar = { attrs, body ->
        // TODO Add more features here
        out << """
        <nav class="navbar navbar-default">
            <div class="container-fluid">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                    <a class="navbar-brand" href="/">${attrs.title}</a>
                </div>
                <div id="navbar" class="navbar-collapse collapse">
                    ${body()}
                </div>
            </div>
        </nav>
        """
    }

    def navcontainer = { attrs, body ->
        def location = attrs.location ? "navbar-$attrs.location" : ""
        out << """
        <ul class="nav navbar-nav $location">
            ${body()}
        </ul>
        """
    }

    def navform = { attrs, body ->
        def location = attrs.location ? "navbar-$attrs.location" : ""
        out << """
        <form class="navbar-form $location">
            ${body()}
        </form>
        """
    }

    def navitem = { attrs, body ->
        out << "<li"
        if (attrs.active) {
            out << " class=\"active\""
        }
        out << ">${body()}</li>"
    }

    def navdropdown = { attrs, body ->
        String title = getRequiredAttribute(attrs, "title", "navdropdown")
        out << """
            <li class=\"dropdown\">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">
                $title
                </a>
                <ul class="dropdown-menu" role="menu">
                    ${body()}
                </ul>
            </li>
            """
    }

    def dropitem = { attrs, body ->
        out << "<li>${body()}</li>"
    }

    def dropdivider = { attrs, body ->
        out << "<li class='divider'></li>"
    }

    protected getRequiredAttribute(attrs, String name, String tagName) {
        if (!attrs.containsKey(name)) {
            throwTagError("Tag [$tagName] is missing required attribute [$name]")
        }
        attrs.remove name
    }

}
