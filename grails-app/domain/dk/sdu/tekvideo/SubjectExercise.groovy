package dk.sdu.tekvideo

class SubjectExercise {
    Subject subject
    Exercise exercise
    Integer weight

    static SubjectExercise create(Subject subject, Exercise exercise, Map params) {
        Integer weight = params.weight
        Boolean save = params.save ?: false
        Boolean flush = params.flush ?: true
        Boolean failOnError = params.failOnError ?: false

        if (weight == null) {
            weight = countBySubject(subject)
        }

        def result = new SubjectExercise(subject: subject, exercise: exercise, weight: weight)
        if (save) {
            result.save(flush: flush, failOnError: failOnError)
        }
        return result
    }

    static constraints = {
        subject nullable: false
        exercise nullable: false
        weight nullable: false, min: 0
    }
}
