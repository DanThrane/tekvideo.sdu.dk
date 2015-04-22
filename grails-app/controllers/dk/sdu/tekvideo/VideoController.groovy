package dk.sdu.tekvideo

class VideoController {

    TeachingService teachingService

    def viewByTeaching(String teacherName, String courseName, String subjectName, Integer videoId) {
        Video video = teachingService.getVideo(teacherName, courseName, subjectName, videoId)
        if (video) {
            render view: "view", model: [video: video]
        } else {
            render status: 404, text: "Unable to find video!"
        }
    }

}
