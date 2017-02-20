package dk.sdu.tekvideo

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
            println("home")
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
                        println(format)
                        if (format == null) {
                            forward action: "viewCourseViews", params: [id: id]
                        } else if (format.equalsIgnoreCase("json")) {
                            forward action: "jsonCourseViews", params: [id: id]
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

        def viewsResult = statsService.courseViews(course)
        if (renderError(viewsResult)) return

        render view: "views", model: [
                node: course,
                views: viewsResult.result,
                breadcrumbs: statsService.getBreadcrumbsToNode(course),
                endpoint: statsService.generateLink("course", id, "views", "json")
        ]
    }

    def jsonCourseViews(Long id) {
        def course = Course.get(id)
        if (course == null) {
            renderNotFoundJson()
            return
        }

        def weekly = parseParamsBoolean("weekly", false)
        def cumulative = parseParamsBoolean("cumulative", false)
        def viewsResult = statsService.courseViews(course, weekly, cumulative)
        if (renderJsonError(viewsResult)) return
        render viewsResult as JSON
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
