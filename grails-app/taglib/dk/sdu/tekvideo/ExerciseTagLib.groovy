package dk.sdu.tekvideo

class ExerciseTagLib {
    static namespace = "exercise"

    def comments = { attrs, body ->
        def exercise = attrs.remove("exercise")
        out << render(template: "/exercises/comments", model: [exercise: exercise])
    }

}
