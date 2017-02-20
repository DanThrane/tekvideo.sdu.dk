package dk.sdu.tekvideo

import static dk.sdu.tekvideo.ServiceResult.ok

import grails.transaction.Transactional
import org.hibernate.SessionFactory

/**
 * @author Dan Thrane
 */
@Transactional
class VideoStatisticsService {
    ServiceResult<Map> retrieveViewBreakdown(Video video) {
        // TODO @event
        // TODO @event
        // TODO @event
        return ok(item: [visits: 0, studentVisits: 0, guestVisits: 0])
    }
}
