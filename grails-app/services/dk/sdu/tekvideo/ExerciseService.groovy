package dk.sdu.tekvideo

import grails.plugin.springsecurity.SpringSecurityUtils
import org.hibernate.SessionFactory

import static dk.sdu.tekvideo.ServiceResult.fail
import static dk.sdu.tekvideo.ServiceResult.ok

class ExerciseService implements NodeInformation<Exercise> {
    def urlMappingService
    def springSecurityService
    def videoService
    def subjectService

    ServiceResult<Comment> createComment(CreateCommentCommand command) {
        User user = springSecurityService.currentUser as User
        if (!command.id) {
            fail "Ukendt opgave"
        } else if (!user) {
            fail "Ukendt bruger"
        } else {
            def comment = new Comment(contents: command.comment, user: user)
            if (!comment.validate()) {
                fail "Kommentaren er ikke gyldig"
            } else {
                if (command.id.addToComments(comment)) {
                    ok comment
                } else {
                    fail "Kunne ikke gemme kommentar"
                }
            }
        }
    }

    ServiceResult<Void> deleteComment(Exercise exercise, Comment comment) {
        if (SpringSecurityUtils.ifAllGranted("ROLE_TEACHER")) {
            if (!exercise) {
                fail "Ukendt opgave"
            } else if (!comment) {
                fail "Ukendt kommentar"
            } else {
                if (!exercise.removeFromComments(comment)) {
                    fail("Kunne ikke fjerne kommentar", true)
                } else {
                    ok null
                }
            }
        } else {
            fail "Du har ikke rettigheder til at slette denne kommentar!"
        }
    }

    @Override
    String getThumbnail(Exercise exercise) {
        // Should delegate to service if we have a lot more exercise types
        if (exercise instanceof Video) return videoService.getThumbnail(exercise)
        if (exercise instanceof WrittenExerciseGroup) return exercise.thumbnailUrl
        throw new IllegalArgumentException("Unknown exercise type: ${exercise.class.name}")
    }

    @Override
    List<NodeBrowserCrumbs> getBreadcrumbs(Exercise exercise) {
        def subject = exercise.subject
        def course = subject.course
        def teacher = course.teacher

        return [
                new NodeBrowserCrumbs(
                        teacher.toString(),
                        urlMappingService.generateLinkToTeacher(teacher)
                ),
                new NodeBrowserCrumbs(
                        course.name,
                        urlMappingService.generateLinkToCourse(course)
                ),
                new NodeBrowserCrumbs(
                        subject.name,
                        urlMappingService.generateLinkToSubject(subject)
                )
        ]
    }

    @Override
    NodeBrowserInformation getInformationForBrowser(Exercise exercise, String thumbnail, boolean resolveThumbnail,
                                                    boolean addBreadcrumbs) {
        def breadcrumbs = addBreadcrumbs ? subjectService.getBreadcrumbs(exercise.subject) : []

        String actualThumbnail = !resolveThumbnail ? thumbnail : getThumbnail(exercise)
        return new NodeBrowserInformation(
                exercise.name,
                exercise.description,
                actualThumbnail,
                urlMappingService.generateLinkToExercise(exercise),
                breadcrumbs
        )
    }
}
