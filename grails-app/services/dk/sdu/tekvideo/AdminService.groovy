package dk.sdu.tekvideo

import dk.sdu.tekvideo.events.AnswerQuestionEvent
import dk.sdu.tekvideo.events.VisitVideoEvent
import grails.plugin.springsecurity.SpringSecurityService
import grails.transaction.Transactional

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAmount

@Transactional
class AdminService {

    public static final DateTimeFormatter TIME_PATTERN = DateTimeFormatter.ofPattern("dd/MM HH:mm")
    TeachingService teachingService
    SpringSecurityService springSecurityService

    List<VisitVideoEvent> retrieveVideoEvents(VideoStatisticsCommand cmd) {
        def teacher = findCurrentTeacher()
        def data = teachingService.getCompleteData(teacher.user.username, cmd.course, cmd.subject, cmd.video)

        def criteria = VisitVideoEvent.where {
            course in teacher.courses
            if (cmd.showOnlyStudents) {
                user != null
            }
            if (cmd.course != null) {
                course.name == cmd.course
            }
            if (cmd.subject != null) {
                subject == cmd.subject
            }
            if (cmd.video != null && data != null) {
                video == data.video
            }
        }
        return criteria.findAll()
    }

    private Map groupByAttribute(List items, String attribute) {
        // TODO Quick @Hack. This really should have been prepared DB side
        Map result = [:]
        items.each {
            List slot = result[it.properties[attribute]]
            if (slot) {
                slot.add(it)
            } else {
                result[it.properties[attribute]] = [it]
            }
        }
        return result
    }

    private BigDecimal calculatePercentage(count, total) {
        (total == 0) ? 0 : (((count) / (total as BigDecimal)) * 100)
    }

    Map<Video, Map> summarizeVisits(List<Video> videos, List<VisitVideoEvent> events) {
        Map<Video, List<VisitVideoEvent>> groupedEvents = groupByAttribute(events, "video")
        Map<Video, Map> result = [:]
        videos.each {
            result[it] = [totalViews: 0, viewsByUsers: 0, viewsByStudents: 0, viewsByUsersPercentage : 0 as BigDecimal,
                          viewsByStudentsPercentage: 0]
        }

        groupedEvents.each { k, v ->
            Map slot = result[k]
            v.each {
                slot.totalViews++
                if (it.user != null) {

                    def student = Student.findByUser(it.user)
                    if (student in it.course.students) {
                        slot.viewsByStudents++
                    } else {
                        slot.viewsByUsers++
                    }
                }
            }
            slot.viewsByUsersPercentage = calculatePercentage(slot.viewsByUsers, slot.totalViews)
            slot.viewsByStudentsPercentage = calculatePercentage(slot.viewsByStudents, slot.totalViews)
            result[k] = slot
        }
        return result
    }

    Map<Video, Map> summarizeVideos(List<Video> videos) {
        Map result = [:]
        videos.each {
            result[it] = [release: it.dateCreated]
        }
        return result
    }

    Map<Video, Map> summarizeAnswers(List<Video> videos, List<AnswerQuestionEvent> events) {
        Map<Video, List<AnswerQuestionEvent>> groupedEvents = groupByAttribute(events, "video")
        Map<Video, Map> result = [:]
        videos.each {
            result[it] = [answersGiven: 0, correctAnswers: 0, answersByUsers: 0, answersByStudents: 0, correctByUsers: 0,
                          correctByStudents: 0, mostCommonIncorrect: "?", wrongAnswers: 0, wrongAnswersByStudents: 0,
                          wrongAnswersByUsers: 0, correctPercentage: 0, correctPercentageByUsers: 0,
                          correctPercentageByStudents: 0]
        }

        groupedEvents.each { k, v ->
            Map slot = result[k]
            Map incorrectAnswers = [:] // TODO Feels like a poor choice of data structure
            v.each {
                boolean inCourse = false
                boolean isUser = false
                if (it.user != null) {
                    isUser = true
                    def student = Student.findByUser(it.user)
                    if (student in it.course.students) {
                        inCourse = true
                    }
                }

                slot.answersGiven++
                if (isUser && !inCourse) slot.answersByUsers++
                if (inCourse) slot.answersByStudents++

                if (it.correct) {
                    slot.correctAnswers++
                    if (isUser && !inCourse) slot.correctByUsers++
                    if (inCourse) slot.correctByStudents++
                } else {
                    incorrectAnswers.putIfAbsent(it.answer, 0)
                    incorrectAnswers[it.answer] = incorrectAnswers[it.answer] + 1
                }
            }
            incorrectAnswers.entrySet().stream()
                    .max(Comparator.comparingInt { it.getValue() })
                    .ifPresent { slot.mostCommonIncorrect = it.getKey() }


            slot.correctPercentage = calculatePercentage(slot.correctAnswers, slot.answersGiven)
            slot.correctPercentageByUsers = calculatePercentage(slot.correctByUsers, slot.answersByUsers)
            slot.correctPercentageByStudents = calculatePercentage(slot.correctByStudents, slot.answersByStudents)
            slot.wrongAnswersByUsers = slot.answersByUsers - slot.correctByUsers
            slot.wrongAnswersByStudents = slot.answersByStudents - slot.correctByStudents
            slot.wrongAnswers = slot.answersGiven - slot.correctAnswers
            println slot.correctPercentage
            result[k] = slot
        }
        return result
    }

    Map<Video, Map> findSummaryData(VideoSummaryQueryCommand command, Teacher teacher = null,
                                    List<Course> courses = null, List<Subject> subjects = null) {
        // TODO Could be done in a single query, instead of several
        // TODO Apply constraints from command object

        // TODO Most of the summary data could be collected directly from the database, as opposed
        // to this
        if (!teacher) teacher = findCurrentTeacher()
        if (command != null) {
            if (command.course != null && command.subject == null) courses = [command.course]
            if (command.subject != null) subjects = [command.subject]
        }

        if (!courses) courses = Course.findAllByTeacher(teacher)
        if (!subjects) subjects = Subject.findAllByCourseInList(courses)

        List<Video> videos = subjects.videos.flatten()
        List<VisitVideoEvent> visitEvents = VisitVideoEvent.findAllByVideoInList(videos)
        List<AnswerQuestionEvent> answerEvents = AnswerQuestionEvent.findAllByVideoInList(videos)

        Map<Video, Map> videoSummary = summarizeVideos(videos)
        Map<Video, Map> viewSummary = summarizeVisits(videos, visitEvents)
        Map<Video, Map> answerSummary = summarizeAnswers(videos, answerEvents)

        Map<Video, Map> result = [:]

        videos.each {
            result[it] = videoSummary[it]
            result[it].putAll(viewSummary[it])
            result[it].putAll(answerSummary[it])
        }

        return result
    }

    def findViewingStatistics(Video video, long from, long to, long periodInMs) {
        List<VisitVideoEvent> events = VisitVideoEvent.findAllByVideo(video)
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

    Teacher findCurrentTeacher() {
        Teacher.findByUser(springSecurityService.currentUser as User)
    }

}
