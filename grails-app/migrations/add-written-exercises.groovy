databaseChangeLog = {
	changeSet(author: "dan (generated)", id: "1481454465736-1") {
		createTable(tableName: "written_exercise") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "written_exercPK")
			}

			column(name: "exercise", type: "text") {
				constraints(nullable: "false")
			}
		}
	}
	changeSet(author: "dan (generated)", id: "1481464052116-1") {
		createTable(tableName: "written_exercise_group_written_exercise") {
			column(name: "written_exercise_group_exercises_id", type: "int8")

			column(name: "written_exercise_id", type: "int8")
		}
	}

	changeSet(author: "dan (generated)", id: "1481464052116-10") {
		addForeignKeyConstraint(baseColumnNames: "written_exercise_group_exercises_id", baseTableName: "written_exercise_group_written_exercise", constraintName: "FK_ofpk2cn15iueh2dj8mk4dphin", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "exercise", referencesUniqueColumn: "false")
	}

	changeSet(author: "dan (generated)", id: "1481464052116-11") {
		addForeignKeyConstraint(baseColumnNames: "written_exercise_id", baseTableName: "written_exercise_group_written_exercise", constraintName: "FK_eoillvx3dx0aj71uwgeoc8trn", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "written_exercise", referencesUniqueColumn: "false")
	}
}
