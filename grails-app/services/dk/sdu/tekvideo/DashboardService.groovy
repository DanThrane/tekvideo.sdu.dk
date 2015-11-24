package dk.sdu.tekvideo

import grails.transaction.Transactional

@Transactional
class DashboardService {

    ServiceResult<Video> retrieveListOfPopularVideos(Map<String, Object> attrs) {
        Set<Course> courses = []


    }

}
