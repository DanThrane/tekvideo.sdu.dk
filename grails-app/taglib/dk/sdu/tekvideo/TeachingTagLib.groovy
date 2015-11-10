package dk.sdu.tekvideo

import static dk.sdu.tekvideo.TagLibUtils.*
import static dk.sdu.tekvideo.TagLibUtils.fail

class TeachingTagLib {
    static namespace = "sdu"

    private Map prepareAttributes(String teacher, Map attrs) {
        def base = [
                mapping: "teaching",
                params : [teacher: teacher]
        ]

        base.putAll(attrs)
        return base
    }

    private Map prepareCourseAttributes(Course course, Map attrs) {
        def res = prepareAttributes(course.teacher.toString(), attrs)
        res.params.course = course.name
        res.params.year = course.year
        res.params.fall = course.spring ? "0" : "1"
        return res
    }

    private Map prepareSubjectAttributes(Subject subject, Map attrs) {
        def res = prepareCourseAttributes(subject.course, attrs)
        res.params.subject = subject.name
        return res
    }

    private Map prepareVideoAttributes(Video video, Map attrs) {
        def res = prepareSubjectAttributes(video.subject, attrs)
        res.params.vidid = video.videos_idx
        return res
    }

    def createLinkToTeacher = { attrs, body ->
        def teacher = attrs.remove("teacher") ?: fail("teacher", "sdu:createLinkToTeacher") as Course
        out << createLink(prepareAttributes(teacher.toString(), attrs))
    }

    def createLinkToCourse = { attrs, body ->
        Course course = attrs.remove("course") ?: fail("course", "sdu:createLinkToCourse") as Course
        out << createLink(prepareCourseAttributes(course, attrs))
    }

    def createLinkToSubject = { attrs, body ->
        Subject subject = attrs.remove("subject") ?: fail("subject", "sdu:createLinkToSubject") as Subject
        out << createLink(prepareSubjectAttributes(subject, attrs))
    }

    def createLinkToVideo = { attrs, body ->
        Video video = attrs.remove("video") ?: fail("video", "sdu:createLinkToVideo") as Video
        def attributes = prepareVideoAttributes(video, attrs)
        out << createLink(attributes)
    }

    def linkToTeacher = { attrs, body ->
        assistAutoComplete(attrs.teacher)
        out << "<a href='"
        out << createLinkToTeacher(attrs)
        out << "'>"
        out << body()
        out << "</a>"
    }

    def linkToCourse = { attrs, body ->
        assistAutoComplete(attrs.course)
        out << "<a href='"
        out << createLinkToCourse(attrs)
        out << "'>"
        out << body()
        out << "</a>"
    }

    def linkToSubject = { attrs, body ->
        assistAutoComplete(attrs.subject)
        out << "<a href='"
        out << createLinkToSubject(attrs)
        out << "'>"
        out << body()
        out << "</a>"
    }

    def linkToVideo = { attrs, body ->
        assistAutoComplete(attrs.video)
        out << "<a href='"
        out << createLinkToVideo(attrs)
        out << "'>"
        out << body()
        out << "</a>"
    }

}
