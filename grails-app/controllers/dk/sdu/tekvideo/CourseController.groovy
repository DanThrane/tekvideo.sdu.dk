package dk.sdu.tekvideo

class CourseController {
    static defaultAction = "list"

    def list() {
        List<Course> courses = Course.list()
        [courses: courses]
    }

    def viewByTeacher(String teacherName, String courseName) {
        Teacher teacher = Teacher.findByUser(User.findByUsername(teacherName))
        Course course = Course.findByNameAndTeacher(courseName, teacher)
        if (course) {
            render(view: "view", model: [course: course])
        } else {
            render status: "404", text: "Course not found!"
        }
    }
}
