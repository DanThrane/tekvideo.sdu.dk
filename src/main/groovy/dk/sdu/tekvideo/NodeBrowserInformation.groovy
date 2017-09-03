package dk.sdu.tekvideo

class NodeBrowserInformation {
    String title
    String description = "Ingen beskrivelse"
    String thumbnail
    String url
    List<NodeBrowserCrumbs> breadcrumbs
    List<NodeBrowserStats> stats
    List<NodeBrowserCrumbs> featuredChildren

    NodeBrowserInformation(String title, String description, String thumbnail, String url,
                           List<NodeBrowserCrumbs> breadcrumbs = Collections.emptyList(),
                           List<NodeBrowserStats> stats = Collections.emptyList(),
                           List<NodeBrowserCrumbs> featuredChildren = Collections.emptyList()) {
        this.title = title
        this.description = description
        this.thumbnail = thumbnail
        this.url = url
        this.breadcrumbs = breadcrumbs
        this.stats = stats
        this.featuredChildren = featuredChildren
    }
}

class NodeBrowserCrumbs {
    String title
    String url

    NodeBrowserCrumbs(String title, String url) {
        this.title = title
        this.url = url
    }
}

class NodeBrowserStats {
    String content
    String icon

    NodeBrowserStats(String content, String icon = null) {
        this.content = content
        this.icon = icon
    }
}
