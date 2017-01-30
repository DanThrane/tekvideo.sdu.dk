package dk.sdu.tekvideo

class BrowserService {
    def courseService
    def subjectService

    List<Map> nodesForBrowser(List<Node> nodes) {
        return nodes.collect { convertNode(it) }
    }

    private Map convertNode(Node node) {
        if (node instanceof Course) return convertCourse(node)
        if (node instanceof Subject) return convertSubject(node)
        if (node instanceof Video) return convertVideo(node)
        if (node instanceof WrittenExerciseGroup) return convertWrittenExerciseGroup(node)
        else throw new IllegalStateException("Internal error: Unknown type of node: ${node.class.name}")
    }

}
