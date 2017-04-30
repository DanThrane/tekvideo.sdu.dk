import dk.sdu.tekvideo.events.*

databaseChangeLog = {

	changeSet(author: "Admin (generated)", id: "1443860443687-1") {
		addColumn(tableName: "event") {
			column(name: "field", type: "int4")
		}
	}

	changeSet(author: "Admin (generated)", id: "1443860443687-2") {
		addColumn(tableName: "event") {
			column(name: "question", type: "int4")
		}
	}

	changeSet(author: "Admin (generated)", id: "1443860443687-3") {
		addColumn(tableName: "event") {
			column(name: "subject", type: "int4")
		}
	}

	changeSet(author: "Admin (generated)", id: "1443860443687-4") {
		modifyDataType(columnName: "correct", newDataType: "boolean", tableName: "event")
	}

	changeSet(author: "Admin (generated)", id: "1443860443687-5") {
		modifyDataType(columnName: "account_expired", newDataType: "boolean", tableName: "myusers")
	}

	changeSet(author: "Admin (generated)", id: "1443860443687-6") {
		modifyDataType(columnName: "account_locked", newDataType: "boolean", tableName: "myusers")
	}

	changeSet(author: "Admin (generated)", id: "1443860443687-7") {
		modifyDataType(columnName: "enabled", newDataType: "boolean", tableName: "myusers")
	}

	changeSet(author: "Admin (generated)", id: "1443860443687-8") {
		modifyDataType(columnName: "password_expired", newDataType: "boolean", tableName: "myusers")
	}

	changeSet(author: "Admin (generated)", id: "1443860443687-9") {
		modifyDataType(columnName: "spring", newDataType: "boolean", tableName: "semester")
	}

	changeSet(author: "Admin (generated)", id: "1443860443687-10") {
		modifyDataType(columnName: "video_type", newDataType: "boolean", tableName: "video")
	}

	changeSet(author: "Dan", id: "set default values for answer events") {
		grailsChange {
            change {
				int count = 0
				sql.eachRow("SELECT COUNT(*) FROM event;") { count = it.count }

				if (count > 0) {
					AnswerQuestionEvent.list().each { event ->
						event.field = 0
						event.subject = 0
						event.question = 0
						event.save(failOnError: true, flush: true)
					}
				}
            }
        }
	}
}
