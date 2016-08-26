package dk.sdu.tekvideo

trait Node {
    abstract Node getParent()
    abstract NodeStatus getLocalStatus()
    abstract NodeIdentifier getIdentifier()

    NodeStatus getStatus() {
        def node = this
        while (node != null && node.localStatus == NodeStatus.VISIBLE) {
            node = node.parent
        }
        return (node == null) ? NodeStatus.VISIBLE : node.localStatus
    }
}
