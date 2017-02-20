databaseChangeLog = {

	changeSet(author: "dan (generated)", id: "1487599892808-1") {
		addColumn(tableName: "written_group_answer") {
			column(name: "date_created", type: "timestamp") {
				constraints(nullable: "false")
			}
		}
	}

}
