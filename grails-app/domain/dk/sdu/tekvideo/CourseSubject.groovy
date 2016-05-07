package dk.sdu.tekvideo

class CourseSubject {
    Course course
    Subject subject
    Integer weight

    static CourseSubject create(Course course, Subject subject, Map params) {
        Integer weight = params.weight
        Boolean save = params.save ?: false

        if (weight == null) {
            weight = countByCourse(course)
        }

        def result = new CourseSubject(course: course, subject: subject, weight: weight)
        if (save) {
            result.save()
        }
        return result
    }

    static List<Long> findSubjectIds(Course course) { // TODO Change for performance
        findAllByCourse(course, [sourt: 'weight']).subject.id
    }

    static constraints = {
        course nullable: false
        subject nullable: false
        weight nullable: false, min: 0
    }
}
