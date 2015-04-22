package dk.sdu.tekvideo

import dk.sdu.tekvideo.events.VisitVideoEvent
import grails.plugin.springsecurity.annotation.Secured
import grails.validation.Validateable

@Secured("ROLE_TEACHER")
class AdminController {

    AdminService adminService

    def index() { }

    def videoStatistics(VideoStatisticsCommand cmd) {
        println cmd
        if (cmd.hasErrors()) {
            log.warn cmd.errors
            render status: 400, text: "Request had errors"
        } else {
            [events: adminService.retrieveVideoEvents(cmd)]
        }
    }
}

@Validateable
class VideoStatisticsCommand {
    String course
    String subject
    Integer video
    Boolean showOnlyStudents = false

    static constraints = {
        course nullable: true, blank: false
        subject nullable: true, blank: false
        video nullable: true, blank: false
        showOnlyStudents nullable: false
    }


    @Override
    public String toString() {
        return "VideoStatisticsCommand{" +
                "course='" + course + '\'' +
                ", subject='" + subject + '\'' +
                ", video=" + video +
                ", showOnlyStudents=" + showOnlyStudents +
                '}';
    }
}