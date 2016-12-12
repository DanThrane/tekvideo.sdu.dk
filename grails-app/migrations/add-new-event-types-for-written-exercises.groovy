databaseChangeLog = {

	changeSet(author: "dan (generated)", id: "1481542252779-1") {
		addColumn(tableName: "event") {
			column(name: "exercise_id", type: "int8")
		}
	}

	changeSet(author: "dan (generated)", id: "1481542252779-2") {
		addColumn(tableName: "event") {
			column(name: "group_id", type: "int8")
		}
	}

}
