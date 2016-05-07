package dk.sdu.tekvideo.data

import dk.sdu.tekvideo.Course
import dk.sdu.tekvideo.CourseSubject
import dk.sdu.tekvideo.Subject

import java.util.concurrent.atomic.AtomicInteger

class SubjectData {
    private static AtomicInteger idx = new AtomicInteger(0)

    static Subject buildTestSubject(String prefix = "Subject", Course course = null, boolean includeIdSuffix = false,
                                    boolean save = true) {
        if (course == null) {
            course = CourseData.buildTestCourse()
        }

        String name = prefix
        if (includeIdSuffix) {
            name += idx.getAndIncrement()
        }

        def subject = new Subject(name: name)

        if (save) {
            subject.save(failOnError: true, flush: true)
            CourseSubject.create(course, subject, [save: true, failOnError: true, flush: true])
        }
        return subject
    }
}
