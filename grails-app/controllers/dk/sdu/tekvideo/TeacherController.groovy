package dk.sdu.tekvideo

class TeacherController {

    def list(String teacherName) {
        Teacher teacher = Teacher.findByUser(User.findByUsername(teacherName))
        if (teacher) {
            [teacher: teacher, courses: teacher.courses]
        } else {
            render status: 404
        }
    }
}
