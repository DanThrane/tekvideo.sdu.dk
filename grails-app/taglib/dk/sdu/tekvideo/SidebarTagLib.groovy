package dk.sdu.tekvideo

class SidebarTagLib {
    static namespace = "sdu"

    StudentService studentService
    CourseManagementService courseManagementService
    TeachingService teachingService

    def leftSidebar = { attrs, body ->
        // Not a fan of implementing this as a tag lib. Really feels like it should be in some controller which
        // then delegates to the real one. This is however a lot easier to create

        def student = studentService.authenticatedStudent
        def teacher = teachingService.authenticatedTeacher
        List<Course> courses =
                (student) ? studentService.getAllCourses(student) as List<Course> :
                        (teacher) ? courseManagementService.activeCourses.result :
                                []

        out << render(template: "/sidebar/left", model: [courses: courses])
    }
}
