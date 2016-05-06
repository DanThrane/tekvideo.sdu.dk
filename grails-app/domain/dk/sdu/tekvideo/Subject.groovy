package dk.sdu.tekvideo

import java.util.stream.Collectors

class Subject implements Node {
    static final String DEFAULT_DESCRIPTION = "Ingen beskrivelse"

    String name
    String description = DEFAULT_DESCRIPTION
    NodeStatus localStatus = NodeStatus.VISIBLE

    List<Video> videos // The subject will order its own videos
    static hasMany = [videos: Video]

    static constraints = {
        name nullable: false, blank: false
        description nullable: true
    }

    static mapping = {
        description type: "text"
        videos cascade: "all-delete-orphan", indexColumn: [name: "videos_idx", type: Long]
    }

    static belongsTo = [course: Course]

    List<Video> getActiveVideos() {
        videos.stream().filter { it != null && it.localStatus != NodeStatus.TRASH }.collect(Collectors.toList())
    }

    List<Video> getVisibleVideos() {
        videos.stream().filter { it != null && it.localStatus == NodeStatus.VISIBLE }.collect(Collectors.toList())
    }

    String getDescription() {
        if (description == null) return DEFAULT_DESCRIPTION
        return description
    }

    @Override
    String toString() {
        return name
    }

    @Override
    Node getParent() {
        course
    }
}
