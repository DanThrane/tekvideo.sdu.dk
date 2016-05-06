package dk.sdu.tekvideo

class SubjectService {
    def userService

    boolean canAccess(Subject subject) {
        def status = subject?.status

        subject != null &&
                (status == NodeStatus.VISIBLE ||
                    (status == NodeStatus.INVISIBLE && userService.authenticatedTeacher == subject.course.teacher))
    }
}
