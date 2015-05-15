package dk.sdu.tekvideo

class VideoController {

    TeachingService teachingService

    def view(String teacherName, String courseName, String subjectName, Integer videoId) {
        Video video = teachingService.getVideo(teacherName, courseName, subjectName, videoId)
        boolean debugMode = params.containsKey("debug")
        if (video) {
            [video: video, debugMode: debugMode]
        } else {
            render status: 404, text: "Unable to find video!"
        }
    }

}
