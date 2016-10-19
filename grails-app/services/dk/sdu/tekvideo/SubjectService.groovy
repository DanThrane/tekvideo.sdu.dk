package dk.sdu.tekvideo

class SubjectService {
    def userService
    def videoService
    def urlMappingService

    boolean canAccess(Subject subject) {
        def status = subject?.status

        subject != null &&
                (status == NodeStatus.VISIBLE ||
                    (status == NodeStatus.INVISIBLE && userService.authenticatedTeacher == subject.course.teacher))
    }

    String getThumbnail(Subject subject) {
        return videoService.getThumbnail((Video) subject.activeVideos?.find { it instanceof Video })
    }

    List<Map> subjectForBrowser(Subject subject) {
        def videos = subject.visibleVideos

        def breadcrumbs = []
        def course = subject.course
        def teacher = course.teacher
        breadcrumbs.add([
                title: teacher.toString(),
                url: urlMappingService.generateLinkToTeacher(teacher)
        ])
        breadcrumbs.add([
                title: course.name,
                url: urlMappingService.generateLinkToCourse(course)
        ])
        breadcrumbs.add([
                title: subject.name,
                url: urlMappingService.generateLinkToSubject(subject)
        ])

        return videos.collect {
            def result = [:]
            result.title = it.name
            result.description = it.description
            result.stats = []
            result.featuredChildren = []
            result.breadcrumbs = breadcrumbs

            if (it instanceof Video) {
                result.thumbnail = videoService.getThumbnail(it)
                result.url = urlMappingService.generateLinkToVideo(it)
            }

            return result
        }
    }
}
