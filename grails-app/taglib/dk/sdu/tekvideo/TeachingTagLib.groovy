package dk.sdu.tekvideo

import static dk.sdu.tekvideo.TagLibUtils.*
import static dk.sdu.tekvideo.TagLibUtils.fail

class TeachingTagLib {
    static namespace = "sdu"

    def urlMappingService

    def createLinkToTeacher = { attrs, body ->
        Teacher teacher = attrs.remove("teacher") ?: fail("teacher", "sdu:createLinkToTeacher")
        out << urlMappingService.generateLinkToTeacher(teacher, attrs)
    }

    def createLinkToCourse = { attrs, body ->
        Course course = attrs.remove("course") ?: fail("course", "sdu:createLinkToCourse") as Course
        out << urlMappingService.generateLinkToCourse(course, attrs)
    }

    def createLinkToSubject = { attrs, body ->
        Subject subject = attrs.remove("subject") ?: fail("subject", "sdu:createLinkToSubject") as Subject
        out << urlMappingService.generateLinkToSubject(subject, attrs)
    }

    def createLinkToVideo = { attrs, body ->
        Video video = attrs.remove("video") ?: fail("video", "sdu:createLinkToVideo") as Video
        out << urlMappingService.generateLinkToVideo(video, attrs)
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
