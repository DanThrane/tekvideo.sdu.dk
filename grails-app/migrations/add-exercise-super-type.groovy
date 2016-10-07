databaseChangeLog = {

	changeSet(author: "dan (generated)", id: "1475399016945-1") {
		createTable(tableName: "exercise") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "exercisePK")
			}

			column(name: "date_created", type: "timestamp") {
				constraints(nullable: "false")
			}

			column(name: "description", type: "text")

			column(name: "local_status", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "name", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "class", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "timeline_json", type: "text")

			column(defaultValue: "true", name: "video_type", type: "boolean")

			column(name: "youtube_id", type: "varchar(255)")
		}
	}

	changeSet(author: "dan (generated)", id: "1475399016945-2") {
		createTable(tableName: "exercise_comment") {
			column(name: "exercise_comments_id", type: "int8")

			column(name: "comment_id", type: "int8")
		}
	}

	changeSet(author: "dan (generated)", id: "1475399016945-3") {
		createTable(tableName: "subject_exercise") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "subject_exercPK")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "exercise_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "subject_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "weight", type: "int4") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "dan (generated)", id: "1475399016945-15") {
		addForeignKeyConstraint(baseColumnNames: "comment_id", baseTableName: "exercise_comment", constraintName: "FK_d846m676e4xnr4ujlhbs046nb", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "comment", referencesUniqueColumn: "false")
	}

	changeSet(author: "dan (generated)", id: "1475399016945-16") {
		addForeignKeyConstraint(baseColumnNames: "exercise_comments_id", baseTableName: "exercise_comment", constraintName: "FK_7vag9e273cjmoabhbs7gf0ka6", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "exercise", referencesUniqueColumn: "false")
	}

    changeSet(author: "Dan", id: "Migrate videos to supertype") {
        grailsChange {
            change {
                int count = 0
                sql.eachRow("SELECT COUNT(*) FROM video;") { count = it.count }

                if (count > 0) {
                    List<Map<String, Object>> existingVideos = []
                    def videoSubject = [:]
                    sql.eachRow("SELECT * FROM video;") {
                        def video = [:]
                        video.id = it.id
                        video.date_created = it.date_created
                        video.description = it.description
                        video.local_status = it.local_status
                        video.name = it.name
                        video.timeline_json = it.timeline_json
                        video.video_type = it.video_type
                        video.youtube_id = it.youtube_id

                        video.class = "dk.sdu.tekvideo.Video"

                        existingVideos += video
                    }

                    sql.eachRow("SELECT * FROM subject_video;") {
                        videoSubject[it.video_id] = [
                                subject_id : it.subject_id,
                                exercise_id: it.video_id,
                                weight     : it.weight
                        ]
                    }

                    existingVideos.each { video ->
                        def id = video.remove("id")
                        def values = sql.executeInsert(video, """
                            INSERT INTO exercise
                                (id, date_created, description, local_status, name, class, timeline_json, video_type,
                                 youtube_id)
                            VALUES
                                (nextval('hibernate_sequence'), :date_created, :description, :local_status, :name, :class,
                                :timeline_json, :video_type, :youtube_id)
                        """)
                        def newVideoId = values[0][0]
                        def subject = videoSubject[id]
                        if (subject != null) {
                            println "Inserting to subject $subject"
                            subject.exercise_id = newVideoId
                            sql.executeInsert(subject, """
                                INSERT INTO subject_exercise
                                    (id, version, subject_id, exercise_id, weight)
                                VALUES
                                    (nextval('hibernate_sequence'), 1, :subject_id, :exercise_id, :weight);
                            """)
                        }
                        println "Updating $id -> $newVideoId"
                        sql.executeUpdate([old_video_id: id, new_video_id: newVideoId], """
                            UPDATE event
                            SET video_id = :new_video_id
                            WHERE
                              video_id = :old_video_id;
                        """)
                    }
                }
            }
        }
    }


    changeSet(author: "dan (generated)", id: "1475399016945-11") {
		dropForeignKeyConstraint(baseTableName: "subject_video", baseTableSchemaName: "public", constraintName: "FK_5997ujs63ju1mbirylenufaby")
	}

	changeSet(author: "dan (generated)", id: "1475399016945-12") {
		dropForeignKeyConstraint(baseTableName: "subject_video", baseTableSchemaName: "public", constraintName: "FK_5j8kljdcb1pq7p7hkejrhyp77")
	}

	changeSet(author: "dan (generated)", id: "1475399016945-13") {
		dropForeignKeyConstraint(baseTableName: "video_comment", baseTableSchemaName: "public", constraintName: "fk_k15a7vs0bl11b7q3bc88x9nw3")
	}

	changeSet(author: "dan (generated)", id: "1475399016945-14") {
		dropForeignKeyConstraint(baseTableName: "video_comment", baseTableSchemaName: "public", constraintName: "fk_sif69tfsrdmn7vdnmpja18r9b")
	}

	changeSet(author: "dan (generated)", id: "1475399016945-19") {
		dropTable(tableName: "subject_video")
	}

	changeSet(author: "dan (generated)", id: "1475399016945-20") {
		dropTable(tableName: "video")
	}

	changeSet(author: "dan (generated)", id: "1475399016945-21") {
		dropTable(tableName: "video_comment")
	}

	changeSet(author: "dan (generated)", id: "1475399016945-17") {
		addForeignKeyConstraint(baseColumnNames: "exercise_id", baseTableName: "subject_exercise", constraintName: "FK_kt98qv321vunf9lsona6hmt3p", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "exercise", referencesUniqueColumn: "false")
	}

	changeSet(author: "dan (generated)", id: "1475399016945-18") {
		addForeignKeyConstraint(baseColumnNames: "subject_id", baseTableName: "subject_exercise", constraintName: "FK_p9rd844jee162ttgnhggvb3is", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "subject", referencesUniqueColumn: "false")
	}
}
