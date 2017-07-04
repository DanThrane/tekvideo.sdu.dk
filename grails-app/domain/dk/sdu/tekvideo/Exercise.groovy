package dk.sdu.tekvideo

abstract class Exercise implements Node {
    String name
    String description = "Ingen beskrivelse"
    Date dateCreated
    NodeStatus localStatus = NodeStatus.VISIBLE

    static mapping = {
        description type: "text"
        version false
    }

    static hasMany = [comments: Comment, similarResources: SimilarResources]

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

    abstract int getScoreToPass()

}
