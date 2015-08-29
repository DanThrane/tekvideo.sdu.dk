package dk.sdu.tekvideo.data

import dk.sdu.tekvideo.v2.Course2
import dk.sdu.tekvideo.v2.Subject2

import java.util.concurrent.atomic.AtomicInteger

class SubjectData {
    private static AtomicInteger idx = new AtomicInteger(0)

    static Subject2 buildTestSubject(String prefix = "Subject", Course2 course = null, boolean includeIdSuffix = false) {
        if (course == null) course = CourseData.buildTestCourse()

        String name = prefix
        if (includeIdSuffix) name += idx.getAndIncrement()

        def subject1 = new Subject2([
                name  : name,
                course: course
        ])
        subject1.save(failOnError: true, flush: true)
    }
}
