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

    List<Video> getActiveVideos() {
        videos.findAll { it != null && it.localStatus != NodeStatus.TRASH }
    }

    List<Video> getVisibleVideos() {
        videos.findAll { it != null && it.localStatus == NodeStatus.VISIBLE }
    }

    // TODO get course

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
