databaseChangeLog = {

	changeSet(author: "dan (generated)", id: "1479211027501-1") {
		createTable(tableName: "temporary_exercise") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "temporary_exePK")
			}

			column(name: "exercise", type: "text") {
				constraints(nullable: "false")
			}

			column(name: "name", type: "varchar(255)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "dan (generated)", id: "1479211027501-10") {
		createIndex(indexName: "name_uniq_1479211026791", tableName: "temporary_exercise", unique: "true") {
			column(name: "name")
		}
	}
}
