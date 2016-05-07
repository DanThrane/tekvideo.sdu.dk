package dk.sdu.tekvideo

class Subject implements Node {
    static final String DEFAULT_DESCRIPTION = "Ingen beskrivelse"

    String name
    String description = DEFAULT_DESCRIPTION
    NodeStatus localStatus = NodeStatus.VISIBLE

    static constraints = {
        name nullable: false, blank: false
        description nullable: true
    }

    static mapping = {
        description type: "text"
    }

    List<Video> getActiveVideos() {
        videos.findAll { it != null && it.localStatus != NodeStatus.TRASH }
    }

    List<Video> getVisibleVideos() {
        videos.findAll { it != null && it.localStatus == NodeStatus.VISIBLE }
    }

    List<Video> getVideos() {
        Collections.unmodifiableList(SubjectVideo.findAllBySubject(this, [sort: 'weight']).video)
    }

    Course getCourse() {
        return getParent() as Course
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
        CourseSubject.findBySubject(this).course
    }
}
