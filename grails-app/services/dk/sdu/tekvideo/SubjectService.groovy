package dk.sdu.tekvideo

import org.hibernate.SessionFactory

class SubjectService implements ContainerNodeInformation<Subject, Exercise> {
    def videoService
    def urlMappingService
    def courseService
    SessionFactory sessionFactory

    @Override
    String getThumbnail(Subject subject) {
        return getThumbnailsBulk([subject]).get(subject)
    }

    @Override
    Map<Subject, String> getThumbnailsBulk(List<Subject> nodes) {
        def subjects = nodes.groupBy { it.id }
        def query = $/
            SELECT DISTINCT ON (s.id)
              s.id,
              e.youtube_id,
              e.thumbnail_url
            FROM
              subject_exercise se, subject s, exercise e
            WHERE
              se.subject_id = s.id AND
              se.exercise_id = e.id AND
              s.id IN (:subjects) AND
              e.local_status = 'VISIBLE' AND
              (e.class = :videoClass OR (e.class = :exerciseClass AND e.thumbnail_url IS NOT NULL)) 
        /$

        def sqlResult = sessionFactory.currentSession
                .createSQLQuery(query)
                .setParameterList("subjects", nodes.collect { it.id })
                .setParameter("videoClass", Video.name)
                .setParameter("exerciseClass", WrittenExerciseGroup.name)
                .list()

        Map<Subject, String> result = [:]
        nodes.each { result[it] = null }
        for (def row : sqlResult) {
            String thumbnail = null
            if (row[1] != null) {
                thumbnail = videoService.getThumbnail(row[1].toString())
            } else if (row[2] != null) {
                thumbnail = row[2]
            }
            result[subjects[row[0].toString().toLong()][0]] = thumbnail
        }
        return result
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
    NodeBrowserInformation getInformationForBrowser(Subject it, String thumbnail, boolean resolveThumbnail,
                                                    boolean addBreadcrumbs) {
        List<NodeBrowserCrumbs> breadcrumbs = addBreadcrumbs ? courseService.getBreadcrumbs(it.course) : []

        String actualThumbnail = !resolveThumbnail ? thumbnail : getThumbnail(it)
        return new NodeBrowserInformation(
                it.name,
                it.description,
                actualThumbnail,
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
