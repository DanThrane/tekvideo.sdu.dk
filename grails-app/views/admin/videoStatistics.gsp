<%@ page import="java.util.stream.Collectors; grails.converters.JSON" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="admin">
    <title>Test Title</title>
</head>

<body>

<twbs:row>
    <twbs:column cols="8">
        <h1>Video statistik</h1>

        <section id="info" class="group">
            <h3>Video infomation</h3>
            <twbs:row>
                <twbs:column cols="4">
                    <img src="http://img.youtube.com/vi/${video.youtubeId}/hqdefault.jpg" class="img-responsive" alt="Video thumbnail">
                </twbs:column>
                <twbs:column cols="8">
                    <span><b>Navn:</b> ${video.name}<br/></span>
                    <span><b>Oprettet:</b> <date:oldDtFormatter date="${video.dateCreated}"/><br/></span>
                    <span>
                        <b>Tilhører:</b> ${video.subjects.size()} emne. [
                        <g:each in="${subjects}" var="subject" status="i">
                            <g:if test="${i > 0}">, </g:if>
                            ${subject} (${subject.course})
                        </g:each>
                        ]
                    </span><br/>
                    %{-- TODO Video længde --}%
                </twbs:column>
            </twbs:row>
        </section>
        <section id="visninger" class="group">
            <h3>Visninger</h3>
            <section id="visninger-over-tid" class="subgroup">
                <h4>Over tid</h4>
                <canvas id="chart" style="width: 100%; height: 500px"></canvas>
                <twbs:linkButton btnstyle="primary" class="show-x-day" attributes="${["data-days": "1"]}">
                    1 dag
                </twbs:linkButton>
                <twbs:linkButton btnstyle="primary" class="show-x-day" attributes="${["data-days": "7"]}">
                    7 dage
                </twbs:linkButton>
                <twbs:linkButton btnstyle="primary" class="show-x-day" attributes="${["data-days": "14"]}">
                    14 dage
                </twbs:linkButton>
                <twbs:linkButton btnstyle="primary" class="show-x-day" attributes="${["data-days": "30"]}">
                    30 dage
                </twbs:linkButton>
                <twbs:linkButton btnstyle="primary" class="show-x-day" attributes="${["data-days": "90"]}">
                    90 dage
                </twbs:linkButton>
                <twbs:linkButton btnstyle="primary" class="show-x-day" attributes="${["data-days": "180"]}">
                    180 dage
                </twbs:linkButton>
                <twbs:linkButton btnstyle="primary" class="show-x-day" attributes="${["data-days": "365"]}">
                    365 dage
                </twbs:linkButton>
            </section>
        </section>
    </twbs:column>
    <twbs:column cols="4">
        <nav class="bs-docs-sidebar">
            <ul id="sidebar" class="nav nav-stacked fixed">
                <li><a href="#info">Video info</a></li>
                <li>
                    <a href="#visninger">Visninger</a>
                    <ul class="nav nav-stacked">
                        <li><a href="#visninger-over-tid">Over tid</a></li>
                    </ul>
                </li>
            </ul>
        </nav>
    </twbs:column>
</twbs:row>


<script type="text/javascript">
    var chart = null;
    $('body').scrollspy({
        target: '.bs-docs-sidebar',
        offset: 40
    });

    $(".show-x-day").click(function(e) {
        e.preventDefault();
        $.get("${createLink(action: "videoViewingChart", id: video.id)}?period=" + $(this).attr("data-days"),
                function(data) {
                    console.log(data);
                    createChart(data.labels, data.data);
                });
    });

    function createChart(labels, data) {
        if (chart != null) chart.destroy();
        var ctx = $("#chart").get(0).getContext("2d");
        chart = new Chart(ctx).Line({
            labels: labels,
            datasets: [
                {
                    label: "Visninger",
                    fillColor: "rgba(220,220,220,0.2)",
                    strokeColor: "rgba(220,220,220,1)",
                    pointColor: "rgba(220,220,220,1)",
                    pointStrokeColor: "#fff",
                    pointHighlightFill: "#fff",
                    pointHighlightStroke: "rgba(220,220,220,1)",
                    data: data
                }
            ]
        });
    }
    createChart(${raw((statistics.labels as JSON).toString())}, ${raw(statistics.data as JSON).toString()});
</script>

</body>
</html>