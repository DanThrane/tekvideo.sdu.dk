package dk.sdu.tekvideo

class CourseService {
    def userService

    boolean canAccess(Course course) {
        def status = course?.status

        course != null &&
                (status == NodeStatus.VISIBLE ||
                        (status == NodeStatus.INVISIBLE &&
                                userService.authenticatedTeacher == course.teacher))
    }

    int getStudentCount(Course course) {
        CourseStudent.countByCourse(course)
    }

    List<Course> listVisibleCourses() {
        Course.findAllByLocalStatus(NodeStatus.VISIBLE)
    }

    List<Course> listActiveCourses() {
        Course.findAllByLocalStatusNotEqual(NodeStatus.TRASH)
    }

    List<Course> listAllCoursesInTrash() {
        Course.findAllByLocalStatus(NodeStatus.TRASH)
    }

    List<Course> listAllInivisbleCourses() {
        Course.findAllByLocalStatus(NodeStatus.INVISIBLE)
    }
}
