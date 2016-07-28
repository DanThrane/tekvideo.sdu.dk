import dk.sdu.tekvideo.User

databaseChangeLog = {

	changeSet(author: "Dan", id: "set default cas status") {
        addColumn(tableName: "myusers") {
            column(name: "is_cas", type: "boolean")
        }

		grailsChange {
			change {
				int count = 0
				sql.eachRow("SELECT COUNT(*) FROM myusers;") { count = it.count }
				if (count > 0) {
                    User.list().each {
                        it.isCas = false
                        it.save(flush: true, failOnError: true)
                    }
				}
			}
		}

        addNotNullConstraint(columnName: "is_cas", tableName: "myusers")
	}

	changeSet(author: "Admin (generated)", id: "1469694568090-9") {
		createIndex(indexName: "unique_username", tableName: "myusers", unique: "true") {
			column(name: "is_cas")

			column(name: "username")
		}
	}
}
