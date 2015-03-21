package dk.sdu.tekvideo

import dk.sdu.tekvideo.Course

class CourseController {
    static defaultAction = "list"

    def list() {
        List<Course> courses = Course.list() // TODO Only show the courses for the authorized student
        [courses: courses]
    }

    def view(Course course) {
        [course: course]
    }
}
