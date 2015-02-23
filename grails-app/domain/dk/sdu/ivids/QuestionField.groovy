package dk.sdu.ivids

class QuestionField {
    String name
    String answer // This is actual Javascript code. TODO Find a good way to store this.
    Integer topOffset
    Integer leftOffset

    static belongsTo = Question
    static constraints = {
        name nullable: false, blank: false
        answer nullable: false, blank: false
        topOffset nullable: false
        leftOffset nullable: false
    }
}
