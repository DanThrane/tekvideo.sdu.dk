var StudentsPage = (function () {
    function StudentsPage(app) {
        this.app = app;
    }

    StudentsPage.prototype.onSelect = function (root, period) {
        var self = this;
        self.app.displaySpinner();
        $.getJSON(baseUrl + "dashboard/studentActivity?identifier=" + root + "&period=" + period, function (data) {
            var activity = data.result;
            var $body = $("#students").find("tbody");
            $body.empty();

            var rowTemplate = $("#student-row-template").html();
            for (var i = 0; i < activity.length; i++) {
                var studentActivity = activity[i];
                var row = $(rowTemplate.format(
                    studentActivity.username,
                    studentActivity.uniqueViews,
                    studentActivity.answerCount,
                    studentActivity.correctAnswers,
                    studentActivity.answerCount - studentActivity.correctAnswers
                ));
                $body.append(row);
            }
            if (!activity.length) {
                $("#students-no-data").removeClass("hide");
            } else {
                $("#students-no-data").addClass("hide");
            }
        }).always(function () {
            self.app.removeSpinner();
        });
    };
    return StudentsPage;
}());