package dk.sdu.tekvideo

class VideoController {

    def view(Video video) {
        [video: video]
    }

    def viewByTeaching(String teacherName, String courseName, String subjectName, Integer videoId) {
        Teacher teacher = Teacher.findByUser(User.findByUsername(teacherName))
        Course course = Course.findByNameAndTeacher(courseName, teacher)
        Subject subject = Subject.findByNameAndCourse(subjectName, course)
        if (subject) {
            if (videoId < subject.videos.size()) {
                Video video = subject.videos[videoId]
                render view: "view", model: [video: video]
            } else {
                render status: 404, text: "Unable to find video!"
            }
        } else {
            render status: 404, text: "Unable to find video!"
        }
    }

}
