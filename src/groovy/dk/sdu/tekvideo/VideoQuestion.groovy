package dk.sdu.tekvideo

import dk.sdu.tekvideo.events.AnswerQuestionEvent

class VideoQuestion {
    Integer timelineId
    List<VideoField> fields
    Integer timecode
    String title
    VideoSubject parent

    Map<VideoField, Boolean> grade(List<AnswerQuestionEvent> answerEvents) {
        Map<VideoField, Boolean> result = [:]
        fields.each { result[it] = false }
        answerEvents.each {
            assert(it.question == timelineId)
            assert(it.subject == parent.timelineId)

            if (it.field < fields.size()) {
                def field = fields[it.field]
                if (it.correct) {
                    result[field] = true
                }
            }
        }
        return result
    }

    static VideoQuestion fromMap(Integer timelineId, VideoSubject parent, Map<String, Object> map) {
        def question = new VideoQuestion()

        def rawFields = (map.fields ?: []) as List<Map>
        List<VideoField> fields = []
        rawFields.eachWithIndex { Map entry, int i ->
            fields.add(VideoField.fromMap(i, question, entry))
        }

        question.timelineId = timelineId
        question.title = map.title
        question.timecode = map.timecode as Integer
        question.fields = fields
        question.parent = parent
        return question
    }

    @Override
    String toString() {
        return "VideoQuestion{" +
                "fields=" + fields +
                ", timecode=" + timecode +
                ", title='" + title + '\'' +
                '}';
    }
}
