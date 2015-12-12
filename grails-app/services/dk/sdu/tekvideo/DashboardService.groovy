package dk.sdu.tekvideo

import dk.sdu.tekvideo.events.VisitVideoEvent

import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.stream.Collectors

import static dk.sdu.tekvideo.ServiceResult.*

import grails.transaction.Transactional

@Transactional
class DashboardService {
    private static final DateTimeFormatter TIME_PATTERN = DateTimeFormatter.ofPattern("dd/MM HH:mm")

    def teachingService
    CourseManagementService courseManagementService

    private Course findCourse(Node node) {
        while (node != null && node.parent != null) {
            node = node.parent
        }
        return (node instanceof Course) ? node : null
    }

    ServiceResult<Node> nodeFromIdentifier(String identifier) {
        def split = identifier.split("/")
        if (split.length != 2) {
            return fail(message: "Invalid identifier")
        } else {
            try {
                def id = Long.parseLong(split[1])
                Node result = null
                switch (split[0]) {
                    case "course":
                        result = Course.get(id)
                        break
                    case "subject":
                        result = Subject.get(id)
                        break
                    case "video":
                        result = Video.get(id)
                        break
                }
                if (result != null) {
                    println "Found a result ${result}"
                    return ok(item: result)
                } else {
                    return fail(message: "Node not found!")
                }
            } catch (NumberFormatException ignored) {
                return fail(message: "Invalid ID")
            }
        }
    }

    ServiceResult<List<Video>> findLeaves(Node node) {
        def course = findCourse(node)
        if (course != null && courseManagementService.canAccess(course)) {
            if (node instanceof Course) {
                def subjects = Subject.findAllByCourse(course)
                ok item: Video.findAllBySubjectInList(subjects)
            } else if (node instanceof Subject) {
                ok item: Video.findAllBySubject(node)
            } else if (node instanceof Video) {
                ok item: [node]
            } else {
                fail message: "Ukendt type", suggestedHttpStatus: 500
            }
        } else {
            fail message: "Du har ikke rettigheder til at tilgå dette kursus", suggestedHttpStatus: 403
        }
    }

    Map findViewingStatistics(List<Video> leaves, Long period) {
        if (leaves == null) leaves = []
        def videoIds = leaves.stream().map { it.id }.collect(Collectors.toList())

        long from = System.currentTimeMillis() - period * 24 * 60 * 60 * 1000
        long to = System.currentTimeMillis()
        List<VisitVideoEvent> events
        if (period <= 0) {
            events = VisitVideoEvent.findAllByVideoIdInList(videoIds).findAll { it != null }
            from = events.min { it.timestamp }.timestamp
        } else {
            events = VisitVideoEvent.findAllByVideoIdInListAndTimestampBetween(videoIds, from, to).findAll { it != null }
        }
        long periodInMs = (to - from) / 24

        List labels = []
        List data = []
        // Generate some labels (X-axis)
        long counter = from
        while (counter < to) {
            labels.add(TIME_PATTERN.format(new Date(counter).toInstant().atZone(ZoneId.systemDefault())))
            data.add(0)
            counter += periodInMs
        }
        events.each {
            long time = it.timestamp
            int index = (time - from) / periodInMs
            if (index > 0) {
                data[index]++
            }
        }

        return [labels: labels, data: data]
    }

}
