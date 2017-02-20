databaseChangeLog = {

	changeSet(author: "dan (generated)", id: "1487598431018-1") {
		createTable(tableName: "written_exercise_visit") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "written_exerc_visPK")
			}

			column(name: "progress_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "sub_exercise_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "timestamp", type: "timestamp") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "dan (generated)", id: "1487598431018-2") {
		createTable(tableName: "written_group_answer") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "written_group_ansPK")
			}

			column(name: "exercise_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "passes", type: "boolean") {
				constraints(nullable: "false")
			}

			column(name: "progress_id", type: "int8") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "dan (generated)", id: "1487598431018-3") {
		createTable(tableName: "written_group_streak") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "written_group_streakPK")
			}

			column(name: "current_streak", type: "int4") {
				constraints(nullable: "false")
			}

			column(name: "longest_streak", type: "int4") {
				constraints(nullable: "false")
			}

			column(name: "progress_id", type: "int8") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "dan (generated)", id: "1487598431018-13") {
		addForeignKeyConstraint(baseColumnNames: "progress_id", baseTableName: "written_exercise_visit", constraintName: "FK_nx9vh99rxb64137p7t6ii8d4f", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "exercise_progress", referencesUniqueColumn: "false")
	}

	changeSet(author: "dan (generated)", id: "1487598431018-14") {
		addForeignKeyConstraint(baseColumnNames: "sub_exercise_id", baseTableName: "written_exercise_visit", constraintName: "FK_hshaivq6xg79379u13n357bg5", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "written_exercise", referencesUniqueColumn: "false")
	}

	changeSet(author: "dan (generated)", id: "1487598431018-15") {
		addForeignKeyConstraint(baseColumnNames: "exercise_id", baseTableName: "written_group_answer", constraintName: "FK_288oqbfpulrt1nsbea085bcir", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "written_exercise", referencesUniqueColumn: "false")
	}

	changeSet(author: "dan (generated)", id: "1487598431018-16") {
		addForeignKeyConstraint(baseColumnNames: "progress_id", baseTableName: "written_group_answer", constraintName: "FK_kxfvr51cspcj2aprpkvq967j6", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "exercise_progress", referencesUniqueColumn: "false")
	}

	changeSet(author: "dan (generated)", id: "1487598431018-17") {
		addForeignKeyConstraint(baseColumnNames: "progress_id", baseTableName: "written_group_streak", constraintName: "FK_a0c4atdg9ummjcyto02xl6i99", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "exercise_progress", referencesUniqueColumn: "false")
	}
}
