package dk.sdu.tekvideo

class CourseController {
    static defaultAction = "list"

    def list() {
        List<Course> courses = Course.list()
        [courses: courses]
    }

    def view(Course course) {
        [course: course]
    }
}
