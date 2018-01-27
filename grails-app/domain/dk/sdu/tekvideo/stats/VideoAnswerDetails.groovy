package dk.sdu.tekvideo.stats

class VideoAnswerDetails {
    VideoAnswer parent
    String answer
    Boolean correct
    Integer field

    static mapping = {
        version false
    }
}
