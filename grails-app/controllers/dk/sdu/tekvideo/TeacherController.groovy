package dk.sdu.tekvideo

import org.springframework.security.access.annotation.Secured

class TeacherController {

    TeachingService teachingService

    @Secured("permitAll")
    def list(String teacherName) {
        Teacher teacher = teachingService.getTeacher(teacherName)
        if (teacher) {
            [teacher: teacher, courses: teacher.courses]
        } else {
            render status: 404
        }
    }
}
