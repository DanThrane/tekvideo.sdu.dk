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

    // TODO Some refactoring would be nice instead of copy & pasting. Should this even be in this class?

    Long getActiveExerciseCount() {
        def subject = this
        return SubjectExercise.withCriteria {
            eq("subject", subject)
            exercise {
                not {
                    eq("localStatus", NodeStatus.TRASH)
                }
            }
            projections {
                count()
            }
        }[0]
    }

    Long getVisibleExerciseCount() {
        def subject = this
        return SubjectExercise.withCriteria {
            eq("subject", subject)
            exercise {
                eq("localStatus", NodeStatus.VISIBLE)
            }
            projections {
                count()
            }
        }[0]
    }

    List<Exercise> getAllExercises() {
        def subject = this
        Collections.unmodifiableList(
                SubjectExercise.withCriteria() {
                    eq("subject", subject)
                    order("weight", "asc")
                    exercise {}
                }.exercise
        )
    }

    List<Exercise> getAllVisibleExercises() {
        def subject = this
        Collections.unmodifiableList(
                SubjectExercise.withCriteria() {
                    eq("subject", subject)
                    order("weight", "asc")
                    exercise {
                        eq("localStatus", NodeStatus.VISIBLE)
                    }
                }.exercise
        )
    }

    List<Exercise> getAllActiveExercises() {
        def subject = this
        Collections.unmodifiableList(
                SubjectExercise.withCriteria() {
                    eq("subject", subject)
                    order("weight", "asc")
                    exercise {
                        not {
                            eq("localStatus", NodeStatus.TRASH)
                        }
                    }
                }.exercise
        )
    }

    List<Exercise> getAllExercisesByStatus(NodeStatus status) {
        def subject = this
        Collections.unmodifiableList(
                SubjectExercise.withCriteria() {
                    eq("subject", subject)
                    order("weight", "asc")
                    exercise {
                        eq("localStatus", status)
                    }
                }.exercise
        )
    }

    List<Video> getActiveVideos() {
        def subject = this
        Collections.unmodifiableList(
                SubjectExercise.withCriteria() {
                    eq("subject", subject)
                    order("weight", "asc")
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
                    order("weight", "asc")
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
                    order("weight", "asc")
                    exercise {
                        eq("class", Video)
                    }
                }.exercise
        ) as List<Video>
    }

    List<WrittenExerciseGroup> getWrittenExercises() {
        def subject = this
        Collections.unmodifiableList(
                SubjectExercise.withCriteria() {
                    eq("subject", subject)
                    order("weight", "asc")
                    exercise {
                        eq("class", WrittenExerciseGroup)
                    }
                }.exercise
        ) as List<WrittenExerciseGroup>
    }

    List<WrittenExerciseGroup> getVisibleWrittenExercises() {
        def subject = this
        Collections.unmodifiableList(
                SubjectExercise.withCriteria() {
                    eq("subject", subject)
                    order("weight", "asc")
                    exercise {
                        eq("class", WrittenExerciseGroup)
                        eq("localStatus", NodeStatus.VISIBLE)
                    }
                }.exercise
        ) as List<WrittenExerciseGroup>
    }

    List<WrittenExerciseGroup> getActiveWrittenExercises() {
        def subject = this
        Collections.unmodifiableList(
                SubjectExercise.withCriteria() {
                    eq("subject", subject)
                    order("weight", "asc")
                    exercise {
                        eq("class", WrittenExerciseGroup)
                        not {
                            eq("localStatus", NodeStatus.TRASH)
                        }
                    }
                }.exercise
        ) as List<WrittenExerciseGroup>
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
