package dk.sdu.tekvideo

import static dk.sdu.tekvideo.ServiceResult.*

class StatsService {
    def courseManagementService
    def nodeService

    ServiceResult<List<NodeBrowserInformation>> coursesForBrowser() {
        def coursesResult = courseManagementService.activeCourses
        if (!coursesResult.success) return coursesResult.convertFailure()
        // TODO Make the links point the correct place :-)
        return ok(coursesResult.result.collect { nodeService.getInformationForBrowser(it) })
    }
}
