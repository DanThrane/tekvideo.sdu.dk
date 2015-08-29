package dk.sdu.tekvideo.data

import dk.sdu.tekvideo.Semester
import dk.sdu.tekvideo.Teacher
import dk.sdu.tekvideo.v2.Course2

import java.util.concurrent.atomic.AtomicInteger

class CourseData {
    private static AtomicInteger idx = new AtomicInteger(0)

    static Course2 buildTestCourse(String prefix = "Course", Teacher teacher = null, boolean includeIdSuffix = false) {
        if (teacher == null) teacher = UserData.buildTestTeacher()

        String name = prefix
        if (includeIdSuffix) name += idx.getAndIncrement()

        def semester = new Semester(spring: false, year: 2015).save(failOnError: true, flush: true)
        def course = new Course2([
                name       : name,
                fullName   : "Full Name",
                description: "A description",
                semester   : semester,
                teacher    : teacher
        ])
        course.save(failOnError: true, flush: true)
    }
}
