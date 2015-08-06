package dk.sdu.tekvideo

import static dk.sdu.tekvideo.ServiceResult.*

/**
 * @author Dan Thrane
 */
class TeacherService {
    def springSecurityService

    Teacher getAuthenticatedTeacher() {
        def user = (User) springSecurityService.currentUser
        return Teacher.findByUser(user)
    }

    ServiceResult<List<Course>> getActiveCourses() {
        def teacher = getAuthenticatedTeacher()
        if (teacher) {
            ok Course.findAllByTeacher(teacher)
        } else {
            fail "teacherservice.no_teacher"
        }
    }

    ServiceResult<Subject> createOrEditSubject(Course course, SubjectCRUDCommand command) {
        new DomainServiceUpdater<SubjectCRUDCommand, Subject>(command) {
            ServiceResult init() {
                def teacher = getAuthenticatedTeacher()
                if (teacher && canAccess(course)) {
                    if (!command.isEditing) course.addToSubjects(command.domain)
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
                def teacher = getAuthenticatedTeacher()
                if (teacher) {
                    command.domain.teacher = teacher
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
        def teacher = getAuthenticatedTeacher()
        if (teacher) {
            if (!command.isEditing && !canAccess(command.subject.course)) {
                fail "teacherservice.not_allowed"
            } else {
                def video = (command.isEditing) ? command.editing : new Video()
                video.name = command.name
                video.youtubeId = command.youtubeId
                video.timelineJson = command.timelineJson
                video.description = command.description
                video.videoType = command.videoType
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
                command.subject.videos.clear()
                command.subject.videos.addAll(command.order)
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
                // As opposed to the videos, this will not cause a delete on the underlying object, since it
                // can be owned by multiple objects.

                // TODO Find the proper way to do this. For large collections this can be expensive
                // Might potentially want to create a CourseSubject domain to deal with this, as "hasMany" seems to
                // have many limitations.

                List<Subject> previous = new ArrayList(command.course.subjects)
                command.course.subjects.clear()
                command.course.subjects.addAll(command.order)
                command.course.save()
                previous.each {
                    if (!command.course.subjects.contains(it)) {
                        command.course.removeFromSubjects(it)
                    }
                }
                command.course.save(flush: true)
                ok command.course
            } else {
                fail("teacherservice.not_allowed", false, [:], 403)
            }
        }
    }

    boolean canAccess(Course course) {
        return course.teacher == getAuthenticatedTeacher()
    }
}
