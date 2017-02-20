package dk.sdu.tekvideo.stats

class VideoAnswer {
    VideoProgress progress
    Date dateCreated
    Boolean correct
    Integer subject
    Integer question

    static mapping = {
        version false
    }
}
