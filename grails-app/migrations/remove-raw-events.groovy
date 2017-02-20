databaseChangeLog = {
	changeSet(author: "dan (generated)", id: "1487583271467-8") {
		dropForeignKeyConstraint(baseTableName: "event", baseTableSchemaName: "public", constraintName: "fk_p84ruvsg7mfwb2x5p7iq3q103")
	}

	changeSet(author: "dan (generated)", id: "1487583271467-9") {
		dropIndex(indexName: "name_uniq_1479211026791", tableName: "temporary_exercise")
	}

	changeSet(author: "dan (generated)", id: "1487583271467-10") {
		dropTable(tableName: "event")
	}

	changeSet(author: "dan (generated)", id: "1487583271467-11") {
		dropTable(tableName: "temporary_exercise")
	}
}
