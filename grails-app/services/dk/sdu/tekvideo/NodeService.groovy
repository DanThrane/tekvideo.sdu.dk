package dk.sdu.tekvideo

class NodeService implements ContainerNodeInformation<Node, Node> {
    def userService
    def courseService
    def subjectService
    def exerciseService
    def rootService

    static final ROOT = RootNode.INSTANCE

    private static enum RootNode implements Node {
        INSTANCE

        @Override
        Node loadParent() {
            return null
        }

        @Override
        NodeStatus getLocalStatus() {
            return NodeStatus.VISIBLE
        }

        @Override
        String getName() {
            return "root"
        }
    }

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
        def start = System.currentTimeMillis()
        def result = resolveImplementation(node).getThumbnail(node)
        println(System.currentTimeMillis() - start)
        return result
    }

    @Override
    List<NodeBrowserCrumbs> getBreadcrumbs(Node node) {
        resolveImplementation(node).getBreadcrumbs(node)
    }

    @Override
    NodeBrowserInformation getInformationForBrowser(Node node, String thumbnail, boolean resolveThumbnail,
                                                    boolean addBreadcrumbs) {
        resolveImplementation(node).getInformationForBrowser(node, thumbnail, resolveThumbnail, addBreadcrumbs)
    }

    List<NodeBrowserInformation> listVisibleChildrenForBrowser(Node node) {
        def children = listVisibleChildren(node)
        Map<Class<? extends Node>, List<Node>> groupedByType = children.groupBy { it.class }
        Map<Node, String> thumbnails = [:]
        for (def entry : groupedByType) {
            def bulk = resolveImplementation(entry.key).getThumbnailsBulk(entry.value)
            thumbnails.putAll(bulk)
        }

        def info = children.collect {
            getInformationForBrowser(it, thumbnails.get(it), false,false)
        }
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
    private <N extends Node> NodeInformation<N> resolveImplementation(Class<N> type) {
        if (type == Course) return courseService
        if (type == Subject) return subjectService
        if (type == Video || type == WrittenExerciseGroup) return exerciseService
        else throw new IllegalArgumentException("Cannot find service implementation for node of type " +
                "${type.name}")
    }

    @SuppressWarnings("GroovyAssignabilityCheck")
    private <N extends Node> NodeInformation<N> resolveImplementation(N node) {
        if (node == ROOT) return rootService
        return resolveImplementation(node.class)
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
