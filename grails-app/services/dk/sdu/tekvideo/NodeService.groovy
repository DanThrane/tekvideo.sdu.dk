package dk.sdu.tekvideo

class NodeService implements ContainerNodeInformation<Node, Node> {
    def userService
    def courseService
    def subjectService
    def exerciseService

    boolean canView(Node node) {
        def status = node?.status

        node != null &&
                (status == NodeStatus.VISIBLE ||
                        (status == NodeStatus.INVISIBLE &&
                                userService.authenticatedTeacher == resolveCourse(node)?.teacher))
    }

    private Course resolveCourse(Node node) {
        while (node != null) {
            if (node instanceof Course) return node
            node = node.parent
        }
        return null
    }

    @Override
    String getThumbnail(Node node) {
        resolveImplementation(node).getThumbnail(node)
    }

    @Override
    List<NodeBrowserCrumbs> getBreadcrumbs(Node node) {
        resolveImplementation(node).getBreadcrumbs(node)
    }

    @Override
    NodeBrowserInformation getInformationForBrowser(Node node, boolean addBreadcrumbs) {
        resolveImplementation(node).getInformationForBrowser(node, addBreadcrumbs)
    }

    List<NodeBrowserInformation> listVisibleChildrenForBrowser(Node node) {
        def info = listVisibleChildren(node).collect { getInformationForBrowser(it, false) }
        def breadcrumbs = getBreadcrumbs(node)
        info.each { it.breadcrumbs = breadcrumbs }
        return info
    }

    @Override
    List<Node> listVisibleChildren(Node node) {
        resolveContainerImplementation(node).listVisibleChildren(node)
    }

    @Override
    List<Node> listActiveChildren(Node node) {
        resolveContainerImplementation(node).listActiveChildren(node)
    }

    @SuppressWarnings("GroovyAssignabilityCheck")
    private <N extends Node> NodeInformation<N> resolveImplementation(N node) {
        if (node instanceof Course) return courseService
        if (node instanceof Subject) return subjectService
        if (node instanceof Exercise) return exerciseService
        else throw new IllegalArgumentException("Cannot find service implementation for node of type " +
                "${node.class.name}")
    }

    private ContainerNodeInformation resolveContainerImplementation(Node node) {
        def implementation = resolveImplementation(node)
        if (implementation instanceof ContainerNodeInformation) {
            return implementation
        } else {
            throw new IllegalArgumentException("Given node of type ${node.class.name} cannot have any children")
        }
    }
}
