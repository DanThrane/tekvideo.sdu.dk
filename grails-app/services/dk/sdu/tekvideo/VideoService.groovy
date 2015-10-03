package dk.sdu.tekvideo

import grails.plugin.springsecurity.SpringSecurityUtils

import static dk.sdu.tekvideo.ServiceResult.*

class VideoService {
    def springSecurityService
    def videoStatisticsService

    ServiceResult<Comment> createComment(CreateVideoCommentCommand command) {
        User user = springSecurityService.currentUser as User
        if (!command.id) {
            fail "Ukendt video"
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

    ServiceResult<Void> deleteComment(Video video, Comment comment) {
        if (SpringSecurityUtils.ifAllGranted("ROLE_TEACHER")) {
            if (!video) {
                fail "Ukendt video"
            } else if (!comment) {
                fail "Ukendt kommentar"
            } else {
                if (!video.removeFromComments(comment)) {
                    fail("Kunne ikke fjerne kommentar", true)
                } else {
                    ok null
                }
            }
        } else {
            fail "Du har ikke rettigheder til at slette denne kommentar!"
        }
    }

    ServiceResult<List<VideoBreakdown>> findVideoBreakdown(List<Video> videos) {
        ok videos.collect {
            new VideoBreakdown(
                    video: it,
                    commentCount: it.comments.size(),
                    viewCount: videoStatisticsService.retrieveViewBreakdown(it).result.visits
            )
        }
    }
}
