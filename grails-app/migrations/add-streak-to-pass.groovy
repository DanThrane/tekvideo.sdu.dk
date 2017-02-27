import dk.sdu.tekvideo.*

databaseChangeLog = {
    changeSet(author: "dan (generated)", id: "1488197539291-1") {
        addColumn(tableName: "exercise") {
            column(name: "streak_to_pass", type: "int4")
        }

        grailsChange {
            change {
                int count = 0
                sql.eachRow("SELECT COUNT(*) FROM exercise;") { count = it.count }
                if (count > 0) {
                    WrittenExerciseGroup.list().each {
                        it.streakToPass = 1
                        it.save(flush: true)
                    }
                }
            }
        }
    }
}
