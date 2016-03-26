package dk.sdu.tekvideo

import java.util.stream.Collectors

class Subject implements Node {
    String name
    String description = "Ingen beskrivelse"
    NodeStatus localStatus = NodeStatus.VISIBLE
    Long subjects_idx

    List<Video> videos // The subject will order its own videos
    static hasMany = [videos: Video]

    static constraints = {
        name nullable: false, blank: false
        description nullable: true
        subjects_idx nullable: true
    }

    static mapping = {
        description type: "text"
        videos cascade: "all-delete-orphan", indexColumn: [name: "videos_idx", type: Long]
        subjects_idx updateable: false, insertable: false
    }

    static belongsTo = [course: Course]

    List<Video> getActiveVideos() {
        videos.stream().filter { it != null && it.localStatus != NodeStatus.TRASH }.collect(Collectors.toList())
    }

    List<Video> getVisibleVideos() {
        videos.stream().filter { it != null && it.localStatus == NodeStatus.VISIBLE }.collect(Collectors.toList())
    }

    String getDescription() {
        if (description == null) return "Ingen beskrivelse"
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
