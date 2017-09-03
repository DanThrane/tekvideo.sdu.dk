package dk.sdu.tekvideo

import dk.sdu.tekvideo.stats.StandardViewingStatisticsConfiguration
import dk.sdu.tekvideo.stats.ViewingStatisticsConfiguration
import dk.sdu.tekvideo.stats.WeeklyViewingStatisticsConfiguration
import grails.converters.JSON
import org.springframework.http.HttpStatus
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
        WRITTEN("written"),
        STUDENT("student")

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
            return home()
        }

        if (id == null) {
            render status: HttpStatus.NOT_FOUND, message: "Not found"
            return
        }

        if (act == null) {
            assert type != null
            assert id != null

            switch (resolvedType) {
                case ResourceType.COURSE:
                    return viewCourse(id)
                case ResourceType.SUBJECT:
                    return viewSubject(id)
                case ResourceType.VIDEO:
                    return viewVideo(id)
                case ResourceType.WRITTEN:
                    return viewWrittenExerciseGroup(id)
                default:
                    render status: HttpStatus.NOT_FOUND, message: "Not found"
                    return
            }
        }

        if (resolvedType == null) {
            render status: HttpStatus.NOT_FOUND, message: "Not found!"
        }

        switch (act) {
            case "students":
                if (renderStatus404Unless(resolvedType, ResourceType.COURSE)) return
                return viewCourseStudents(id)
            case "progress":
                switch (resolvedType) {
                    case ResourceType.COURSE:
                        return viewCourseProgress(id)
                    case ResourceType.SUBJECT:
                        return viewSubjectProgress(id)
                    case ResourceType.STUDENT:
                        return viewStudentProgress(id)
                    default:
                        render status: HttpStatus.NOT_FOUND, message: "Not found"
                        return
                }
            case "views":
                switch (resolvedType) {
                    case ResourceType.COURSE:
                        if (format == null) {
                            return viewCourseViews(id)
                        } else if (format.equalsIgnoreCase("json")) {
                            return jsonCourseViews(id)
                        } else {
                            render status: HttpStatus.NOT_FOUND, message: "Not found"
                            return
                        }
                    case ResourceType.SUBJECT:
                        if (format == null) {
                            return viewSubjectViews(id)
                        } else if (format.equalsIgnoreCase("json")) {
                            return jsonSubjectViews(id)
                        } else {
                            render status: HttpStatus.NOT_FOUND, message: "Not found"
                            return
                        }
                    case ResourceType.VIDEO:
                        if (format == null) {
                            return viewVideoViews(id)
                        } else if (format.equalsIgnoreCase("json")) {
                            return jsonVideoViews(id)
                        } else {
                            render status: HttpStatus.NOT_FOUND, message: "Not found"
                            return
                        }
                    case ResourceType.WRITTEN:
                        if (format == null) {
                            return viewWrittenViews(id)
                        } else if (format.equalsIgnoreCase("json")) {
                            return jsonWrittenViews(id)
                        } else {
                            render status: HttpStatus.NOT_FOUND, message: "Not found"
                            return
                        }
                    default:
                        render status: HttpStatus.NOT_FOUND, message: "Not found"
                        return
                }
        }
    }

    // Quite a bit of duplicated code down here. Not sure if refactoring it out is that helpful.
    // Although if changes need to made for all endpoints, it should probably be refactored out!

    def home() {
        def browser = statsService.coursesForBrowser()
        if (renderError(browser)) return

        render view: "home", model: [
                items      : browser.result,
                breadcrumbs: statsService.getBreadcrumbsToNode(null),
                stats      : []
        ]
    }

    private def viewCourse(Long id) {
        def course = Course.get(id)
        if (renderStatus404IfNotFound(course)) return

        def browser = statsService.subjectsForBrowser(course)
        if (renderError(browser)) return
        render view: "home", model: [
                node       : course,
                items      : browser.result,
                breadcrumbs: statsService.getBreadcrumbsToNode(course),
                stats      : [
                        studentsButton(course),
                        viewsButton(course),
                        progressButton(course)
                ]
        ]
    }

    private def viewSubject(Long id) {
        def subject = Subject.get(id)
        if (renderStatus404IfNotFound(subject)) return

        def browser = statsService.exercisesForBrowser(subject)
        if (renderError(browser)) return
        render view: "home", model: [
                node       : subject,
                items      : browser.result,
                breadcrumbs: statsService.getBreadcrumbsToNode(subject),
                stats      : [
                        viewsButton(subject),
                        progressButton(subject)
                ]
        ]
    }

    private def viewVideo(Long id) {
        def video = Video.get(id)
        if (renderStatus404IfNotFound(video)) return

        render view: "home", model: [
                node       : video,
                items      : null,
                breadcrumbs: statsService.getBreadcrumbsToNode(video),
                stats      : [
                        viewsButton(video)
                ]
        ]
    }

    private def viewWrittenExerciseGroup(Long id) {
        def written = WrittenExerciseGroup.get(id)
        if (renderStatus404IfNotFound(written)) return

        render view: "home", model: [
                node       : written,
                items      : null,
                breadcrumbs: statsService.getBreadcrumbsToNode(written),
                stats      : [
                        viewsButton(written)
                ]
        ]
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

    private def viewCourseProgress(Long id) {
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

    private def viewSubjectProgress(Long id) {
        def subject = Subject.get(id)
        if (renderStatus404IfNotFound(subject)) return

        def progressResult = statsService.subjectProgress(subject)
        if (renderError(progressResult)) return

        render view: "node_progress", model: [
                node       : subject,
                breadcrumbs: statsService.getBreadcrumbsToNode(subject),
                table      : progressResult.result
        ]
    }

    private def viewStudentProgress(Long id) {
        def student = Student.get(id)
        if (renderStatus404IfNotFound(student)) return

        Node node = retrieveNodeFromStudentRequest()
        if (renderStatus404IfNotFound(node)) return

        render view: "student_progress", model: [
                node       : node,
                breadcrumbs: statsService.getBreadcrumbsToNode(node),
                student    : student
        ]
    }

    private def viewCourseViews(Long id) {
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

    private def jsonCourseViews(Long id) {
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

    private def viewSubjectViews(Long id) {
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

    private def jsonSubjectViews(Long id) {
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

    private def viewVideoViews(Long id) {
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

    private def jsonVideoViews(Long id) {
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

    private def viewWrittenViews(Long id) {
        def group = WrittenExerciseGroup.get(id)
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

    private def jsonWrittenViews(Long id) {
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
            render status: HttpStatus.NOT_FOUND, text: "Not found!"
            return true
        }
        return false
    }

    private boolean renderStatus404IfNotFound(Object obj) {
        if (obj == null) {
            render stats: HttpStatus.NOT_FOUND, text: "Not found!"
            return true
        }
        return false
    }

    private void renderNotFoundJson() {
        response.status = HttpStatus.NOT_FOUND.value()
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

    private Node retrieveNodeFromStudentRequest() {
        if (params.containsKey("course")) {
            return Course.get(params.getLong("course"))
        }

        if (params.containsKey("subject")) {
            return Subject.get(params.getLong("subject"))
        }

        if (params.containsKey("exercise")) {
            return Exercise.get(params.getLong("exercise"))
        }

        return null
    }

    private def studentsButton(Node node) {
        [name: "Studerende", icon: FaIcon.GRADUATION_CAP, url: statsService.linkToNode(node, "students")]
    }

    private def viewsButton(Node node) {
        [name: "Visninger", icon: FaIcon.BAR_CHART, url: statsService.linkToNode(node, "views")]
    }

    private def progressButton(Node node) {
        [name: "Fremskridt", icon: FaIcon.PENCIL, url: statsService.linkToNode(node, "progress")]
    }
}
