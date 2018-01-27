package dk.sdu.tekvideo

import org.hibernate.SessionFactory

class CourseService implements ContainerNodeInformation<Course, Subject> {
    def urlMappingService
    def videoService
    SessionFactory sessionFactory

    // Course specific operations

    int getStudentCount(Course course) {
        CourseStudent.countByCourse(course)
    }

    List<Course> listVisibleCourses() {
        def visible = Course.findAllByLocalStatus(NodeStatus.VISIBLE)
        visible.sort()
    }

    List<Course> listActiveCourses() {
        Course.findAllByLocalStatusNotEqual(NodeStatus.TRASH)
    }

    // General "node" operations

    @Override
    String getThumbnail(Course course) {
        return getThumbnailsBulk([course]).get(course)
    }

    @Override
    Map<Course, String> getThumbnailsBulk(List<Course> nodes) {
        def courses = nodes.groupBy { it.id }
        def query = $/
            SELECT DISTINCT ON (cs.course_id)
              cs.course_id,
              e.youtube_id,
              e.thumbnail_url
            FROM course_subject cs, subject_exercise se, subject s, exercise e
            WHERE
              cs.course_id IN (:courses) AND
              cs.subject_id = se.subject_id AND
              cs.subject_id = s.id AND
              se.subject_id = s.id AND
              se.exercise_id = e.id AND
              s.local_status = 'VISIBLE' AND
              e.local_status = 'VISIBLE' AND
              (e.class = :videoClass OR (e.class = :exerciseClass AND e.thumbnail_url IS NOT NULL)) 
        /$

        def sqlResult = sessionFactory.currentSession
                .createSQLQuery(query)
                .setParameterList("courses", nodes.collect { it.id })
                .setParameter("videoClass", Video.name)
                .setParameter("exerciseClass", WrittenExerciseGroup.name)
                .list()

        Map<Course, String> result = [:]
        nodes.each { result[it] = null }
        for (def row : sqlResult) {
            String thumbnail = null
            if (row[1] != null) {
                thumbnail = videoService.getThumbnail(row[1].toString())
            } else if (row[2] != null) {
                thumbnail = row[2]
            }
            result[courses[row[0].toString().toLong()][0]] = thumbnail
        }
        return result
    }


    @Override
    List<NodeBrowserCrumbs> getBreadcrumbs(Course course) {
        [
                new NodeBrowserCrumbs(
                        course.teacher.toString(),
                        urlMappingService.generateLinkToTeacher(course.teacher)
                ),
                new NodeBrowserCrumbs(
                        course.name,
                        urlMappingService.generateLinkToCourse(course)
                )
        ]
    }

    @Override
    NodeBrowserInformation getInformationForBrowser(Course course, String thumbnail, boolean resolveThumbnail,
                                                    boolean addBreadcrumbs) {
        List<NodeBrowserCrumbs> breadcrumbs = []
        if (addBreadcrumbs) breadcrumbs += new NodeBrowserCrumbs(
                course.teacher.toString(),
                urlMappingService.generateLinkToTeacher(course.teacher)
        )

        String actualThumbnail = !resolveThumbnail ? thumbnail : getThumbnail(course)
        return new NodeBrowserInformation(
                "${course.name} (${course.fullName})",
                course.description,
                actualThumbnail,
                urlMappingService.generateLinkToCourse(course),
                breadcrumbs,
                [new NodeBrowserStats(course.getShortWhen(), "watch-later")]
        )
    }

    @Override
    List<Subject> listVisibleChildren(Course course) {
        return course.visibleSubjects
    }

    @Override
    List<Subject> listActiveChildren(Course course) {
        return course.activeSubjects
    }
}
