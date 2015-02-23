package dk.sdu.ivids

class Question {
    String title
    Integer timecode

    static hasMany = [fields: QuestionField]
    static belongsTo = TimelineItem
    static constraints = {
        title nullable: false, blank: false
        timecode nullable: false
    }
}
