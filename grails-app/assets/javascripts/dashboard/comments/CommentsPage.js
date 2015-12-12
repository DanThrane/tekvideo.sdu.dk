//= require NotificationCommentComponent

var CommentsPage = (function () {
    function CommentsPage(app) {
        this.app = app;
    }

    CommentsPage.prototype.onSelect = function (root, period) {
        var self = this;
        self.app.displaySpinner();
        $("#error-message").removeClass("hide").hide();

        $.getJSON(baseUrl + "dashboard/comments?identifier=" + root + "&period=" + period, function (data) {
            console.log(data);
            self.app.removeSpinner();
            var $comments = $("#comment-container");
            $comments.empty();
            var rawTemplate = $("#comment-template").html();

            for (var i = 0; i < data.length; i++) {
                var comment = data[i];
                var template = $(rawTemplate.format(
                    comment.username,
                    comment.videoTitle,
                    comment.dateCreated,
                    comment.comment
                ));
                $comments.append(template);
                var component = new NotificationCommentComponent(template);
                component.init();
            }
        });
    };
    return CommentsPage;
}());
