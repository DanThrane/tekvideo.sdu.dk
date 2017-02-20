databaseChangeLog = {

	changeSet(author: "dan (generated)", id: "1487579422743-1") {
		addColumn(tableName: "event") {
			column(name: "uuid", type: "varchar(255)")
		}
	}

}
