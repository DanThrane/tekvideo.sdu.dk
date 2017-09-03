package dk.sdu.tekvideo

class RootService implements ContainerNodeInformation<Node, Course> {
    def courseService

    @Override
    String getThumbnail(Node node) {
        throw new UnsupportedOperationException("Not supported by root node")
    }

    @Override
    List<NodeBrowserCrumbs> getBreadcrumbs(Node node) {
        Collections.emptyList()
    }

    @Override
    NodeBrowserInformation getInformationForBrowser(Node node, String thumbnail, boolean resolveThumbnail,
                                                    boolean addBreadcrumbs) {
        throw new UnsupportedOperationException("Not supported by root node")
    }

    @Override
    List<Course> listVisibleChildren(Node node) {
        courseService.listVisibleCourses()
    }

    @Override
    List<Course> listActiveChildren(Node node) {
        courseService.listActiveCourses()
    }
}
