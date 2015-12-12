var ViewsPage = (function () {
    function ViewsPage(app) {
        this.app = app;
        this.viewsChart = null;
    }

    ViewsPage.prototype.onSelect = function (root, period) {
        var self = this;
        console.log(root);
        console.log(period);
        this.app.displaySpinner();
        $("#error-message").removeClass("hide").hide();
        var chartPromise = $.getJSON(baseUrl + "dashboard/visits?identifier=" + root + "&period=" + period,
            function (data) {
                var chartData = {
                    labels: data.labels,
                    datasets: [
                        {
                            label: "Visninger",
                            fillColor: "rgba(220,220,220,0.2)",
                            strokeColor: "rgba(220,220,220,1)",
                            pointColor: "rgba(220,220,220,1)",
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