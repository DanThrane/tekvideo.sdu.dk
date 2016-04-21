package dk.sdu.tekvideo

class SidebarTagLib {
    static namespace = "sdu"

    UserService userService
    StudentService studentService
    CourseManagementService courseManagementService

    def leftSidebar = { attrs, body ->
        // Not a fan of implementing this as a tag lib. Really feels like it should be in some controller which
        // then delegates to the real one. This is however a lot easier to create

        def student = studentService.authenticatedStudent
        def teacher = userService.authenticatedTeacher
        List<Course> courses =
                (student) ? studentService.getAllCourses(student) as List<Course> :
                        (teacher) ? courseManagementService.activeCourses.result :
                                []

        out << render(template: "/sidebar/left", model: [courses: courses])
    }
}
