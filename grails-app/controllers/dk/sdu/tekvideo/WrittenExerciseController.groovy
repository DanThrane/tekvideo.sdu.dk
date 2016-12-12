package dk.sdu.tekvideo

import dk.sdu.tekvideo.events.CompletedWrittenExercise
import org.springframework.security.access.annotation.Secured

class WrittenExerciseController {
    def exerciseService
    def springSecurityService

    @Secured("permitAll")
    def view(WrittenExerciseGroup exercise) {
        if (exerciseService.canAccess(exercise)) {
            def completed = []
            def user = springSecurityService.currentUser
            if (user != null) {
                def all = CompletedWrittenExercise.findAllByUserAndGroupId(user, exercise.id)
                completed = all.exerciseId
            }
            render view: "view", model: [exercise: exercise, subExercises: exercise.exercises, completed: completed]
        } else {
            render status: 404, text: "Unable to find exercise!"
        }
    }

}
