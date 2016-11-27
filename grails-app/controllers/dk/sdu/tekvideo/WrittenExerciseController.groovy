package dk.sdu.tekvideo

import org.springframework.security.access.annotation.Secured

class WrittenExerciseController {
    def exerciseService

    @Secured("permitAll")
    def view(WrittenExercise exercise) {
        if (exerciseService.canAccess(exercise)) {
            render view: "view", model: [exercise: exercise]
        } else {
            render status: 404, text: "Unable to find exercise!"
        }
    }

}
