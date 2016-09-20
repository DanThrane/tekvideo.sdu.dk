package dk.sdu.tekvideo

import dk.sdu.tekvideo.events.AnswerQuestionEvent
import dk.sdu.tekvideo.events.VisitVideoEvent

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

trait ExerciseNode extends Node {}