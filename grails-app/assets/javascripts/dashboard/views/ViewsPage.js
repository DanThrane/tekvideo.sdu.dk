var ViewsPage = (function () {
    var cumulativeCheckbox;

    function ViewsPage(app) {
        this.app = app;
        this.viewsChart = null;

        cumulativeCheckbox = $("#cumulative");
        cumulativeCheckbox.click(function() {
            console.log("Refreshing");
            app.refresh();
        });
    }

    ViewsPage.prototype.onSelect = function (root, periodFrom, periodTo) {
        var self = this;
        this.app.displaySpinner();
        $("#error-message").removeClass("hide").hide();
        var cumulative = cumulativeCheckbox.is(":checked");
        var chartPromise = $.getJSON(baseUrl + "dashboard/visits?identifier=" + root + "&periodFrom=" + periodFrom
            + "&periodTo=" + periodTo + "&cumulative=" + cumulative,
            function (data) {
                var config = {
                    type: "line",
                    data: {
                        labels: data.labels,
                        datasets: [{
                            label: "Visninger",
                            data: data.data,
                            fill: true,
                            borderDash: [1]
                        }]
                    },
                    options: {
                        responsive: true
                    }
                };

                var ctx = $("#my-chart").get(0).getContext("2d");
                if (self.viewsChart !== null) {
                    self.viewsChart.destroy();
                }
                self.viewsChart = new Chart(ctx, config);

                if (!data.data) {
                    $("#views-no-data").removeClass("hide");
                } else {
                    $("#views-no-data").addClass("hide");
                }
            });

        var popularVideosPromise = $.getJSON(baseUrl + "dashboard/popularVideos?identifier=" + root + "&periodFrom=" +
            periodFrom + "&periodTo=" + periodTo, function (data) {
            console.log(data);
            var $popular = $("#popular-video-container");
            $popular.empty();
            for (var i = 0; i < data.length; i++) {
                var stat = data[i];
                var rawTemplate = $("#popular-video-template").html();
                var template = $(rawTemplate.format(
                    i + 1,
                    stat.videoName,
                    stat.subjectName,
                    stat.courseName,
                    stat.visits,
                    stat.answerCount,
                    stat.correctAnswers
                ));
                $popular.append(template);
            }
        });

        $.when(chartPromise, popularVideosPromise).always(function () {
            self.app.removeSpinner();
        }).fail(function () {
            if (self.viewsChart !== null) {
                self.viewsChart.destroy();
            }
            $("#error-message").fadeIn();
        });
    };
    return ViewsPage;
}());