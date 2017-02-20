databaseChangeLog = {

	changeSet(author: "dan (generated)", id: "1487589974483-1") {
		createTable(tableName: "video_answer") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "video_answerPK")
			}

			column(name: "correct", type: "boolean") {
				constraints(nullable: "false")
			}

			column(name: "date_created", type: "timestamp") {
				constraints(nullable: "false")
			}

			column(name: "progress_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "question", type: "int4") {
				constraints(nullable: "false")
			}

			column(name: "subject", type: "int4") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "dan (generated)", id: "1487589974483-2") {
		createTable(tableName: "video_answer_details") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "video_answer_PK")
			}

			column(name: "answer", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "correct", type: "boolean") {
				constraints(nullable: "false")
			}

			column(name: "field", type: "int4") {
				constraints(nullable: "false")
			}

			column(name: "parent_id", type: "int8") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "dan (generated)", id: "1487589974483-3") {
		createTable(tableName: "video_progress") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "video_progresPK")
			}

			column(name: "user_id", type: "int8")

			column(name: "uuid", type: "varchar(255)")

			column(name: "video_id", type: "int8") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "dan (generated)", id: "1487589974483-4") {
		createTable(tableName: "video_visit") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "video_visitPK")
			}

			column(name: "progress_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "timestamp", type: "timestamp") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "dan (generated)", id: "1487589974483-12") {
		addForeignKeyConstraint(baseColumnNames: "progress_id", baseTableName: "video_answer", constraintName: "FK_sjyafttp9uqxocmc7rxk86yhv", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "video_progress", referencesUniqueColumn: "false")
	}

	changeSet(author: "dan (generated)", id: "1487589974483-13") {
		addForeignKeyConstraint(baseColumnNames: "parent_id", baseTableName: "video_answer_details", constraintName: "FK_ggj17rpkqhemj7dyjni3t95v4", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "video_answer", referencesUniqueColumn: "false")
	}

	changeSet(author: "dan (generated)", id: "1487589974483-14") {
		addForeignKeyConstraint(baseColumnNames: "user_id", baseTableName: "video_progress", constraintName: "FK_m83qqpp9hhkqk1wc4ienn95cj", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "myusers", referencesUniqueColumn: "false")
	}

	changeSet(author: "dan (generated)", id: "1487589974483-15") {
		addForeignKeyConstraint(baseColumnNames: "video_id", baseTableName: "video_progress", constraintName: "FK_h98wq0ioonqgb60jfr1xarmma", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "exercise", referencesUniqueColumn: "false")
	}

	changeSet(author: "dan (generated)", id: "1487589974483-16") {
		addForeignKeyConstraint(baseColumnNames: "progress_id", baseTableName: "video_visit", constraintName: "FK_2ttayl75uxy4pcvu38lsr81i7", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "video_progress", referencesUniqueColumn: "false")
	}
}
