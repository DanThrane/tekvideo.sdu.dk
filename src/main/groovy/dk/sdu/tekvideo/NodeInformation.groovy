package dk.sdu.tekvideo

trait NodeInformation<N extends Node> {
    abstract String getThumbnail(N node)
    abstract List<NodeBrowserCrumbs> getBreadcrumbs(N node)
    abstract NodeBrowserInformation getInformationForBrowser(N node, String thumbnail, boolean resolveThumbnail,
                                                             boolean addBreadcrumbs)

    Map<N, String> getThumbnailsBulk(List<N> node) {
        return node.collectEntries { [(it): getThumbnail(it)] }
    }
}

interface ContainerNodeInformation<N extends Node, C extends Node> extends NodeInformation<N> {
    List<C> listVisibleChildren(N node)
    List<C> listActiveChildren(N node)
}
