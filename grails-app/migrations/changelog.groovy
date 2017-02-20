databaseChangeLog = {

	changeSet(author: "Admin (generated)", id: "1442739448243-1") {
		createTable(tableName: "comment") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "comment_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "contents", type: "TEXT") {
				constraints(nullable: "false")
			}

			column(name: "date_created", type: "TIMESTAMP WITH TIME ZONE") {
				constraints(nullable: "false")
			}

			column(name: "user_id", type: "int8") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "Admin (generated)", id: "1442739448243-2") {
		createTable(tableName: "course") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "course_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "description", type: "TEXT") {
				constraints(nullable: "false")
			}

			column(name: "full_name", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "name", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "semester_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "teacher_id", type: "int8") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "Admin (generated)", id: "1442739448243-3") {
		createTable(tableName: "course2") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "course2_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "description", type: "TEXT") {
				constraints(nullable: "false")
			}

			column(name: "full_name", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "name", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "semester_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "teacher_id", type: "int8") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "Admin (generated)", id: "1442739448243-4") {
		createTable(tableName: "course_student") {
			column(name: "course_students_id", type: "int8")

			column(name: "student_id", type: "int8")

			column(name: "id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "course_id", type: "int8") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "Admin (generated)", id: "1442739448243-5") {
		createTable(tableName: "course_student2") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "course_student2_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "course_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "student_id", type: "int8") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "Admin (generated)", id: "1442739448243-6") {
		createTable(tableName: "event") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "event_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "timestamp", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "user_id", type: "int8")

			column(name: "class", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "course_id", type: "int8")

			column(name: "subject_id", type: "int8")

			column(name: "teacher_id", type: "int8")

			column(name: "video_id", type: "int8")

			column(name: "label", type: "VARCHAR(512)")

			column(name: "skipped_at", type: "int8")

			column(name: "video", type: "VARCHAR(512)")

			column(name: "video_time_code", type: "int8")

			column(name: "answer", type: "VARCHAR(512)")

			column(name: "correct", type: "bool")

			column(name: "time", type: "int8")

			column(name: "ua", type: "VARCHAR(512)")

			column(name: "url", type: "VARCHAR(512)")

			column(name: "timecode", type: "int8")
		}
	}

	changeSet(author: "Admin (generated)", id: "1442739448243-7") {
		createTable(tableName: "myusers") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "myusers_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "account_expired", type: "bool") {
				constraints(nullable: "false")
			}

			column(name: "account_locked", type: "bool") {
				constraints(nullable: "false")
			}

			column(name: "elearn_id", type: "VARCHAR(255)")

			column(name: "email", type: "VARCHAR(255)")

			column(name: "enabled", type: "bool") {
				constraints(nullable: "false")
			}

			column(name: "password", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "password_expired", type: "bool") {
				constraints(nullable: "false")
			}

			column(name: "username", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "Admin (generated)", id: "1442739448243-8") {
		createTable(tableName: "registration_code") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "registration_code_pkey")
			}

			column(name: "date_created", type: "TIMESTAMP WITH TIME ZONE") {
				constraints(nullable: "false")
			}

			column(name: "token", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "username", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "Admin (generated)", id: "1442739448243-9") {
		createTable(tableName: "role") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "role_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "authority", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "Admin (generated)", id: "1442739448243-10") {
		createTable(tableName: "semester") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "semester_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "spring", type: "bool") {
				constraints(nullable: "false")
			}

			column(name: "year", type: "int4") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "Admin (generated)", id: "1442739448243-11") {
		createTable(tableName: "student") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "student_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "user_id", type: "int8") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "Admin (generated)", id: "1442739448243-12") {
		createTable(tableName: "subject") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "subject_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "course_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "name", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "subjects_idx", type: "int4")

			column(name: "description", type: "TEXT")
		}
	}

	changeSet(author: "Admin (generated)", id: "1442739448243-13") {
		createTable(tableName: "subject2") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "subject2_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "course_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "description", type: "TEXT")

			column(name: "name", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "subjects_idx", type: "int4")
		}
	}

	changeSet(author: "Admin (generated)", id: "1442739448243-14") {
		createTable(tableName: "subject_videos") {
			column(name: "video_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "subject_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "videos_idx", type: "int4")
		}
	}

	changeSet(author: "Admin (generated)", id: "1442739448243-15") {
		createTable(tableName: "teacher") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "teacher_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "user_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "alias", type: "VARCHAR(255)")
		}
	}

	changeSet(author: "Admin (generated)", id: "1442739448243-16") {
		createTable(tableName: "user_role") {
			column(name: "role_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "user_id", type: "int8") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "Admin (generated)", id: "1442739448243-17") {
		createTable(tableName: "video") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "video_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "date_created", type: "TIMESTAMP WITH TIME ZONE") {
				constraints(nullable: "false")
			}

			column(name: "name", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "timeline_json", type: "TEXT")

			column(name: "youtube_id", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "description", type: "VARCHAR(255)")

			column(defaultValueBoolean: "true", name: "video_type", type: "bool") {
				constraints(nullable: "false")
			}

			column(name: "videos_idx", type: "int4")
		}
	}

	changeSet(author: "Admin (generated)", id: "1442739448243-18") {
		createTable(tableName: "video2") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "video2_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "date_created", type: "TIMESTAMP WITH TIME ZONE") {
				constraints(nullable: "false")
			}

			column(name: "description", type: "VARCHAR(255)")

			column(name: "name", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "subject_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "timeline_json", type: "TEXT")

			column(defaultValueBoolean: "true", name: "video_type", type: "bool") {
				constraints(nullable: "false")
			}

			column(name: "youtube_id", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "videos_idx", type: "int4")
		}
	}

	changeSet(author: "Admin (generated)", id: "1442739448243-19") {
		createTable(tableName: "video_comment") {
			column(name: "video_comments_id", type: "int8")

			column(name: "comment_id", type: "int8")
		}
	}

	changeSet(author: "Admin (generated)", id: "1442739448243-20") {
		addPrimaryKey(columnNames: "role_id, user_id", constraintName: "user_role_pkey", tableName: "user_role")
	}

	changeSet(author: "Admin (generated)", id: "1442739448243-21") {
		addUniqueConstraint(columnNames: "username", constraintName: "uk_l5uhqaxvn8cef5srtdseyxvx9", deferrable: "false", disabled: "false", initiallyDeferred: "false", tableName: "myusers")
	}

	changeSet(author: "Admin (generated)", id: "1442739448243-22") {
		addUniqueConstraint(columnNames: "authority", constraintName: "uk_irsamgnera6angm0prq1kemt2", deferrable: "false", disabled: "false", initiallyDeferred: "false", tableName: "role")
	}

	changeSet(author: "Admin (generated)", id: "1442739448243-49") {
		createSequence(schemaName: "public", sequenceName: "hibernate_sequence")
	}

	changeSet(author: "Admin (generated)", id: "1442739448243-23") {
		addForeignKeyConstraint(baseColumnNames: "user_id", baseTableName: "comment", baseTableSchemaName: "public", constraintName: "fk_mxoojfj9tmy8088avf57mpm02", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "myusers", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "Admin (generated)", id: "1442739448243-24") {
		addForeignKeyConstraint(baseColumnNames: "semester_id", baseTableName: "course", baseTableSchemaName: "public", constraintName: "fk_rycs8w7ludava0ahssdyy2i8k", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "semester", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "Admin (generated)", id: "1442739448243-25") {
		addForeignKeyConstraint(baseColumnNames: "teacher_id", baseTableName: "course", baseTableSchemaName: "public", constraintName: "fk_ps182hc66y3h1w7vrjwwo4rot", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "teacher", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "Admin (generated)", id: "1442739448243-26") {
		addForeignKeyConstraint(baseColumnNames: "semester_id", baseTableName: "course2", baseTableSchemaName: "public", constraintName: "fk_cx4j15qtvp0qkfrluvkvh2dy3", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "semester", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "Admin (generated)", id: "1442739448243-27") {
		addForeignKeyConstraint(baseColumnNames: "teacher_id", baseTableName: "course2", baseTableSchemaName: "public", constraintName: "fk_s1e0rfe5brw6e8fgh1jd2mand", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "teacher", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "Admin (generated)", id: "1442739448243-28") {
		addForeignKeyConstraint(baseColumnNames: "course_id", baseTableName: "course_student", baseTableSchemaName: "public", constraintName: "fk_e7ecu73rf342itmbja59nci37", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "course", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "Admin (generated)", id: "1442739448243-29") {
		addForeignKeyConstraint(baseColumnNames: "course_students_id", baseTableName: "course_student", baseTableSchemaName: "public", constraintName: "fk_sv1i8oao41apn7tid0t9625lm", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "course", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "Admin (generated)", id: "1442739448243-30") {
		addForeignKeyConstraint(baseColumnNames: "student_id", baseTableName: "course_student", baseTableSchemaName: "public", constraintName: "fk_4vvhda4qgl7pbr5pbhmht6ir", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "student", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "Admin (generated)", id: "1442739448243-31") {
		addForeignKeyConstraint(baseColumnNames: "course_id", baseTableName: "course_student2", baseTableSchemaName: "public", constraintName: "fk_h6nu5yqk82dkivlsd6tp41id", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "course2", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "Admin (generated)", id: "1442739448243-32") {
		addForeignKeyConstraint(baseColumnNames: "student_id", baseTableName: "course_student2", baseTableSchemaName: "public", constraintName: "fk_dw4yor8qomm1bej0nkihv5erc", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "student", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "Admin (generated)", id: "1442739448243-33") {
		addForeignKeyConstraint(baseColumnNames: "course_id", baseTableName: "event", baseTableSchemaName: "public", constraintName: "fk_rj3dgj7ybcm3ky31vro20f7rf", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "course", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "Admin (generated)", id: "1442739448243-34") {
		addForeignKeyConstraint(baseColumnNames: "subject_id", baseTableName: "event", baseTableSchemaName: "public", constraintName: "fk_n512drne35f8bx23o71yj298q", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "subject", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "Admin (generated)", id: "1442739448243-35") {
		addForeignKeyConstraint(baseColumnNames: "teacher_id", baseTableName: "event", baseTableSchemaName: "public", constraintName: "fk_m0spahucwfamp4ngf1t36wiwr", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "teacher", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "Admin (generated)", id: "1442739448243-36") {
		addForeignKeyConstraint(baseColumnNames: "user_id", baseTableName: "event", baseTableSchemaName: "public", constraintName: "fk_p84ruvsg7mfwb2x5p7iq3q103", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "myusers", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "Admin (generated)", id: "1442739448243-37") {
		addForeignKeyConstraint(baseColumnNames: "video_id", baseTableName: "event", baseTableSchemaName: "public", constraintName: "fk_fpflfp7l8koab0pwtyav2uyqs", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "video", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "Admin (generated)", id: "1442739448243-38") {
		addForeignKeyConstraint(baseColumnNames: "user_id", baseTableName: "student", baseTableSchemaName: "public", constraintName: "fk_bkix9btnoi1n917ll7bplkvg5", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "myusers", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "Admin (generated)", id: "1442739448243-39") {
		addForeignKeyConstraint(baseColumnNames: "course_id", baseTableName: "subject", baseTableSchemaName: "public", constraintName: "fk_sap4lbm82mrwih3wjsymu9l3w", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "course", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "Admin (generated)", id: "1442739448243-40") {
		addForeignKeyConstraint(baseColumnNames: "course_id", baseTableName: "subject2", baseTableSchemaName: "public", constraintName: "fk_h7t837kcuqd7aafdwe13crqla", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "course2", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "Admin (generated)", id: "1442739448243-41") {
		addForeignKeyConstraint(baseColumnNames: "subject_id", baseTableName: "subject_videos", baseTableSchemaName: "public", constraintName: "fk_5g32v6o7cadhe8sfm2wph4ckg", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "subject", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "Admin (generated)", id: "1442739448243-42") {
		addForeignKeyConstraint(baseColumnNames: "video_id", baseTableName: "subject_videos", baseTableSchemaName: "public", constraintName: "fk_ll9o3lul9d4pvxwmfsyp6sebx", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "video", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "Admin (generated)", id: "1442739448243-43") {
		addForeignKeyConstraint(baseColumnNames: "user_id", baseTableName: "teacher", baseTableSchemaName: "public", constraintName: "fk_i5wqs2ds2vpmfpbcdxi9m2jvr", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "myusers", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "Admin (generated)", id: "1442739448243-44") {
		addForeignKeyConstraint(baseColumnNames: "role_id", baseTableName: "user_role", baseTableSchemaName: "public", constraintName: "fk_it77eq964jhfqtu54081ebtio", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "role", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "Admin (generated)", id: "1442739448243-45") {
		addForeignKeyConstraint(baseColumnNames: "user_id", baseTableName: "user_role", baseTableSchemaName: "public", constraintName: "fk_apcc8lxk2xnug8377fatvbn04", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "myusers", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "Admin (generated)", id: "1442739448243-46") {
		addForeignKeyConstraint(baseColumnNames: "subject_id", baseTableName: "video2", baseTableSchemaName: "public", constraintName: "fk_ik23il7osoignfd1sxyvy8t7s", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "subject2", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "Admin (generated)", id: "1442739448243-47") {
		addForeignKeyConstraint(baseColumnNames: "comment_id", baseTableName: "video_comment", baseTableSchemaName: "public", constraintName: "fk_k15a7vs0bl11b7q3bc88x9nw3", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "comment", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "Admin (generated)", id: "1442739448243-48") {
		addForeignKeyConstraint(baseColumnNames: "video_comments_id", baseTableName: "video_comment", baseTableSchemaName: "public", constraintName: "fk_sif69tfsrdmn7vdnmpja18r9b", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "video", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	include file: 'add-video-belongs-to-subject.groovy'
	include file: 'add-field-info-to-answer-event.groovy'
	include file: 'add-node-statuses.groovy'
	include file: 'embed-semester-in-course.groovy'
	include file: 'add-course-uniqueness-constraint.groovy'
	include file: 'add-real-name-to-user.groovy'
	include file: 'change_video_description_type.groovy'
	include file: 'refactor-has-many-collections.groovy'
	include file: 'remove-old-username-index.groovy'
	include file: 'add-cas-support.groovy'
	include file: 'add-exercise-super-type.groovy'
	include file: 'add-similar-resources-to-exercises.groovy'
	include file: 'add-temporary-exercise.groovy'
	include file: 'add-written-exercises.groovy'

	include file: 'add-new-event-types-for-written-exercises.groovy'

	include file: 'add-thumbnail-to-written-exercise.groovy'

	include file: 'add-uuid-to-events.groovy'
}
