package dk.sdu.tekvideo

import org.springframework.security.access.annotation.Secured

class WrittenExerciseController {
    def springSecurityService
    def nodeService

    @Secured("permitAll")
    def view(WrittenExerciseGroup exercise) {
        if (nodeService.canView(exercise)) {
            def completed = []
//            def user = springSecurityService.currentUser
            // TODO @event
            // TODO @event
            // TODO @event
//            if (user != null) {
//                def all = CompletedWrittenExercise.findAllByUserAndGroupId(user, exercise.id)
//                completed = all.exerciseId
//            }
            render view: "view", model: [exercise: exercise, subExercises: exercise.exercises, completed: completed]
        } else {
            render status: 404, text: "Unable to find exercise!"
        }
    }

}
