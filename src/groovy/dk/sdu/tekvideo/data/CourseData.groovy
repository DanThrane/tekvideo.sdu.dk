package dk.sdu.tekvideo.data

import dk.sdu.tekvideo.Teacher
import dk.sdu.tekvideo.Course

import java.util.concurrent.atomic.AtomicInteger

class CourseData {
    private static AtomicInteger idx = new AtomicInteger(0)

    static Course buildTestCourse(String prefix = "Course", Teacher teacher = null, boolean includeIdSuffix = false) {
        if (teacher == null) teacher = UserData.buildTestTeacher()

        String name = prefix
        if (includeIdSuffix) name += idx.getAndIncrement()

        def course = new Course([
                name       : name,
                fullName   : "Full Name",
                description: "A description",
                year       : 2015,
                spring     : false,
                teacher    : teacher
        ])
        course.save(failOnError: true, flush: true)
    }
}
