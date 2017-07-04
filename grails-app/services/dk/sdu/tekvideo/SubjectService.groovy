package dk.sdu.tekvideo

class SubjectService implements ContainerNodeInformation<Subject, Exercise> {
    def videoService
    def urlMappingService
    def courseService

    @Override
    String getThumbnail(Subject subject) {
        // TODO Only covers videos
        return videoService.getThumbnail((Video) subject.activeVideos?.find { it instanceof Video })
    }

    @Override
    List<NodeBrowserCrumbs> getBreadcrumbs(Subject subject) {
        def teacher = subject.course.teacher
        [
                new NodeBrowserCrumbs(
                        teacher.toString(),
                        urlMappingService.generateLinkToTeacher(teacher)
                ),
                new NodeBrowserCrumbs(
                        subject.course.name,
                        urlMappingService.generateLinkToCourse(subject.course)
                )
        ]
    }

    @Override
    NodeBrowserInformation getInformationForBrowser(Subject it, boolean addBreadcrumbs) {
        List<NodeBrowserCrumbs> breadcrumbs = addBreadcrumbs ? courseService.getBreadcrumbs(it.course) : []

        return new NodeBrowserInformation(
                it.name,
                it.description,
                getThumbnail(it),
                urlMappingService.generateLinkToSubject(it),
                breadcrumbs
        )
    }

    @Override
    List<Exercise> listVisibleChildren(Subject subject) {
        subject.allVisibleExercises
    }

    @Override
    List<Exercise> listActiveChildren(Subject subject) {
        subject.allActiveExercises
    }
}
