package dk.sdu.tekvideo

import org.apache.http.HttpStatus

import static dk.sdu.tekvideo.ServiceResult.*

/**
 * Provides services for course management. These services generally require a teacher to be authenticated. Teachers
 * are also generally only allowed to operator on their own courses.
 *
 * @author Dan Thrane
 */
class CourseManagementService {
    def userService

    /**
     * Finds courses belonging to the authenticated teacher of the given status. If there is no authenticated teacher,
     * then this method will fail.
     *
     * @param status    The node status to look for
     * @return A list of courses or failure
     */
    ServiceResult<List<Course>> getCourses(NodeStatus status) {
        def teacher = userService.authenticatedTeacher
        if (teacher) {
            ok Course.findAllByTeacherAndLocalStatus(teacher, status)
        } else {
            fail "teacherservice.no_teacher"
        }
    }

    /**
     * Finds subjects belonging to the authenticated teacher of the given status. If there is no authenticated teacher,
     * then this method will fail.
     *
     * @param status    The node status to look for
     * @return A list of subjects or failure
     */
    ServiceResult<List<Subject>> getSubjects(NodeStatus status, Course course) {
        if (canAccess(course)) {
            ok Subject.findAllByIdInListAndLocalStatus(CourseSubject.findSubjectIds(course), status)
        } else {
            fail "teacherservice.no_teacher"
        }
    }

    /**
     * Finds videos belonging to the authenticated teacher of the given status. If there is no authenticated teacher,
     * then this method will fail.
     *
     * @param status    The node status to look for
     * @return A list of videos or failure
     */
    ServiceResult<List<Video>> getVideos(NodeStatus status, Subject subject) {
        if (canAccess(subject.course)) {
            ok Video.findAllByIdInListAndLocalStatus(SubjectVideo.findAllVideoIds(subject), status)
        } else {
            fail "teacherservice.no_teacher"
        }
    }

    /**
     * Finds all courses that are not marked as TRASH for the authenticated teacher. If there is no authenticated
     * teacher, then this method will fail.
     *
     * @return A list of courses or failure
     */
    ServiceResult<List<Course>> getActiveCourses() {
        def teacher = userService.authenticatedTeacher
        if (teacher) {
            ok Course.findAllByTeacherAndLocalStatusNotEqual(teacher, NodeStatus.TRASH)
        } else {
            fail "teacherservice.no_teacher"
        }
    }

    /**
     * Creates, or edits an existing, subject belonging to a course. This is only possible to do if the authenticated
     * user is the teacher of the supplied course (and subject, if given), if not this method will fail.
     *
     * @param course     The course to add the subject to
     * @param command    The CRUD command for the subject
     * @return If successful the newly created/existing subject
     */
    ServiceResult<Subject> createOrEditSubject(Course course, SubjectCRUDCommand command) {
        new DomainServiceUpdater<SubjectCRUDCommand, Subject>(command) {
            ServiceResult init() {
                def teacher = userService.authenticatedTeacher
                if (teacher && canAccess(course)) {
                    if (!command.isEditing) course.addToSubjects(command.domain)
                    command.domain.localStatus = command.visible ? NodeStatus.VISIBLE : NodeStatus.INVISIBLE
                    ok null
                } else {
                    fail "teacherservice.not_allowed"
                }
            }
        }.dispatch()
    }

    /**
     * Creates, or edits an existing, course. This is only possible to do if the authenticated user is a teacher.
     * If not this method will fail.
     *
     * @param command    The CRUD command
     * @return If successful the newly created/existing course
     */
    ServiceResult<Course> createOrEditCourse(CourseCRUDCommand command) {
        new DomainServiceUpdater<CourseCRUDCommand, Course>(command) {
            ServiceResult<Void> init() {
                def teacher = userService.authenticatedTeacher
                if (teacher) {
                    command.domain.teacher = teacher
                    command.domain.localStatus = command.visible ? NodeStatus.VISIBLE : NodeStatus.INVISIBLE
                    ok null
                } else {
                    fail "teacherservice.not_allowed"
                }
            }

            ServiceResult<Void> postValidation() {
                if (command.isEditing && !canAccess(command.domain)) {
                    fail "teacherservice.not_allowed"
                } else {
                    ok null
                }
            }
        }.dispatch()
    }

