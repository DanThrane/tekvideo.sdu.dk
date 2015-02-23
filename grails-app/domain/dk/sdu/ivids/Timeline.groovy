package dk.sdu.ivids

class Timeline {
    static belongsTo = Video
    static hasMany = [items: TimelineItem]

    static constraints = {
    }
}
