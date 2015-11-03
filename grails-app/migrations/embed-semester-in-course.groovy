import dk.sdu.tekvideo.Course

databaseChangeLog = {

	changeSet(author: "Admin (generated)", id: "1446563779257-1") {
		addColumn(tableName: "course") {
			column(name: "spring", type: "boolean")
		}
	}

	changeSet(author: "Admin (generated)", id: "1446563779257-2") {
		addColumn(tableName: "course") {
			column(name: "year", type: "int4")
		}
	}

	changeSet(author: "Dan", id: "copy old semester information") {
		grailsChange {
			change {
				Course.list().each {
					Long semesterId = null
					Integer year = null
					Boolean spring = null
					sql.eachRow("SELECT semester_id FROM course WHERE id = ${it.id};") { semesterId = it.semester_id }
					sql.eachRow("SELECT year, spring FROM semester WHERE id = ${semesterId};") {
						spring = it.spring
						year = it.year
					}
					it.year = year
					it.spring = spring
					it.save(failOnError: true, flush: true)
				}
			}
		}
		addNotNullConstraint(columnName: "spring", tableName: "course")
		addNotNullConstraint(columnName: "year", tableName: "course")
	}

	changeSet(author: "Admin (generated)", id: "1446563779257-3") {
		modifyDataType(columnName: "correct", newDataType: "boolean", tableName: "event")
	}

	changeSet(author: "Admin (generated)", id: "1446563779257-4") {
		modifyDataType(columnName: "account_expired", newDataType: "boolean", tableName: "myusers")
	}

	changeSet(author: "Admin (generated)", id: "1446563779257-5") {
		modifyDataType(columnName: "account_locked", newDataType: "boolean", tableName: "myusers")
	}

	changeSet(author: "Admin (generated)", id: "1446563779257-6") {
		modifyDataType(columnName: "enabled", newDataType: "boolean", tableName: "myusers")
	}

	changeSet(author: "Admin (generated)", id: "1446563779257-7") {
		modifyDataType(columnName: "password_expired", newDataType: "boolean", tableName: "myusers")
	}

	changeSet(author: "Admin (generated)", id: "1446563779257-8") {
		modifyDataType(columnName: "video_type", newDataType: "boolean", tableName: "video")
	}

	changeSet(author: "Admin (generated)", id: "1446563779257-9") {
		dropForeignKeyConstraint(baseTableName: "course", baseTableSchemaName: "public", constraintName: "fk_rycs8w7ludava0ahssdyy2i8k")
	}

	changeSet(author: "Admin (generated)", id: "1446563779257-10") {
		dropColumn(columnName: "semester_id", tableName: "course")
	}

	changeSet(author: "Admin (generated)", id: "1446563779257-11") {
		dropTable(tableName: "semester")
	}
}
