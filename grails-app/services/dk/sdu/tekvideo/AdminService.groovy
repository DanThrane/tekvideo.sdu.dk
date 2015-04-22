package dk.sdu.tekvideo

import dk.sdu.tekvideo.events.VisitVideoEvent
import grails.gorm.DetachedCriteria
import grails.plugin.springsecurity.SpringSecurityService
import grails.transaction.Transactional

@Transactional
class AdminService {

    TeachingService teachingService
    SpringSecurityService springSecurityService

    List<VisitVideoEvent> retrieveVideoEvents(VideoStatisticsCommand cmd) {
        def teacher = Teacher.findByUser(springSecurityService.currentUser)
        def data = teachingService.getCompleteData(teacher.user.username, cmd.course, cmd.subject, cmd.video)

        def criteria = VisitVideoEvent.where {
            course in teacher.courses
            if (cmd.showOnlyStudents) {
                user != null
            }
            if (cmd.course != null) {
                course.name == cmd.course
            }
            if (cmd.subject != null) {
                subject == cmd.subject
            }
            if (cmd.video != null && data != null) {
                video == data.video
            }
        }
        return criteria.findAll()
    }
}
