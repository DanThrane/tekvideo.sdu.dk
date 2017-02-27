package dk.sdu.tekvideo

import dk.sdu.tekvideo.stats.StandardViewingStatisticsConfiguration
import dk.sdu.tekvideo.stats.ViewingStatisticsConfiguration
import dk.sdu.tekvideo.stats.WeeklyViewingStatisticsConfiguration
import grails.converters.JSON
import org.apache.http.HttpStatus
import org.springframework.security.access.annotation.Secured

@Secured(["ROLE_TEACHER"])
class StatsController {
    def statsService

    // TODO Refactor this somewhere else?
    // Not entirely sure. Technically the routing part best belongs to in the controller, so
    // no service should be needed.
    private static enum ResourceType {
        COURSE("course"),
        SUBJECT("subject"),
        VIDEO("video"),
        WRITTEN("written")
        private final String name

        ResourceType(String name) {
            this.name = name
        }

        @Override
        String toString() {
            return name
        }

        static ResourceType fromString(String name) {
            return values().find { it.name == name }
        }
    }

    def index(String type, Long id, String act, String format) {
        def resolvedType = ResourceType.fromString(type)

        if (type == null) {
            forward action: "home"
            return
        }

        if (id == null) {
            render status: HttpStatus.SC_NOT_FOUND, message: "Not found"
            return
        }

        if (act == null) {
            assert type != null
            assert id != null

            switch (resolvedType) {
                case ResourceType.COURSE:
                    forward action: "viewCourse", params: [id: id]
                    return
                case ResourceType.SUBJECT:
                    forward action: "viewSubject", params: [id: id]
                    return
                case ResourceType.VIDEO:
                    forward action: "viewVideo", params: [id: id]
                    return
                case ResourceType.WRITTEN:
                    forward action: "viewWrittenExerciseGroup", params: [id: id]
                    return
                default:
                    render status: HttpStatus.SC_NOT_FOUND, message: "Not found"
                    return
            }
        }

        if (resolvedType == null) {
            render status: HttpStatus.SC_NOT_FOUND, message: "Not found!"
        }

        switch (act) {
            case "students":
                if (renderStatus404Unless(resolvedType, ResourceType.COURSE)) return
                forward action: "viewCourseStudents", params: [id: id]
                return
            case "progress":
                switch (resolvedType) {
                    case ResourceType.COURSE:
                        forward action: "viewCourseProgress", params: [id: id]
                        return
                    case ResourceType.SUBJECT:
                        forward action: "viewSubjectProgress", params: [id: id]
                        return
                    default:
                        render status: HttpStatus.SC_NOT_FOUND, message: "Not found"
                        return
                }
            case "views":
                switch (resolvedType) {
                    case ResourceType.COURSE:
                        if (format == null) {
                            forward action: "viewCourseViews", params: [id: id]
                        } else if (format.equalsIgnoreCase("json")) {
                            forward action: "jsonCourseViews", params: [id: id]
                        }
                        return
                    case ResourceType.SUBJECT:
                        if (format == null) {
                            forward action: "viewSubjectViews", params: [id: id]
                        } else if (format.equalsIgnoreCase("json")) {
                            forward action: "jsonSubjectViews", params: [id: id]
                        }
                        return
                    case ResourceType.VIDEO:
                        if (format == null) {
                            forward action: "viewVideoViews", params: [id: id]
                        } else if (format.equalsIgnoreCase("json")) {
                            forward action: "jsonVideoViews", params: [id: id]
                        }
                        return
                    case ResourceType.WRITTEN:
                        if (format == null) {
                            forward action: "viewWrittenViews", params: [id: id]
                        } else if (format.equalsIgnoreCase("json")) {
                            forward action: "jsonWrittenViews", params: [id: id]
                        }
                        return
                    default:
                        render status: HttpStatus.SC_NOT_FOUND, message: "Not found"
                        return
                }
        }
    }

    def home() {
        def browser = statsService.coursesForBrowser()
        if (renderError(browser)) return

        [
                items      : browser.result,
                breadcrumbs: statsService.getBreadcrumbsToNode(null)
        ]
    }

    def viewCourse(Long id) {
        def course = Course.get(id)
        if (renderStatus404IfNotFound(course)) return

        def browser = statsService.subjectsForBrowser(course)
        if (renderError(browser)) return
        render view: "home", model: [
                node       : course,
                items      : browser.result,
                breadcrumbs: statsService.getBreadcrumbsToNode(course)
        ]
    }

    def viewSubject(Long id) {
        def subject = Subject.get(id)
        if (renderStatus404IfNotFound(subject)) return

        def browser = statsService.exercisesForBrowser(subject)
        if (renderError(browser)) return
        render view: "home", model: [
                node       : subject,
                items      : browser.result,
                breadcrumbs: statsService.getBreadcrumbsToNode(subject)
        ]
    }

    def viewVideo(Long id) {
        render text: "viewVideo(${id})"
    }

    def viewWrittenExerciseGroup(Long id) {
        render text: "viewWrittenExerciseGroup(${id})"
    }

    def viewCourseStudents(Long id) {
        def course = Course.get(id)
        if (renderStatus404IfNotFound(course)) return

        def studentsResult = statsService.studentsForCourse(course)
        if (renderError(studentsResult)) return

        render view: "student_list", model: [
                node       : course,
                breadcrumbs: statsService.getBreadcrumbsToNode(course),
                students   : studentsResult.result
        ]
    }

