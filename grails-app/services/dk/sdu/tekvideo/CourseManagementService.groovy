package dk.sdu.tekvideo

import org.apache.http.HttpStatus

import static dk.sdu.tekvideo.ServiceResult.*

/**
 * @author Dan Thrane
 */
class CourseManagementService {
    def teachingService

    ServiceResult<List<Course>> getCourses(NodeStatus status) {
        def teacher = teachingService.authenticatedTeacher
        if (teacher) {
            ok Course.findAllByTeacherAndLocalStatus(teacher, status)
        } else {
            fail "teacherservice.no_teacher"
        }
    }

    ServiceResult<List<Subject>> getSubjects(NodeStatus status, Course course) {
        if (canAccess(course)) {
            ok Subject.findAllByCourseAndLocalStatus(course, status)
        } else {
            fail "teacherservice.no_teacher"
        }
    }

    ServiceResult<List<Video>> getVideos(NodeStatus status, Subject subject) {
        if (canAccess(subject.course)) {
            ok Video.findAllBySubjectAndLocalStatus(subject, status)
        } else {
            fail "teacherservice.no_teacher"
        }
    }

    ServiceResult<List<Course>> getActiveCourses() {
        def teacher = teachingService.authenticatedTeacher
        if (teacher) {
            ok Course.findAllByTeacherAndLocalStatusNotEqual(teacher, NodeStatus.TRASH)
        } else {
            fail "teacherservice.no_teacher"
        }
    }

    ServiceResult<Subject> createOrEditSubject(Course course, SubjectCRUDCommand command) {
        new DomainServiceUpdater<SubjectCRUDCommand, Subject>(command) {
            ServiceResult init() {
                def teacher = teachingService.authenticatedTeacher
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

    ServiceResult<Course> createOrEditCourse(CourseCRUDCommand command) {
        new DomainServiceUpdater<CourseCRUDCommand, Course>(command) {
            ServiceResult<Void> init() {
                def teacher = teachingService.authenticatedTeacher
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

    ServiceResult<Video> createOrEditVideo(CreateVideoCommand command) {
        def teacher = teachingService.authenticatedTeacher
        if (teacher) {
            if (!command.isEditing && !canAccess(command.subject.course)) {
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

    ServiceResult<Course> importCourse(ImportCourseCommand command) {
        def teacher = teachingService.authenticatedTeacher
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

    private void copySubjectToCourse(Subject subject, Course course) {
        if (subject == null) return
        def newSubject = new Subject([
                name       : subject.name,
                description: subject.description,
                course     : course
        ]).save(flush: true)
        subject.visibleVideos.forEach { copyVideoToSubject(it, newSubject) }
    }

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

    boolean canAccess(Course course) {
        return course.teacher == teachingService.authenticatedTeacher
    }

    ServiceResult<Void> changeCourseStatus(Course course, NodeStatus status) {
        def teacher = teachingService.authenticatedTeacher
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
}
