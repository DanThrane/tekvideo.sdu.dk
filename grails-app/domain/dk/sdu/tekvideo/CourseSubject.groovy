package dk.sdu.tekvideo

class CourseSubject {
    Course course
    Subject subject
    Integer index

    static constraints = {
        course nullable: false
        subject nullable: false
        index nullable: false, min: 0
    }
}
