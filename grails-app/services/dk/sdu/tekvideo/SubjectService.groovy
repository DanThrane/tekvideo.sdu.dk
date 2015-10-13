package dk.sdu.tekvideo

class SubjectService {
    def teachingService

    boolean canAccess(Subject subject) {
        def status = subject?.status

        subject != null &&
                (status == NodeStatus.VISIBLE ||
                    (status == NodeStatus.INVISIBLE && teachingService.authenticatedTeacher == subject.course.teacher))
    }
}
