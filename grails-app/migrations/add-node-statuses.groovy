import dk.sdu.tekvideo.Course
import dk.sdu.tekvideo.NodeStatus
import dk.sdu.tekvideo.Subject
import dk.sdu.tekvideo.Video

databaseChangeLog = {

	changeSet(author: "Admin (generated)", id: "1444737274017-1") {
		addColumn(tableName: "course") {
			column(name: "local_status", type: "varchar(255)")
		}
	}

	changeSet(author: "Admin (generated)", id: "1444737274017-2") {
		addColumn(tableName: "subject") {
			column(name: "local_status", type: "varchar(255)")
		}
	}

	changeSet(author: "Admin (generated)", id: "1444737274017-3") {
		addColumn(tableName: "video") {
			column(name: "local_status", type: "varchar(255)")
		}
	}

	changeSet(author: "Dan", id: "set default node statuses") {
		grailsChange {
			change {
				boolean hasSomething = false
				sql.eachRow("SELECT COUNT(*) FROM video, subject, course;") { if (it.count > 0) hasSomething = true }
				if (hasSomething) {
					Video.list().each {
						it.localStatus = NodeStatus.VISIBLE
						it.save(flush: true)
					}
					Subject.list().each {
						it.localStatus = NodeStatus.VISIBLE
						it.save(flush: true)
					}
					Course.list().each {
						it.localStatus = NodeStatus.VISIBLE
						it.save(flush: true)
					}
				}
			}
		}
		addNotNullConstraint(columnName: "local_status", tableName: "video")
		addNotNullConstraint(columnName: "local_status", tableName: "course")
		addNotNullConstraint(columnName: "local_status", tableName: "subject")
	}

}
