package dk.sdu.tekvideo

trait Node {
    Node eagerlyLoadedParent = null
    Node getParent() {
        if (eagerlyLoadedParent != null) return eagerlyLoadedParent
        eagerlyLoadedParent = loadParent()
        return eagerlyLoadedParent
    }

    abstract Node loadParent()
    abstract NodeStatus getLocalStatus()
    abstract String getName()

    NodeStatus getStatus() {
        def node = this
        while (node != null && node.localStatus == NodeStatus.VISIBLE) {
            node = node.parent
        }
        return (node == null) ? NodeStatus.VISIBLE : node.localStatus
    }
}
