package dk.sdu.tekvideo.events

class SkipToContentEvent extends Event {
    String video
    String label
    Long skippedAt
    Long videoTimeCode

    static constraints = {
        video blank: false, maxSize: 512
        label blank: false, maxSize: 512
        videoTimeCode min: 0L
        skippedAt min: 0L
    }
}
