package dk.sdu.tekvideo

class DashboardTagLib {
    static namespace = "sdu"

    def dashboardService

    /**
     * Displays a view graph for a set of courses, subjects or videos
     */
    def viewGraph = { attrs, body ->
        out << render([template: "/dashboard/viewGraph", model: [:]], body)
    }

    /**
     * Displays a list of popular videos, for a set of courses, subjects or videos
     */
    def popularVideos = { attrs, body ->
        List<PopularVideo> videos = []

        out << render([template: "/dashboard/popularVideos", model: [
                videos: videos
        ]], body)
    }

    /**
     * Displays an activity feed, for a set of courses, subjects, videos or students
     */
    def activityFeed = { attrs, body -> }

    /**
     * Displays an activity summary, for a set of courses, subjects or videos
     */
    def activitySummary = { attrs, body -> }
}
