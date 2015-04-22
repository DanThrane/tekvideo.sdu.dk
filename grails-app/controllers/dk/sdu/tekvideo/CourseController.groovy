package dk.sdu.tekvideo

class CourseController {
    static defaultAction = "list"

    TeachingService teachingService

    def list() {
        List<Course> courses = Course.list()
        [courses: courses]
    }

    def viewByTeacher(String teacherName, String courseName) {
        Course course = teachingService.getCourse(teacherName, courseName)
        if (course) {
            render(view: "view", model: [course: course])
        } else {
            render status: "404", text: "Course not found!"
        }
    }
}
