package dk.sdu.tekvideo

import dk.sdu.tekvideo.events.AnswerQuestionEvent
import dk.sdu.tekvideo.events.VisitVideoEvent

class VideoSubject {
    Integer timelineId
    List<VideoQuestion> questions
    Integer timecode
    String title
    VideoTimeline parent

    NodeIdentifier getIdentifier() {
        parent.video.identifier.child("videosubject", timelineId)
    }

    static VideoSubject fromMap(Integer timelineId, VideoTimeline parent, Map<String, Object> map) {
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
        subject.parent = parent
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
