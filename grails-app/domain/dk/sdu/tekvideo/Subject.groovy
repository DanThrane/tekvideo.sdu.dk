package dk.sdu.tekvideo

class Subject implements Node {
    static final String DEFAULT_DESCRIPTION = "Ingen beskrivelse"

    String name
    String description = DEFAULT_DESCRIPTION
    NodeStatus localStatus = NodeStatus.VISIBLE
    static transients = ['eagerlyLoadedParent']

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
        List<Exercise> exercises = SubjectExercise.withCriteria() {
            eq("subject", subject)
            order("weight", "asc")
            exercise {}
        }.exercise
        exercises.eachWithIndex { it, idx ->
            it.eagerlyLoadedParent = this
            it.eagerIndex = idx
        }
        Collections.unmodifiableList(exercises)
    }

    List<Exercise> getAllVisibleExercises() {
        def subject = this
        List<Exercise> exercises = SubjectExercise.withCriteria() {
            eq("subject", subject)
            order("weight", "asc")
            exercise {
                eq("localStatus", NodeStatus.VISIBLE)
            }
        }.exercise
        exercises.eachWithIndex { it, idx ->
            it.eagerlyLoadedParent = this
            it.eagerIndex = idx
        }
        Collections.unmodifiableList(exercises)
    }

    List<Exercise> getAllActiveExercises() {
        def subject = this
        List<Exercise> exercises = SubjectExercise.withCriteria() {
            eq("subject", subject)
            order("weight", "asc")
            exercise {
                not {
                    eq("localStatus", NodeStatus.TRASH)
                }
            }
        }.exercise
        exercises.eachWithIndex { it, idx ->
            it.eagerlyLoadedParent = this
            it.eagerIndex = idx
        }
        Collections.unmodifiableList(exercises)
    }

    List<Exercise> getAllExercisesByStatus(NodeStatus status) {
        def subject = this
        List<Exercise> exercises = SubjectExercise.withCriteria() {
            eq("subject", subject)
            order("weight", "asc")
            exercise {
                eq("localStatus", status)
            }
        }.exercise
        exercises.eachWithIndex { it, idx ->
            it.eagerlyLoadedParent = this
            it.eagerIndex = idx
        }
        Collections.unmodifiableList(exercises)
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
    Node loadParent() {
        CourseSubject.findBySubject(this).course
    }
}
