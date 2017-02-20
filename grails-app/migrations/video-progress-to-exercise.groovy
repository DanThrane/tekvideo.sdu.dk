databaseChangeLog = {

	changeSet(author: "dan (generated)", id: "1487595879571-1") {
		createTable(tableName: "exercise_progress") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "exercise_progPK")
			}

			column(name: "exercise_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "user_id", type: "int8")

			column(name: "uuid", type: "varchar(255)")
		}
	}

	changeSet(author: "dan (generated)", id: "1487595879571-2") {
		createTable(tableName: "exercise_visit") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "exercise_visiPK")
			}

			column(name: "progress_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "timestamp", type: "timestamp") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "dan (generated)", id: "1487595879571-12") {
		dropForeignKeyConstraint(baseTableName: "video_answer", baseTableSchemaName: "public", constraintName: "FK_sjyafttp9uqxocmc7rxk86yhv")
	}

	changeSet(author: "dan (generated)", id: "1487595879571-13") {
		dropForeignKeyConstraint(baseTableName: "video_progress", baseTableSchemaName: "public", constraintName: "FK_m83qqpp9hhkqk1wc4ienn95cj")
	}

	changeSet(author: "dan (generated)", id: "1487595879571-14") {
		dropForeignKeyConstraint(baseTableName: "video_progress", baseTableSchemaName: "public", constraintName: "FK_h98wq0ioonqgb60jfr1xarmma")
	}

	changeSet(author: "dan (generated)", id: "1487595879571-15") {
		dropForeignKeyConstraint(baseTableName: "video_visit", baseTableSchemaName: "public", constraintName: "FK_2ttayl75uxy4pcvu38lsr81i7")
	}

	changeSet(author: "dan (generated)", id: "1487595879571-20") {
		dropTable(tableName: "video_progress")
	}

	changeSet(author: "dan (generated)", id: "1487595879571-21") {
		dropTable(tableName: "video_visit")
	}

	changeSet(author: "dan (generated)", id: "1487595879571-16") {
		addForeignKeyConstraint(baseColumnNames: "exercise_id", baseTableName: "exercise_progress", constraintName: "FK_7vmkv6c7qkpwyuj1y9qbi1i3g", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "exercise", referencesUniqueColumn: "false")
	}

	changeSet(author: "dan (generated)", id: "1487595879571-17") {
		addForeignKeyConstraint(baseColumnNames: "user_id", baseTableName: "exercise_progress", constraintName: "FK_fo6p32oe9fybaw1rtn3ctvvsn", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "myusers", referencesUniqueColumn: "false")
	}

	changeSet(author: "dan (generated)", id: "1487595879571-18") {
		addForeignKeyConstraint(baseColumnNames: "progress_id", baseTableName: "exercise_visit", constraintName: "FK_coad7hgjcrq2p4vaexo0gclgs", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "exercise_progress", referencesUniqueColumn: "false")
	}

	changeSet(author: "dan (generated)", id: "1487595879571-19") {
		addForeignKeyConstraint(baseColumnNames: "progress_id", baseTableName: "video_answer", constraintName: "FK_sjyafttp9uqxocmc7rxk86yhv", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "exercise_progress", referencesUniqueColumn: "false")
	}
}
