package dk.sdu.tekvideo

class SubjectController {

    TeachingService teachingService

    def viewByTeacherAndCourse(String teacherName, String courseName, String subjectName) {
        Subject subject = teachingService.getSubject(teacherName, courseName, subjectName)
        if (subject) {
            render view: "view", model: [subject: subject]
        } else {
            render status: 404, text: "Unable to find subject!"
        }
    }
}
