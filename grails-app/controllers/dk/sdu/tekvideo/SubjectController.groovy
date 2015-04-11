package dk.sdu.tekvideo

class SubjectController {

    def view(Subject subject) {
        [subject: subject]
    }

    def viewByTeacherAndCourse(String teacherName, String courseName, String subjectName) {
        Teacher teacher = Teacher.findByUser(User.findByUsername(teacherName))
        Course course = Course.findByNameAndTeacher(courseName, teacher)
        Subject subject = Subject.findByNameAndCourse(subjectName, course)
        if (subject) {
            render view: "view", model: [subject: subject]
        } else {
            render status: 404, text: "Unable to find subject!"
        }
    }
}
