databaseChangeLog = {
	changeSet(author: "Admin (generated)", id: "1462606941717-1") {
		createTable(tableName: "course_subject") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "course_subjecPK")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "course_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "subject_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "weight", type: "int4") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "Admin (generated)", id: "1462606941717-2") {
		createTable(tableName: "subject_video") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "subject_videoPK")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "subject_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "video_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "weight", type: "int4") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "Admin (generated)", id: "1462606941717-10") {
		dropForeignKeyConstraint(baseTableName: "subject", baseTableSchemaName: "public", constraintName: "fk_sap4lbm82mrwih3wjsymu9l3w")
	}

	changeSet(author: "Admin (generated)", id: "1462606941717-11") {
		dropForeignKeyConstraint(baseTableName: "video", baseTableSchemaName: "public", constraintName: "FK_e2mqsqcagnqvsq1iv37uklumo")
	}

	changeSet(author: "Admin (generated)", id: "1462606941717-16") {
		dropColumn(columnName: "course_id", tableName: "subject")
	}

	changeSet(author: "Admin (generated)", id: "1462606941717-17") {
		dropColumn(columnName: "subjects_idx", tableName: "subject")
	}

	changeSet(author: "Admin (generated)", id: "1462606941717-18") {
		dropColumn(columnName: "subject_id", tableName: "video")
	}

	changeSet(author: "Admin (generated)", id: "1462606941717-19") {
		dropColumn(columnName: "videos_idx", tableName: "video")
	}

	changeSet(author: "Admin (generated)", id: "1462606941717-12") {
		addForeignKeyConstraint(baseColumnNames: "course_id", baseTableName: "course_subject", constraintName: "FK_fsosg9nltl22wt4lsn4ix9ce1", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "course", referencesUniqueColumn: "false")
	}

	changeSet(author: "Admin (generated)", id: "1462606941717-13") {
		addForeignKeyConstraint(baseColumnNames: "subject_id", baseTableName: "course_subject", constraintName: "FK_6guci9n6n84xrk7w7kbe7qedx", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "subject", referencesUniqueColumn: "false")
	}

	changeSet(author: "Admin (generated)", id: "1462606941717-14") {
		addForeignKeyConstraint(baseColumnNames: "subject_id", baseTableName: "subject_video", constraintName: "FK_5997ujs63ju1mbirylenufaby", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "subject", referencesUniqueColumn: "false")
	}

	changeSet(author: "Admin (generated)", id: "1462606941717-15") {
		addForeignKeyConstraint(baseColumnNames: "video_id", baseTableName: "subject_video", constraintName: "FK_5j8kljdcb1pq7p7hkejrhyp77", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "video", referencesUniqueColumn: "false")
	}
}
