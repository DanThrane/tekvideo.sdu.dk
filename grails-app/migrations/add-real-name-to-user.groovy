databaseChangeLog = {
	changeSet(author: "Admin (generated)", id: "1456566685990-1") {
		addColumn(tableName: "myusers") {
			column(name: "real_name", type: "varchar(255)")
		}
	}
}