    // TODO Might want to copy over comments and statistics (when moving)
    /**
     * Creates, or edits an existing, video. This is only possible to do if the authenticated user is a teacher of the
     * associated course, and if relevant the supplied video.
     *
     * This method also allows for an existing video to be moved to a new subject. In this case the video will be
     * re-created and added to the new subject. The video on the old subject will be removed from the database. This
     * will cause any comments associated with the video to be deleted!
     *
     * @param command    The CRUD command
     * @return If successful the newly created/existing video
     */
    ServiceResult<Video> createOrEditVideo(CreateOrUpdateVideoCommand command) {
        def teacher = userService.authenticatedTeacher
        if (teacher) {
            if (!canAccess(command.subject.course)) {
                fail "teacherservice.not_allowed"
            } else {
                def video = (command.isEditing) ? command.editing : new Video()
                if (command.isEditing && video.subject != command.subject) {
                    video.subject.removeFromVideos(video)
                    command.isEditing = false
                    video = new Video()
                }
                video.name = command.name
                video.youtubeId = command.youtubeId
                video.timelineJson = command.timelineJson
                video.description = command.description
                video.videoType = command.videoType
                video.subject = command.subject
                video.localStatus = command.visible ? NodeStatus.VISIBLE : NodeStatus.INVISIBLE
                if (video.validate()) {
                    if (command.isEditing) {
                        video.save()
                    } else {
                        command.subject.addToVideos(video).save(flush: true)
                    }
                    ok video
                } else {
                    fail "teacherservice.field_errors"
                }
            }
        } else {
            fail "teacherservice.not_allowed"
        }
    }

    /**
     * Updates the video list of a subject. This allows for videos to be moved up and down in the list, as well as
     * removing videos entirely from the list. Any video deleted will be marked as TRASH.
     *
     * @param command    The update command
     * @return The subject being edited
     */
    ServiceResult<Subject> updateVideos(UpdateVideosCommand command) {
        if (!command.validate()) {
            fail("teacherservice.invalid_request", false, [:], 400)
        } else {
            if (canAccess(command.subject.course)) {
                def diff = command.subject.videos.minus(command.order)
                diff.each {
                    it.localStatus = NodeStatus.TRASH
                    it.save()
                }
                command.subject.videos.clear()
                command.subject.videos.addAll(command.order)
                command.subject.videos.addAll(diff)
                command.subject.save()
                ok command.subject
            } else {
                fail("teacherservice.not_allowed", false, [:], 403)
            }
        }
    }

    /**
     * Updates the subject list of a course. This allows for subjects to be moved up and down in the list, as well as
     * removing subjects entirely from the list. Any subject deleted will be marked as TRASH.
     *
     * @param command    The update command
     * @return The course being edited
     */
    ServiceResult<Course> updateSubjects(UpdateSubjectsCommand command) {
        if (!command.validate()) {
            fail("teacherservice.invalid_request", false, [:], 400)
        } else {
            if (canAccess(command.course)) {
                def diff = command.course.subjects.minus(command.order)
                diff.each {
                    it.localStatus = NodeStatus.TRASH
                    it.save()
                }
                command.course.subjects.clear()
                command.course.subjects.addAll(command.order)
                command.course.subjects.addAll(diff)
                command.course.save()
                ok command.course
            } else {
                fail("teacherservice.not_allowed", false, [:], 403)
            }
        }
    }

    /**
     * Deletes a course, essentially marking it as TRASH.
     *
     * @param command    The delete command.
     * @return The course in question.
     */
    ServiceResult<Course> deleteCourse(DeleteCourseCommand command) {
        if (!command.validate()) {
            fail("teacherservice.invalid_request", false, [:], 400)
        } else {
            if (canAccess(command.course)) {
                command.course.localStatus = NodeStatus.TRASH
                ok command.course
            } else {
                fail("teacherservice.not_allowed", false, [:], 403)
            }
        }
    }

