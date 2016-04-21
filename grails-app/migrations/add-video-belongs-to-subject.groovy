import dk.sdu.tekvideo.Subject
import dk.sdu.tekvideo.Video

databaseChangeLog = {

    changeSet(author: "Admin (generated)", id: "1442739717408-1") {
        addColumn(tableName: "video") {
            column(name: "subject_id", type: "int8")
        }
    }

    changeSet(author: "Admin (generated)", id: "1442739717408-2") {
        addNotNullConstraint(columnDataType: "int8", columnName: "student_id", tableName: "course_student")
    }

    changeSet(author: "Admin (generated)", id: "1442739717408-3") {
        modifyDataType(columnName: "correct", newDataType: "boolean", tableName: "event")
    }

    changeSet(author: "Admin (generated)", id: "1442739717408-4") {
        modifyDataType(columnName: "account_expired", newDataType: "boolean", tableName: "myusers")
    }

    changeSet(author: "Admin (generated)", id: "1442739717408-5") {
        modifyDataType(columnName: "account_locked", newDataType: "boolean", tableName: "myusers")
    }

    changeSet(author: "Admin (generated)", id: "1442739717408-6") {
        modifyDataType(columnName: "enabled", newDataType: "boolean", tableName: "myusers")
    }

    changeSet(author: "Admin (generated)", id: "1442739717408-7") {
        modifyDataType(columnName: "password_expired", newDataType: "boolean", tableName: "myusers")
    }

    changeSet(author: "Admin (generated)", id: "1442739717408-8") {
        modifyDataType(columnName: "spring", newDataType: "boolean", tableName: "semester")
    }

    changeSet(author: "Admin (generated)", id: "1442739717408-9") {
        modifyDataType(columnName: "video_type", newDataType: "boolean", tableName: "video")
    }

    changeSet(author: "Admin (generated)", id: "1442739717408-10") {
        addPrimaryKey(columnNames: "id", constraintName: "course_studenPK", tableName: "course_student")
    }

    changeSet(author: "Admin (generated)", id: "1442739717408-11") {
        dropForeignKeyConstraint(baseTableName: "course2", baseTableSchemaName: "public", constraintName: "fk_cx4j15qtvp0qkfrluvkvh2dy3")
    }

    changeSet(author: "Admin (generated)", id: "1442739717408-12") {
        dropForeignKeyConstraint(baseTableName: "course2", baseTableSchemaName: "public", constraintName: "fk_s1e0rfe5brw6e8fgh1jd2mand")
    }

    changeSet(author: "Admin (generated)", id: "1442739717408-13") {
        dropForeignKeyConstraint(baseTableName: "course_student", baseTableSchemaName: "public", constraintName: "fk_sv1i8oao41apn7tid0t9625lm")
    }

    changeSet(author: "Admin (generated)", id: "1442739717408-14") {
        dropForeignKeyConstraint(baseTableName: "course_student2", baseTableSchemaName: "public", constraintName: "fk_h6nu5yqk82dkivlsd6tp41id")
    }

    changeSet(author: "Admin (generated)", id: "1442739717408-15") {
        dropForeignKeyConstraint(baseTableName: "course_student2", baseTableSchemaName: "public", constraintName: "fk_dw4yor8qomm1bej0nkihv5erc")
    }

    changeSet(author: "Admin (generated)", id: "1442739717408-16") {
        dropForeignKeyConstraint(baseTableName: "event", baseTableSchemaName: "public", constraintName: "fk_rj3dgj7ybcm3ky31vro20f7rf")
    }

    changeSet(author: "Admin (generated)", id: "1442739717408-17") {
        dropForeignKeyConstraint(baseTableName: "event", baseTableSchemaName: "public", constraintName: "fk_n512drne35f8bx23o71yj298q")
    }

    changeSet(author: "Admin (generated)", id: "1442739717408-18") {
        dropForeignKeyConstraint(baseTableName: "event", baseTableSchemaName: "public", constraintName: "fk_m0spahucwfamp4ngf1t36wiwr")
    }

    changeSet(author: "Admin (generated)", id: "1442739717408-19") {
        dropForeignKeyConstraint(baseTableName: "event", baseTableSchemaName: "public", constraintName: "fk_fpflfp7l8koab0pwtyav2uyqs")
    }

    changeSet(author: "Admin (generated)", id: "1442739717408-20") {
        dropForeignKeyConstraint(baseTableName: "subject2", baseTableSchemaName: "public", constraintName: "fk_h7t837kcuqd7aafdwe13crqla")
    }


    changeSet(author: "Admin (generated)", id: "1442739717408-23") {
        dropForeignKeyConstraint(baseTableName: "video2", baseTableSchemaName: "public", constraintName: "fk_ik23il7osoignfd1sxyvy8t7s")
    }

    changeSet(author: "Admin (generated)", id: "1442739717408-25") {
        dropColumn(columnName: "course_students_id", tableName: "course_student")
    }

    changeSet(author: "Admin (generated)", id: "1442739717408-26") {
        dropColumn(columnName: "course_id", tableName: "event")
    }

    changeSet(author: "Admin (generated)", id: "1442739717408-27") {
        dropColumn(columnName: "subject_id", tableName: "event")
    }

    changeSet(author: "Admin (generated)", id: "1442739717408-28") {
        dropColumn(columnName: "teacher_id", tableName: "event")
    }

    changeSet(author: "Admin (generated)", id: "1442739717408-29") {
        dropTable(tableName: "course2")
    }

    changeSet(author: "Admin (generated)", id: "1442739717408-30") {
        dropTable(tableName: "course_student2")
    }

    changeSet(author: "Admin (generated)", id: "1442739717408-31") {
        dropTable(tableName: "subject2")
    }

    changeSet(author: "Admin (generated)", id: "1442739717408-33") {
        dropTable(tableName: "video2")
    }

    changeSet(author: "Admin (generated)", id: "1442739717408-24") {
        addForeignKeyConstraint(baseColumnNames: "subject_id", baseTableName: "video", constraintName: "FK_e2mqsqcagnqvsq1iv37uklumo", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "subject", referencesUniqueColumn: "false")
    }

    changeSet(author: "Dan", id: "set default values for videos belonging to subjects") {
        grailsChange {
            change {
                int count = 0
                sql.eachRow("SELECT COUNT(*) FROM video;") {
                    count = it.count
                }

                if (count > 0) {
                    def defaultSubject = Subject.list()[0]
                    Video.list().each { video ->
                        Long id = null
                        sql.eachRow("SELECT subject_id FROM subject_videos WHERE video_id = ${video.id};") {
                            id = it.subject_id
                        }

                        def subject = Subject.get(id) ?: defaultSubject
                        video.subject = subject
                        subject.addToVideos(video)
                        video.save(failOnError: true, flush: true)
                    }
                }
            }
        }
        addNotNullConstraint(columnName: "subject_id", tableName: "video")
    }

    changeSet(author: "Admin (generated)", id: "1442583748918-18") {
        dropForeignKeyConstraint(baseTableName: "subject_videos", baseTableSchemaName: "public", constraintName: "fk_5g32v6o7cadhe8sfm2wph4ckg")
    }

    changeSet(author: "Admin (generated)", id: "1442583748918-19") {
        dropForeignKeyConstraint(baseTableName: "subject_videos", baseTableSchemaName: "public", constraintName: "fk_ll9o3lul9d4pvxwmfsyp6sebx")
    }

    changeSet(author: "Admin (generated)", id: "1442583748918-28") {
        dropTable(tableName: "subject_videos")
    }
}
