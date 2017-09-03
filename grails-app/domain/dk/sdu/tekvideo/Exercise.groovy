package dk.sdu.tekvideo

abstract class Exercise implements Node {
    String name
    String description = "Ingen beskrivelse"
    Date dateCreated
    NodeStatus localStatus = NodeStatus.VISIBLE
    int eagerIndex = -1

    static mapping = {
        description type: "text"
        version false
    }

    static hasMany = [comments: Comment, similarResources: SimilarResources]
    static transients = ['eagerlyLoadedParent', 'eagerIndex']

    static constraints = {
        name            nullable: false, blank: false, maxSize: 255
        description     nullable: true
    }

    String getDescription() {
        if (description == null) return "Ingen beskrivelse"
        return description
    }

    Subject getSubject() {
        return parent as Subject
    }

    int getExerciseIndex() {
        if (eagerIndex == -1) {
            subject.allVisibleExercises.collect { it.id }.indexOf(id)
        }
        return eagerIndex
    }

    abstract int getScoreToPass()

}
