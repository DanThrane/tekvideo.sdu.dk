import dk.sdu.tekvideo.Course
import dk.sdu.tekvideo.NodeStatus

databaseChangeLog = {

	changeSet(author: "Dan", id: "Rename deleted and invisible courses") {
		grailsChange {
			change {
                int idx = 0
                Course.list().each {
                    if (it.localStatus != NodeStatus.VISIBLE) {
                        it.name += " [RENAMED-$idx]"
                        idx++
                    }
                    it.save(flush: true, failOnError: true)
                }
			}
		}
	}

	changeSet(author: "Dan", id: "1447167619251-10") {
		createIndex(indexName: "unique_name", tableName: "course", unique: "true") {
			column(name: "year")

			column(name: "spring")

			column(name: "teacher_id")

			column(name: "name")
		}
	}
}
