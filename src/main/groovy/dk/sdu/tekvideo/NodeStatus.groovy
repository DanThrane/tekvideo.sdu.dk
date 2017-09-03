package dk.sdu.tekvideo

/**
 * Represents the status of a single node
 */
enum NodeStatus {
    VISIBLE,
    INVISIBLE,
    TRASH

    static NodeStatus fromValue(String str) {
        values().find { it.name() == str }
    }
}