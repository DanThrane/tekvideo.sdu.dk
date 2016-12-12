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
        def exercises = subject.allVisibleExercises

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

        return exercises.collect {
            def result = [:]
            result.title = it.name
            result.description = it.description
            result.stats = []
            result.featuredChildren = []
            result.breadcrumbs = breadcrumbs
            result.url = urlMappingService.generateLinkToExercise(it)

            if (it instanceof Video) {
                result.thumbnail = videoService.getThumbnail(it)
            } else if (it instanceof WrittenExerciseGroup) {
                result.thumbnail = it.thumbnailUrl
            }
            return result
        }
    }
}