    def viewCourseProgress(Long id) {
        def course = Course.get(id)
        if (renderStatus404IfNotFound(course)) return

        def progressResult = statsService.courseProgress(course)
        if (renderError(progressResult)) return

        render view: "node_progress", model: [
                node       : course,
                breadcrumbs: statsService.getBreadcrumbsToNode(course),
                table      : progressResult.result
        ]
    }

    def viewSubjectProgress(Long id) {
        def subject = Subject.get(id)
        if (renderStatus404IfNotFound(subject)) return

        throw new IllegalStateException("Not yet implemented") // TODO NYI
    }

    def viewCourseViews(Long id) {
        def course = Course.get(id)
        if (renderStatus404IfNotFound(course)) return

        def viewsResult = statsService.courseViews(course, getViewsConfiguration())
        if (renderError(viewsResult)) return

        render view: "views", model: [
                node       : course,
                views      : viewsResult.result,
                breadcrumbs: statsService.getBreadcrumbsToNode(course),
                endpoint   : statsService.generateLink("course", id, "views", "json")
        ]
    }

    def jsonCourseViews(Long id) {
        def course = Course.get(id)
        if (course == null) {
            renderNotFoundJson()
            return
        }

        def config = getViewsConfiguration()
        def viewsResult = statsService.courseViews(course, config)
        if (renderJsonError(viewsResult)) return
        render viewsResult as JSON
    }

    def viewSubjectViews(Long id) {
        def subject = Subject.get(id)
        if (renderStatus404IfNotFound(subject)) return

        def viewsResult = statsService.subjectViews(subject, getViewsConfiguration())
        if (renderError(viewsResult)) return

        render view: "views", model: [
                node       : subject,
                views      : viewsResult.result,
                breadcrumbs: statsService.getBreadcrumbsToNode(subject),
                endpoint   : statsService.generateLink("subject", id, "views", "json")
        ]
    }

    def jsonSubjectViews(Long id) {
        def subject = Subject.get(id)
        if (subject == null) {
            renderNotFoundJson()
            return
        }

        def config = getViewsConfiguration()
        def viewsResult = statsService.subjectViews(subject, config)
        if (renderJsonError(viewsResult)) return
        render viewsResult as JSON
    }

    def viewVideoViews(Long id) {
        def exercise = Video.get(id)
        if (renderStatus404IfNotFound(exercise)) return

        def viewsResult = statsService.exerciseViews(exercise, getViewsConfiguration())
        if (renderError(viewsResult)) return

        render view: "views", model: [
                node       : exercise,
                views      : viewsResult.result,
                breadcrumbs: statsService.getBreadcrumbsToNode(exercise),
                endpoint   : statsService.generateLink("video", id, "views", "json")
        ]
    }

    def jsonVideoViews(Long id) {
        def video = Video.get(id)
        if (video == null) {
            renderNotFoundJson()
            return
        }

        def config = getViewsConfiguration()
        def viewsResult = statsService.exerciseViews(video, config)
        if (renderJsonError(viewsResult)) return
        render viewsResult as JSON
    }

    def viewWrittenViews(Long id) {
        def group = Video.get(id)
        if (renderStatus404IfNotFound(group)) return

        def viewsResult = statsService.exerciseViews(group, getViewsConfiguration())
        if (renderError(viewsResult)) return

        render view: "views", model: [
                node       : group,
                views      : viewsResult.result,
                breadcrumbs: statsService.getBreadcrumbsToNode(group),
                endpoint   : statsService.generateLink("written", id, "views", "json")
        ]
    }

    def jsonWrittenViews(Long id) {
        def group = WrittenExerciseGroup.get(id)
        if (group == null) {
            renderNotFoundJson()
            return
        }

        def config = getViewsConfiguration()
        def viewsResult = statsService.exerciseViews(group, config)
        if (renderJsonError(viewsResult)) return
        render viewsResult as JSON
    }

    private ViewingStatisticsConfiguration getViewsConfiguration() {
        def weekly = parseParamsBoolean("weekly", false)

        if (weekly) {
            return new WeeklyViewingStatisticsConfiguration()
        } else {
            def cumulative = parseParamsBoolean("cumulative", false)

            def result = new StandardViewingStatisticsConfiguration()
            result.cumulative = cumulative
            return result
        }
    }

    private boolean renderError(ServiceResult s) {
        if (!s.success) {
            render status: s.suggestedHttpStatus, text: s.message
            return true
        }
        return false
    }

    private boolean renderJsonError(ServiceResult s) {
        if (!s.success) {
            response.status = s.suggestedHttpStatus
            render response as JSON
            return true
        }
        return false
    }

    private <T> boolean renderStatus404Unless(T expected, T... actual) {
        def asList = Arrays.asList(actual)
        if (!asList.any { it == expected }) {
            render status: HttpStatus.SC_NOT_FOUND, text: "Not found!"
            return true
        }
        return false
    }

    private boolean renderStatus404IfNotFound(Object obj) {
        if (obj == null) {
            render stats: HttpStatus.SC_NOT_FOUND, text: "Not found!"
            return true
        }
        return false
    }

    private void renderNotFoundJson() {
        response.status = HttpStatus.SC_NOT_FOUND
        def resp = [message: "Not found"]
        render resp as JSON
    }

    private boolean parseParamsBoolean(String key, boolean defaultValue, boolean remove = true) {
        if (remove) {
            return Boolean.parseBoolean(params.remove(key) as String) ?: defaultValue
        } else {
            return Boolean.parseBoolean(params.get(key) as String) ?: defaultValue
        }
    }
}
