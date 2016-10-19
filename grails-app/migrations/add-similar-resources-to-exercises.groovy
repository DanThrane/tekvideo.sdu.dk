databaseChangeLog = {

	changeSet(author: "Admin (generated)", id: "1475826524325-1") {
		createTable(tableName: "exercise_similar_resources") {
			column(name: "exercise_similar_resources_id", type: "int8")

			column(name: "similar_resources_id", type: "int8")
		}
	}

	changeSet(author: "Admin (generated)", id: "1475826524325-2") {
		createTable(tableName: "similar_resources") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "similar_resouPK")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "link", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "title", type: "text") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "Admin (generated)", id: "1475826524325-11") {
		addForeignKeyConstraint(baseColumnNames: "exercise_similar_resources_id", baseTableName: "exercise_similar_resources", constraintName: "FK_jd8eklpdyrykhx3ed9yjm7iud", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "exercise", referencesUniqueColumn: "false")
	}

	changeSet(author: "Admin (generated)", id: "1475826524325-12") {
		addForeignKeyConstraint(baseColumnNames: "similar_resources_id", baseTableName: "exercise_similar_resources", constraintName: "FK_trg6wn56lcqkfxrh32c0dxaem", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "similar_resources", referencesUniqueColumn: "false")
	}
}
