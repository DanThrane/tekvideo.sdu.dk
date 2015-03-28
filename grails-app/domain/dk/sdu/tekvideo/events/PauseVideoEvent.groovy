package dk.sdu.tekvideo.events

class PauseVideoEvent extends Event {
    String video
    Long timecode

    static constraints = {
        timecode min: 0L
        video maxSize: 512
    }
}
