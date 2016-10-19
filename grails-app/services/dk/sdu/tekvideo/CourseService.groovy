package dk.sdu.tekvideo

class CourseService {
    def userService
    def urlMappingService
    def videoService
    def subjectService

    boolean canAccess(Course course) {
        def status = course?.status

        course != null &&
                (status == NodeStatus.VISIBLE ||
                        (status == NodeStatus.INVISIBLE &&
                                userService.authenticatedTeacher == course.teacher))
    }

    int getStudentCount(Course course) {
        CourseStudent.countByCourse(course)
    }

    List<Course> listVisibleCourses() {
        Course.findAllByLocalStatus(NodeStatus.VISIBLE)
    }

    List<Course> listActiveCourses() {
        Course.findAllByLocalStatusNotEqual(NodeStatus.TRASH)
    }

    List<Course> listAllCoursesInTrash() {
        Course.findAllByLocalStatus(NodeStatus.TRASH)
    }

    List<Course> listAllInivisbleCourses() {
        Course.findAllByLocalStatus(NodeStatus.INVISIBLE)
    }

    String getThumbnailForCourse(Course course) {
        // TODO Performance
        return videoService.getThumbnail((Video) course.activeSubjects.first()?.activeVideos?.find { it instanceof Video })
    }

    List<Map> visibleCoursesForBrowser() {
        def courses = listVisibleCourses()

        return courses.collect {
            [
                    title: "${it.name} (${it.fullName})",
                    description: it.description,
                    thumbnail: getThumbnailForCourse(it),
                    url: urlMappingService.generateLinkToCourse(it),
                    breadcrumbs: [
                            [
                                    title: it.teacher.toString(),
                                    url: urlMappingService.generateLinkToTeacher(it.teacher)
                            ]
                    ],
                    stats: [],
                    featuredChildren: []
            ]
        }
    }

    List<Map> visibleSubjectsForBrowser(Course course) {
        def breadcrumbs = []
        breadcrumbs.add([
                title: course.teacher.toString(),
                url: urlMappingService.generateLinkToTeacher(course.teacher)
        ])
        breadcrumbs.add([
                title: course.name,
                url: urlMappingService.generateLinkToCourse(course)
        ])

        return course.activeSubjects.collect {
            [
                    title: it.name,
                    thumbnail: subjectService.getThumbnail(it),
                    description: it.description,
                    url: urlMappingService.generateLinkToSubject(it),
                    breadcrumbs: breadcrumbs,
                    stats: [],
                    featuredChildren: []
            ]
        }
    }
}
