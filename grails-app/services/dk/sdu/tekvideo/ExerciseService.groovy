package dk.sdu.tekvideo

import grails.plugin.springsecurity.SpringSecurityUtils

import static dk.sdu.tekvideo.ServiceResult.fail
import static dk.sdu.tekvideo.ServiceResult.ok

class ExerciseService {
    def userService
    def springSecurityService

    boolean canAccess(Exercise video) {
        def status = video?.status

        video != null &&
                (status == NodeStatus.VISIBLE ||
                        (status == NodeStatus.INVISIBLE &&
                                userService.authenticatedTeacher == video.subject.course.teacher))
    }

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


}
