package dk.sdu.ivids

class TimelineItem {
    String title
    Integer timecode

    static hasMany = [questions: Question]
    static belongsTo = Timeline
    static constraints = {
        title nullable: false, blank: false
        timecode nullable: false
    }
}
