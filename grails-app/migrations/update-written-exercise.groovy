databaseChangeLog = {

	changeSet(author: "dan (generated)", id: "1480249830631-1") {
		addColumn(tableName: "exercise") {
			column(name: "exercise", type: "text")
		}
	}

}
