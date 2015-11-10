package dk.sdu.tekvideo.data

import dk.sdu.tekvideo.Course
import dk.sdu.tekvideo.Subject

import java.util.concurrent.atomic.AtomicInteger

class SubjectData {
    private static AtomicInteger idx = new AtomicInteger(0)

    static Subject buildTestSubject(String prefix = "Subject", Course course = null, boolean includeIdSuffix = false,
                                    boolean save = true) {
        if (course == null) course = CourseData.buildTestCourse()

        String name = prefix
        if (includeIdSuffix) name += idx.getAndIncrement()

        def subject1 = new Subject([
                name  : name,
                course: course
        ])

        if (save) {
            subject1.save(failOnError: true, flush: true)
        }

        return subject1
    }
}
