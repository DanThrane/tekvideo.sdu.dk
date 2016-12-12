databaseChangeLog = {

	changeSet(author: "dan (generated)", id: "1481554576192-1") {
		addColumn(tableName: "exercise") {
			column(name: "thumbnail_url", type: "varchar(255)")
		}
	}

}