    /**
     * Creates a copy of the course given in the command with the new attributes as specified.
     *
     * @param command    The import command
     * @return The new course
     */
    ServiceResult<Course> importCourse(ImportCourseCommand command) {
        def teacher = userService.authenticatedTeacher
        if (teacher == null) {
            fail("teacherservice.not_a_teacher", false, [:], 401)
        } else {
            if (command.validate()) {
                def course = new Course([
                        name       : command.newCourseName,
                        fullName   : command.newCourseFullName,
                        description: command.newDescription,
                        year       : command.newSemester,
                        spring     : command.newSemesterSpring,
                        teacher    : teacher,
                        localStatus: command.visible ? NodeStatus.VISIBLE : NodeStatus.INVISIBLE
                ]).save(flush: true)
                command.course.visibleSubjects.forEach { copySubjectToCourse(it, course) }

                ok course
            } else {
                fail("teacherservice.invalid_request", false, [command: command], 400)
            }
        }
    }

    /**
     * Checks if the authenticated user is the owner of the given course.
     *
     * @param course    The course
     * @return true if the authenticated user owns the course
     */
    boolean canAccess(Course course) {
        return course.teacher == userService.authenticatedTeacher
    }

    /**
     * Changes the status of a single course. This can only be done if the teacher owns the associated course.
     *
     * @param course    The node to change status on
     * @param status    The status to update to
     * @return failure if the authenticated user is not the teacher of the course, otherwise OK
     */
    ServiceResult<Void> changeCourseStatus(Course course, NodeStatus status) {
        def teacher = userService.authenticatedTeacher
        if (teacher && course.teacher == teacher) {
            if (status != null) {
                course.localStatus = status
                course.save()
                ok()
            } else {
                fail message: "Ugyldig forspørgsel", suggestedHttpStatus: HttpStatus.SC_BAD_REQUEST
            }
        } else {
            fail message: "Ugyldigt kursus", suggestedHttpStatus: HttpStatus.SC_NOT_FOUND
        }
    }

    /**
     * Changes the status of a single subject. This can only be done if the teacher owns the associated course.
     *
     * @param course    The node to change status on
     * @param status    The status to update to
     * @return failure if the authenticated user is not the teacher of the course, otherwise OK
     */
    ServiceResult<Void> changeSubjectStatus(Subject subject, NodeStatus status) {
        if (canAccess(subject.course)) {
            if (status != null) {
                subject.localStatus = status
                subject.save()
                ok()
            } else {
                fail message: "Ugyldig forspørgsel", suggestedHttpStatus: HttpStatus.SC_BAD_REQUEST
            }
        } else {
            fail message: "Ugyldigt kursus", suggestedHttpStatus: HttpStatus.SC_NOT_FOUND
        }
    }

    /**
     * Changes the status of a single video. This can only be done if the teacher owns the associated course.
     *
     * @param course    The node to change status on
     * @param status    The status to update to
     * @return failure if the authenticated user is not the teacher of the course, otherwise OK
     */
    ServiceResult<Void> changeVideoStatus(Video video, NodeStatus status) {
        if (canAccess(video.subject.course)) {
            if (status != null) {
                video.localStatus = status
                video.save()
                ok()
            } else {
                fail message: "Ugyldig forspørgsel", suggestedHttpStatus: HttpStatus.SC_BAD_REQUEST
            }
        } else {
            fail message: "Ugyldigt kursus", suggestedHttpStatus: HttpStatus.SC_NOT_FOUND
        }
    }

    /**
     * Utility method for copying a subject to a course.
     *
     * @param subject    The subject to copy
     * @param course     The destination course
     */
    private void copySubjectToCourse(Subject subject, Course course) {
        if (subject == null) return
        def newSubject = new Subject([
                name       : subject.name,
                description: subject.description,
                course     : course
        ]).save(flush: true)
        subject.visibleVideos.forEach { copyVideoToSubject(it, newSubject) }
    }

    /**
     * Utility method for copying a video to a subject.
     *
     * @param video      The video
     * @param subject    The destination subject
     */
    private void copyVideoToSubject(Video video, Subject subject) {
        if (video == null) return
        new Video([
                name        : video.name,
                youtubeId   : video.youtubeId,
                timelineJson: video.timelineJson,
                description : video.description,
                videoTyupe  : video.videoType,
                subject     : subject
        ]).save(flush: true)
    }

}
