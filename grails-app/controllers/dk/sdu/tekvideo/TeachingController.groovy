package dk.sdu.tekvideo

class TeachingController {
    def index(String teacher, String course, String subject, String vidid) {
        // Do some logic, and forward the user to the correct controller etc
        if (course == null) {
            render "$teacher"
        } else if (subject == null) {
            render "$teacher, $course"
        } else if (vidid == null) {
            render "$teacher, $course, $subject"
        } else {
            render "$teacher, $course, $subject, $vidid"
        }
    }
}
