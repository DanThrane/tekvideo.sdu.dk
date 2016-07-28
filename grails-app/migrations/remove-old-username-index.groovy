import dk.sdu.tekvideo.User

databaseChangeLog = {
    changeSet(author: "Dan", id: "Remove old username index") {
        grailsChange {
            change {
                sql.execute("ALTER TABLE myusers DROP CONSTRAINT uk_l5uhqaxvn8cef5srtdseyxvx9;")
            }
        }
    }
}
