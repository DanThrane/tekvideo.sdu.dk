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

    // TODO Some refactoring would be nice instead of copy & pasting

    List<Exercise> getAllExercises() {
        def subject = this
        Collections.unmodifiableList(
                SubjectExercise.withCriteria() {
                    eq("subject", subject)
                    order("weight", "desc")
                    exercise {}
                }.exercise
        )
    }

    List<Video> getActiveVideos() {
        def subject = this
        Collections.unmodifiableList(
                SubjectExercise.withCriteria() {
                    eq("subject", subject)
                    order("weight", "desc")
                    exercise {
                        eq("class", Video)
                        not {
                            eq("localStatus", NodeStatus.TRASH)
                        }
                    }
                }.exercise
        ) as List<Video>
    }

    List<Video> getVisibleVideos() {
        def subject = this
        Collections.unmodifiableList(
                SubjectExercise.withCriteria() {
                    eq("subject", subject)
                    order("weight", "desc")
                    exercise {
                        eq("class", Video)
                        eq("localStatus", NodeStatus.VISIBLE)
                    }
                }.exercise
        ) as List<Video>
    }

    List<Video> getVideos() {
        def subject = this
        Collections.unmodifiableList(
                SubjectExercise.withCriteria() {
                    eq("subject", subject)
                    order("weight", "desc")
                    exercise {
                        eq("class", Video)
                    }
                }.exercise
        ) as List<Video>
    }

    List<WrittenExercise> getWrittenExercises() {
        def subject = this
        Collections.unmodifiableList(
                SubjectExercise.withCriteria() {
                    eq("subject", subject)
                    order("weight", "desc")
                    exercise {
                        eq("class", WrittenExercise)
                    }
                }.exercise
        ) as List<WrittenExercise>
    }

    List<WrittenExercise> getVisibleWrittenExercises() {
        def subject = this
        Collections.unmodifiableList(
                SubjectExercise.withCriteria() {
                    eq("subject", subject)
                    order("weight", "desc")
                    exercise {
                        eq("class", WrittenExercise)
                        eq("localStatus", NodeStatus.VISIBLE)
                    }
                }.exercise
        ) as List<WrittenExercise>
    }

    List<WrittenExercise> getActiveWrittenExercises() {
        def subject = this
        Collections.unmodifiableList(
                SubjectExercise.withCriteria() {
                    eq("subject", subject)
                    order("weight", "desc")
                    exercise {
                        eq("class", WrittenExercise)
                        not {
                            eq("localStatus", NodeStatus.TRASH)
                        }
                    }
                }.exercise
        ) as List<WrittenExercise>
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
