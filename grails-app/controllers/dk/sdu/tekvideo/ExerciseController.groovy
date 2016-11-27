package dk.sdu.tekvideo

import org.springframework.security.access.annotation.Secured

class ExerciseController {
    UrlMappingService urlMappingService
    ExerciseService exerciseService

    @Secured("permitAll")
    def view(String teacherName, String courseName, String subjectName, Integer videoId, Integer year, Boolean spring) {
        Exercise exercise = urlMappingService.getExercise(teacherName, courseName, subjectName, videoId, year, spring)
        if (exerciseService.canAccess(exercise)) {
            if (exercise instanceof Video) {
                forward controller: "video", action: "viewV", params: [id: exercise.id]
            } else if (exercise instanceof WrittenExercise) {
                forward controller: "writtenExercise", action: "view", params: [id: exercise.id]
            } else {
                render status: 500, text: "Unable to view exercise"
            }
        } else {
            render status: 404, text: "Unable to find exercise!"
        }
    }
}
