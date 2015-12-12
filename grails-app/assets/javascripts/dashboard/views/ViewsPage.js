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
        $.getJSON(baseUrl + "dashboard/visits?identifier=" + root + "&period=" + period, function(data) {
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
            self.app.removeSpinner();
        }).fail(function() {
            if (self.viewsChart !== null) {
                self.viewsChart.destroy();
            }
            self.app.removeSpinner();
            $("#error-message").fadeIn();
        });
    };
    return ViewsPage;
}());