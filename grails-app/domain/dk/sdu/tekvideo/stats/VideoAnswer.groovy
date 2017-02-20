package dk.sdu.tekvideo.stats

class VideoAnswer {
    ExerciseProgress progress
    Date dateCreated
    Boolean correct
    Integer subject
    Integer question

    static mapping = {
        version false
    }
}
