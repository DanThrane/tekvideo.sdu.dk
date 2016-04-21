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
				int count = 0
				sql.eachRow("SELECT COUNT(*) FROM course;") { count = it.count }
				if (count > 0) {
					Course.list().each {
						it.localStatus = NodeStatus.VISIBLE
						it.save(flush: true)
					}
				}
				count = 0
				sql.eachRow("SELECT COUNT(*) FROM video;") { count = it.count }
				if (count > 0) {
					Video.list().each {
						it.localStatus = NodeStatus.VISIBLE
						it.save(flush: true)
					}
				}
				count = 0
				sql.eachRow("SELECT COUNT(*) FROM subject;") { count = it.count }
				if (count > 0) {
					Subject.list().each {
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
