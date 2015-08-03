package dk.sdu.tekvideo

class CourseService {
    int getStudentCount(Course course) {
        CourseStudent.countByCourse(course)
    }
}
