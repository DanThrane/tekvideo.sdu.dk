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

    CourseManagementService courseManagementService

    private Course findCourse(Node node) {
        while (node != null && !node instanceof Course) {
            node = node.parent
        }
        return (node instanceof Course) ? node : null
    }

    ServiceResult<List<Video>> findLeaves(Node node) {
        def course = findCourse(node)
        if (course != null && courseManagementService.canAccess(course)) {
            def subjects = Subject.findAllByCourse(course)
            ok item: Video.findAllBySubjectInList(subjects)
        } else {
            fail message: "Du har ikke rettigheder til at tilgå dette kursus", suggestedHttpStatus: 403
        }
    }

    Map findViewingStatistics(List<Video> leaves, Integer period) {
        def videoIds = leaves.stream().map { it.id }.collect(Collectors.toList())
        List<VisitVideoEvent> events = VisitVideoEvent.findAllByVideoIdInList(videoIds)

        long from = System.currentTimeMillis() - period * 24 * 60 * 60 * 1000
        long to = System.currentTimeMillis()
        if (period <= 0) from = events.min { it.timestamp }.timestamp
        long periodInMs = to - from / 24

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
