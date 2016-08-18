package dk.sdu.tekvideo.data

import dk.sdu.tekvideo.Subject
import dk.sdu.tekvideo.SubjectVideo
import dk.sdu.tekvideo.Video
import grails.converters.JSON

import java.util.concurrent.atomic.AtomicInteger

class VideoData {
    private static AtomicInteger idx = new AtomicInteger(0)

    static Video buildTestVideoWithTimeline(Map args) {
        String prefix = args.remove("prefix") ?: "Video"
        Subject subject = args.remove("subject") as Subject ?: SubjectData.buildTestSubject()
        Boolean includeIdSuffix = args.remove("includeIdSuffix")
        if (includeIdSuffix == null) includeIdSuffix = false
        Boolean save = args.remove("save")
        if (save == null) save = true

        Integer subjectCount = args.remove("subjectCount") ?: 1
        Integer questionPerSubject = args.remove("questionPerSubject") ?: 1
        Integer fieldsPerQuestion = args.remove("fieldsPerQuestion") ?: 1


        def video = buildTestVideo(prefix, subject, includeIdSuffix, false)
        def timeline = []
        for (def i in 0..<subjectCount) {
            def currentSubject = [
                    title: "Subject/$i",
                    timecode: 0
            ]
            def currentQuestions = []
            for (def j in 0..<questionPerSubject) {
                def currentQuestion = [
                        timecode: 0,
                        title: "Question/$i/$j"
                ]
                def currentFields = []
                for (def k in 0..<fieldsPerQuestion) {
                    currentFields.add([
                            answer: [type: "expression", value: 0],
                            leftoffset: 0,
                            topoffset: 0,
                            name: "Field/$i/$j/$k"
                    ])
                }
                currentQuestion.fields = currentFields
                currentQuestions.add(currentQuestion)
            }
            currentSubject["questions"] = currentQuestions
            timeline.add(currentSubject)
        }
        def v = timeline as JSON
        video.timelineJson = v.toString(true)
        if (save) {
            video.save(flush: true, failOnError: true)
            SubjectVideo.create(subject, video, [save: true, failOnError: true, flush: true])
        }
        println(video.timelineJson)
        println(video.subject)
        return video
    }

    static Video buildTestVideo(String prefix = "Video", Subject subject = null, boolean includeIdSuffix = false,
                                boolean save = true) {
        if (subject == null) subject = SubjectData.buildTestSubject()

        String name = prefix
        if (includeIdSuffix) name += idx.getAndIncrement()

        def video = new Video([
                name        : name,
                youtubeId   : "f2J9N7wgYas",
                timelineJson: "[]"
        ])

        if (save) {
            video.save(failOnError: true, flush: true)
            SubjectVideo.create(subject, video, [save: true, failOnError: true, flush: true])
        }

        return video
    }
}
