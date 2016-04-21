package dk.sdu.tekvideo

import org.springframework.security.access.annotation.Secured

class TeacherController {

    UrlMappingService urlMappingService

    @Secured("permitAll")
    def list(String teacherName) {
        Teacher teacher = urlMappingService.getTeacher(teacherName)
        if (teacher) {
            [teacher: teacher, courses: teacher.courses]
        } else {
            render status: 404
        }
    }
}
