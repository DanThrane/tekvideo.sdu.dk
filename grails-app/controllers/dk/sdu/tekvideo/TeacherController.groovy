package dk.sdu.tekvideo

class TeacherController {

    TeachingService teachingService

    def list(String teacherName) {
        Teacher teacher = teachingService.getTeacher(teacherName)
        if (teacher) {
            [teacher: teacher, courses: teacher.courses]
        } else {
            render status: 404
        }
    }
}
