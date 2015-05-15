package dk.sdu.tekvideo

class TeachingController {
    def index(String teacher, String course, String subject, Integer vidid) {
        // Do some logic, and forward the user to the correct controller etc
        if (course == null) {
            forward controller: "teacher", action: "list", params: [teacherName: teacher]
        } else if (subject == null) {
            forward controller: "course", action: "viewByTeacher", params: [teacherName: teacher, courseName: course]
        } else if (vidid == null) {
            forward controller: "subject", action: "viewByTeacherAndCourse", params: [teacherName: teacher,
                courseName: course, subjectName: subject]
        } else {
            forward controller: "video", action: "view", params: [teacherName: teacher,
                courseName: course, subjectName: subject, videoId: vidid]
        }
    }
}
