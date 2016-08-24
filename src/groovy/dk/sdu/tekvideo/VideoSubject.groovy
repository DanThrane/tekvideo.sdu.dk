package dk.sdu.tekvideo

class VideoSubject {
    Integer timelineId
    List<VideoQuestion> questions
    Integer timecode
    String title

    static VideoSubject fromMap(Integer timelineId, Map<String, Object> map) {
        def subject = new VideoSubject()

        def rawQuestions = (map.questions ?: []) as List<Map>
        List<VideoQuestion> questions = []
        rawQuestions.eachWithIndex { Map entry, int i ->
            questions.add(VideoQuestion.fromMap(i, subject, entry))
        }

        subject.timelineId =  timelineId
        subject.title =  map.title
        subject.timecode =  map.timecode as Integer
        subject.questions =  questions
        return subject
    }

    @Override
    String toString() {
        return "VideoSubject{" +
                "questions=" + questions +
                ", timecode=" + timecode +
                ", title='" + title + '\'' +
                '}';
    }
}
