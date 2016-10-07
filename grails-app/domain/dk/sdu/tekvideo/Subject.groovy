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

    List<Exercise> getActiveVideos() {
        videos.findAll { it != null && it.localStatus != NodeStatus.TRASH }
    }

    List<Exercise> getVisibleVideos() {
        videos.findAll { it != null && it.localStatus == NodeStatus.VISIBLE }
    }

    List<Exercise> getVideos() {
        Collections.unmodifiableList(SubjectExercise.findAllBySubject(this, [sort: 'weight']).exercise)
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
