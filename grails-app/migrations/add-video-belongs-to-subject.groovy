import dk.sdu.tekvideo.Subject
import dk.sdu.tekvideo.Video

databaseChangeLog = {

    changeSet(author: "Admin (generated)", id: "1442583748918-1") {
        createTable(tableName: "comment") {
            column(name: "id", type: "int8") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "commentPK")
            }

            column(name: "version", type: "int8") {
                constraints(nullable: "false")
            }

            column(name: "contents", type: "text") {
                constraints(nullable: "false")
            }

            column(name: "date_created", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "user_id", type: "int8") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "Admin (generated)", id: "1442583748918-2") {
        createTable(tableName: "video_comment") {
            column(name: "video_comments_id", type: "int8")

            column(name: "comment_id", type: "int8")
        }
    }

    changeSet(author: "Admin (generated)", id: "1442583748918-3") {
        addColumn(tableName: "video") {
            column(name: "subject_id", type: "int8")
        }
    }

    changeSet(author: "Admin (generated)", id: "1442583748918-5") {
        addNotNullConstraint(columnDataType: "int8", columnName: "student_id", tableName: "course_student")
    }

    changeSet(author: "Admin (generated)", id: "1442583748918-6") {
        modifyDataType(columnName: "correct", newDataType: "boolean", tableName: "event")
    }

    changeSet(author: "Admin (generated)", id: "1442583748918-7") {
        modifyDataType(columnName: "account_expired", newDataType: "boolean", tableName: "myusers")
    }

    changeSet(author: "Admin (generated)", id: "1442583748918-8") {
        modifyDataType(columnName: "account_locked", newDataType: "boolean", tableName: "myusers")
    }

    changeSet(author: "Admin (generated)", id: "1442583748918-9") {
        modifyDataType(columnName: "enabled", newDataType: "boolean", tableName: "myusers")
    }

    changeSet(author: "Admin (generated)", id: "1442583748918-10") {
        modifyDataType(columnName: "password_expired", newDataType: "boolean", tableName: "myusers")
    }

    changeSet(author: "Admin (generated)", id: "1442583748918-11") {
        modifyDataType(columnName: "spring", newDataType: "boolean", tableName: "semester")
    }

    changeSet(author: "Admin (generated)", id: "1442583748918-12") {
        modifyDataType(columnName: "video_type", newDataType: "boolean", tableName: "video")
    }

    changeSet(author: "Admin (generated)", id: "1442583748918-13") {
        addPrimaryKey(columnNames: "id", constraintName: "course_studenPK", tableName: "course_student")
    }

    changeSet(author: "Admin (generated)", id: "1442583748918-14") {
        dropForeignKeyConstraint(baseTableName: "course_student", baseTableSchemaName: "public", constraintName: "fk_sv1i8oao41apn7tid0t9625lm")
    }

    changeSet(author: "Admin (generated)", id: "1442583748918-15") {
        dropForeignKeyConstraint(baseTableName: "event", baseTableSchemaName: "public", constraintName: "fk_rj3dgj7ybcm3ky31vro20f7rf")
    }

    changeSet(author: "Admin (generated)", id: "1442583748918-16") {
        dropForeignKeyConstraint(baseTableName: "event", baseTableSchemaName: "public", constraintName: "fk_n512drne35f8bx23o71yj298q")
    }

    changeSet(author: "Admin (generated)", id: "1442583748918-17") {
        dropForeignKeyConstraint(baseTableName: "event", baseTableSchemaName: "public", constraintName: "fk_m0spahucwfamp4ngf1t36wiwr")
    }



    changeSet(author: "Admin (generated)", id: "1442583748918-24") {
        dropColumn(columnName: "course_students_id", tableName: "course_student")
    }

    changeSet(author: "Admin (generated)", id: "1442583748918-25") {
        dropColumn(columnName: "course_id", tableName: "event")
    }

    changeSet(author: "Admin (generated)", id: "1442583748918-26") {
        dropColumn(columnName: "subject_id", tableName: "event")
    }

    changeSet(author: "Admin (generated)", id: "1442583748918-27") {
        dropColumn(columnName: "teacher_id", tableName: "event")
    }

    changeSet(author: "Admin (generated)", id: "1442583748918-20") {
        addForeignKeyConstraint(baseColumnNames: "user_id", baseTableName: "comment", constraintName: "FK_mxoojfj9tmy8088avf57mpm02", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "myusers", referencesUniqueColumn: "false")
    }

    changeSet(author: "Admin (generated)", id: "1442583748918-21") {
        addForeignKeyConstraint(baseColumnNames: "subject_id", baseTableName: "video", constraintName: "FK_e2mqsqcagnqvsq1iv37uklumo", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "subject", referencesUniqueColumn: "false")
    }

    changeSet(author: "Admin (generated)", id: "1442583748918-22") {
        addForeignKeyConstraint(baseColumnNames: "comment_id", baseTableName: "video_comment", constraintName: "FK_k15a7vs0bl11b7q3bc88x9nw3", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "comment", referencesUniqueColumn: "false")
    }

    changeSet(author: "Admin (generated)", id: "1442583748918-23") {
        addForeignKeyConstraint(baseColumnNames: "video_comments_id", baseTableName: "video_comment", constraintName: "FK_sif69tfsrdmn7vdnmpja18r9b", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "video", referencesUniqueColumn: "false")
    }

    changeSet(author: "Admin (generated)", id: "1442583748918-4") {
        addColumn(tableName: "video") {
            column(name: "videos_idx", type: "int4")
        }
    }

    changeSet(author: "Dan", id: "set default values for videos belonging to subjects") {
        grailsChange {
            change {
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

    changeSet(author: "Dan", id: "Relax foreign key constraint for video events") {
        dropForeignKeyConstraint(baseTableName: "event", baseTableSchemaName: "public", constraintName: "fk_fpflfp7l8koab0pwtyav2uyqs")
    }
}
