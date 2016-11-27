package dk.sdu.tekvideo

class ExerciseService {
    def userService

    boolean canAccess(Exercise video) {
        def status = video?.status

        video != null &&
                (status == NodeStatus.VISIBLE ||
                        (status == NodeStatus.INVISIBLE &&
                                userService.authenticatedTeacher == video.subject.course.teacher))
    }
}
