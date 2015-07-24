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

    ServiceResult<Video> createVideo(CreateVideoCommand command) {
        def teacher = getAuthenticatedTeacher()
        if (teacher && canAccess(command.subject.course)) {
            def video = new Video(name: command.name, youtubeId: command.youtubeId, timelineJson: command.timelineJson)
            if (video.validate()) {
                command.subject.addToVideos(video).save(flush: true)
                ok video
            } else {
                fail "teacherservice.field_errors"
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

    boolean canAccess(Course course) {
        return course.teacher == getAuthenticatedTeacher()
    }
}
