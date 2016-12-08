package dk.sdu.tekvideo

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

class ExercisePlaygroundController {
    static allowedMethods = [editor: "GET", demo: "GET", show: "GET", createExercise: "POST"]

    @Secured("ROLE_TEACHER")
    def editor() {

    }

    @Secured("permitAll")
    def demo() {
        def all = TemporaryExercise.where{true}.list(sort: 'name')
        [all: all]
    }

    @Secured("ROLE_TEACHER")
    def createExercise(CreateExerciseCommand command) {
        def output = TemporaryExercise.findByName(command.name)
        def exists = output != null
        if (!exists) output = new TemporaryExercise()
        output.name = command.name
        output.exercise = command.exercise

        if (output.validate()) {
            output.save(flush: true)
            render([message: "OK"] as JSON)
        } else {
            response.status = 500
            render([message: "Der skete en fejl"] as JSON)
        }
    }

    @Secured("permitAll")
    def show(String id) {
        // TODO FIXME THIS MAKES THE ASSUMPTION THAT THE TEACHER IS NOT MALICIOUS.
        // THE DOCUMENT PAGE IS VULNERABLE TO A BUNCH OF BAD THINGS IF THIS IS NOT TRUE.
        println(id)
        println TemporaryExercise.findAll().collect { it.name }
        def exercise = TemporaryExercise.findByName(id)
        if (exercise) {
            [exercise: exercise]
        } else {
            render status: 404, text: "Not found"
        }
    }
}
