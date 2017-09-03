<%@ page import="grails.converters.JSON" %>
<g:applyLayout name="stats_layout">
    <html>
    <head>
        <title>Visninger for ${node.name}</title>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.2.1/Chart.bundle.min.js"></script>
    </head>

    <body>
    <canvas id="chart"
            style="width: 100%; height: 500px"
            data-initial="${views.encodeAsJSON().encodeAsHTML()}"
            data-endpoint="${endpoint}">
    </canvas>

    <twbs:checkbox name="cumulative-check" labelText="Cumulative"/>
    <twbs:checkbox name="weekly-check" labelText="Ugentlig data"/>

    <twbs:button class="reload-btn">Reload data!</twbs:button>
    <twbs:button class="download-btn">Download data!</twbs:button>

    <a id="downloadAnchorElem" style="display:none"></a>

    <g:content key="layout-script">
        <script>
            $(function () {
                var selector = "#chart";
                var dataLabel = "Visninger";

                var chart = null;
                var currentData = null;
                var $target = $(selector);
                var initial = $target.data("initial");
                var endpoint = $target.data("endpoint");
                var $cumulative = $("#cumulative-check");
                var $weekly = $("#weekly-check");
                renderWithData(initial.labels, initial.data);

                $(".reload-btn").click(function () {
                    initial.data = initial.data.map(function (e) {
                        return e * 2;
                    });
                    console.log(initial.data);
                    renderWithData(initial.labels, initial.data);
                });

                $(".download-btn").click(function () {
                    var cumulative = $cumulative.is(":checked");
                    var weekly = $weekly.is(":checked");
                    var fileName = "${node.name}-views";
                    if (cumulative) fileName += "-cumulative";
                    if (weekly) fileName += "-weekly";
                    fileName += ".json";
                    console.log(currentData, fileName);
                    saveData(currentData, fileName);
                });

                $cumulative.click(refresh);
                $weekly.click(refresh);

                function refresh() {
                    var cumulative = $cumulative.is(":checked");
                    var weekly = $weekly.is(":checked");

                    $.getJSON(endpoint + "?weekly=" + weekly + "&cumulative=" + cumulative, function (result) {
                        var data = result.result;
                        renderWithData(data.labels, data.data);
                    });
                }

                function renderWithData(labels, data) {
                    currentData = {labels: labels, data: data};
                    if (chart !== null) {
                        chart.destroy();
                    }
                    var ctx = $target.get(0).getContext("2d");
                    var config = {
                        type: "line",
                        data: {
                            labels: labels,
                            datasets: [{
                                label: dataLabel,
                                data: data,
                                fill: true,
                                borderDash: [1]
                            }]
                        },
                        options: {
                            responsive: true
                        }
                    };

                    chart = new Chart(ctx, config);
                }

                /**
                 * function to save JSON to file from browser
                 * adapted from http://bgrins.github.io/devtools-snippets/#console-save
                 * @param {Object} data -- json object to save
                 * @param {String} file -- file name to save to
                 */
                function saveData(data, filename) {

                    if (!data) {
                        console.error('No data');
                        return;
                    }

                    if (!filename) filename = 'console.json';

                    if (typeof data === "object") {
                        data = JSON.stringify(data, undefined, 4)
                    }

                    var blob = new Blob([data], {type: 'text/json'}),
                        e = document.createEvent('MouseEvents'),
                        a = document.createElement('a');

                    a.download = filename;
                    a.href = window.URL.createObjectURL(blob);
                    a.dataset.downloadurl = ['text/json', a.download, a.href].join(':');
                    e.initMouseEvent('click', true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);
                    a.dispatchEvent(e);
                }
            });
        </script>
    </g:content>
    </body>
    </html>
</g:applyLayout>
