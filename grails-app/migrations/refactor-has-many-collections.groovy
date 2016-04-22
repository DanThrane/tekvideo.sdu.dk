import dk.sdu.tekvideo.Course
import dk.sdu.tekvideo.CourseSubject
import dk.sdu.tekvideo.Subject
import dk.sdu.tekvideo.SubjectVideo
import dk.sdu.tekvideo.Video

databaseChangeLog = {
	changeSet(author: "Admin (generated)", id: "1460194289304-1") {
		createTable(tableName: "course_subject") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "course_subjecPK")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "course_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "index", type: "int4") {
				constraints(nullable: "false")
			}

			column(name: "subject_id", type: "int8") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "Admin (generated)", id: "1460194289304-2") {
		createTable(tableName: "subject_video") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "subject_videoPK")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "index", type: "int4") {
				constraints(nullable: "false")
			}

			column(name: "subject_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "video_id", type: "int8") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "Dan", id: "Migrate Course#subjects") {
		grailsChange {
			change {
                Course.list().each { course ->
                    course.subjects.eachWithIndex { Subject entry, int i ->
                        if (entry != null) {
                            new CourseSubject(course: course, subject: entry, index: i).save(flush: true, failOnError: true)
                        }
                    }
                }
			}
		}
	}

    changeSet(author: "Dan", id: "Migrate Subject#videos") {
		grailsChange {
			change {
                Subject.list().each { subject ->
                    subject.videos.eachWithIndex { Video entry, int i ->
                        if (entry != null) {
                            new SubjectVideo(subject: subject, video: entry, index: i).save(flush: true, failOnError: true)
                        }
                    }
                }
			}
		}
	}
}
