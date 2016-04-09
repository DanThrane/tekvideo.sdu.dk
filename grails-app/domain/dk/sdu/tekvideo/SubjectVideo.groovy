package dk.sdu.tekvideo

class SubjectVideo {
    Subject subject
    Video video
    Integer index

    static constraints = {
        subject nullable: false
        video nullable: false
        index nullable: false, min: 0
    }
}
