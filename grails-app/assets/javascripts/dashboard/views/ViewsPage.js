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

    ViewsPage.prototype.onSelect = function (root, period) {
        var self = this;
        this.app.displaySpinner();
        $("#error-message").removeClass("hide").hide();
        var cumulative = cumulativeCheckbox.is(":checked");
        var chartPromise = $.getJSON(baseUrl + "dashboard/visits?identifier=" + root + "&period=" + period +
            "&cumulative=" + cumulative,
            function (data) {
                var chartData = {
                    labels: data.labels,
                    datasets: [
                        {
                            label: "Visninger",
                            fillColor: "rgba(100,100,100,0.2)",
                            strokeColor: "rgba(100, 100, 100, 1)",
                            pointColor: "rgba(100, 100, 100, 1)",
                            pointStrokeColor: "#fff",
                            pointHighlightFill: "#fff",
                            pointHighlightStroke: "rgba(220,220,220,1)",
                            data: data.data
                        }
                    ]
                };
                var ctx = $("#my-chart").get(0).getContext("2d");
                if (self.viewsChart !== null) {
                    self.viewsChart.destroy();
                }
                self.viewsChart = new Chart(ctx).Line(chartData);

                if (!data.data) {
                    $("#views-no-data").removeClass("hide");
                } else {
                    $("#views-no-data").addClass("hide");
                }
            });

        var popularVideosPromise = $.getJSON(baseUrl + "dashboard/popularVideos?identifier=" + root + "&period=" +
            period, function (data) {
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