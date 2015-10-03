<%@ page import="dk.sdu.tekvideo.FaIcon; dk.danthrane.twbs.Icon; java.util.stream.Collectors; grails.converters.JSON;" contentType="text/html;charset=UTF-8" %>
<%@ page import="dk.danthrane.twbs.ButtonStyle; dk.danthrane.twbs.ButtonSize" %>
<html>
<head>
    <meta name="layout" content="main_fluid">
    <script src="//cdnjs.cloudflare.com/ajax/libs/Chart.js/1.0.2/Chart.min.js"></script>
</head>

<body>

<twbs:pageHeader>
    <h3>Video statistik</h3>
</twbs:pageHeader>

<span><b>Navn:</b> ${video.name}<br/></span>
<span><b>Oprettet:</b> <date:oldDtFormatter date="${video.dateCreated}"/><br/></span>

<twbs:pageHeader>
    <h4>Visninger <small>Opsummering</small></h4>
</twbs:pageHeader>

<twbs:row>
    <twbs:column sm="4">
        <b><fa:icon icon="${FaIcon.BAR_CHART}"/> Antal visninger:</b> <span>${viewBreakdown.visits}</span>
    </twbs:column>
    <twbs:column sm="4">
        <b><fa:icon icon="${FaIcon.GRADUATION_CAP}"/> Heraf registeret:</b> <span>${viewBreakdown.studentVisits}</span>
    </twbs:column>
    <twbs:column sm="4">
        <b><fa:icon icon="${FaIcon.USER_SECRET}"/> Heraf gæster:</b> <span>${viewBreakdown.guestVisits}</span>
    </twbs:column>
</twbs:row>

<twbs:pageHeader>
    <h4>Svar <small>Opsummering</small></h4>
</twbs:pageHeader>

<twbs:table hover="true" responsive="true" striped="true">
    <thead>
    <twbs:th>Spørgsmål</twbs:th>
    <twbs:th>Svar</twbs:th>
    <twbs:th>Antal</twbs:th>
    <twbs:th>Korrekt?</twbs:th>
    </thead>
    <tbody>
    <g:each in="${answerSummary}" var="summary">
        <twbs:tr>
            <twbs:td>
                <span class="summary-question" data-id="${summary.subject} ${summary.question} ${summary.field}">
                    ${summary.subject} ${summary.question} ${summary.field}
                </span>
            </twbs:td>
            <twbs:td>${summary.answer}</twbs:td>
            <twbs:td>${summary.frequency}</twbs:td>
            <twbs:td>
                <fa:icon icon="${summary.correct ? FaIcon.CHECK : FaIcon.CLOSE}"/>
            </twbs:td>
        </twbs:tr>
    </g:each>
    </tbody>
</twbs:table>

<twbs:pageHeader>
    <h4>Visninger <small>Over tid</small></h4>
</twbs:pageHeader>
<canvas id="chart" style="width: 100%; height: 500px"></canvas>
<twbs:buttonToolbar>
    <twbs:buttonGroup justified="true">
        <twbs:button style="${ButtonStyle.PRIMARY}" class="show-x-day" data-days="1">
            1 dag
        </twbs:button>
        <twbs:button style="${ButtonStyle.PRIMARY}" class="show-x-day" data-days="7">
            7 dage
        </twbs:button>
        <twbs:button style="${ButtonStyle.PRIMARY}" class="show-x-day" data-days="14">
            14 dage
        </twbs:button>
        <twbs:button style="${ButtonStyle.PRIMARY}" class="show-x-day" data-days="30">
            30 dage
        </twbs:button>
        <twbs:button style="${ButtonStyle.PRIMARY}" class="show-x-day" data-days="90">
            90 dage
        </twbs:button>
        <twbs:button style="${ButtonStyle.PRIMARY}" class="show-x-day" data-days="180">
            180 dage
        </twbs:button>
        <twbs:button style="${ButtonStyle.PRIMARY}" class="show-x-day" data-days="365">
            365 dage
        </twbs:button>
    </twbs:buttonGroup>
</twbs:buttonToolbar>

<twbs:pageHeader>
    <h4>Visninger <small>Blandt tilmeldte studerende</small></h4>
</twbs:pageHeader>
<twbs:table hover="true" responsive="true" striped="true">
    <thead>
    <twbs:th>Brugernavn</twbs:th>
    <twbs:th>Tidspunkt</twbs:th>
    </thead>
    <tbody>
    <g:each in="${viewsAmongStudents}" var="summary">
        <twbs:tr>
            <twbs:td>${summary.username}</twbs:td>
            <twbs:td>
                <date:utilDateFormatter time="${new Date(summary.timestamp as long)}"/>
            </twbs:td>
        </twbs:tr>
    </g:each>
    </tbody>
</twbs:table>

<g:content key="sidebar-right">

</g:content>


<script type="text/javascript">
    var timeline = ${raw(video.timelineJson)};

    $(".summary-question").each(function() {
        var summary = $(this);
        var ids = summary.data("id").split(" ").map(function (e) { return parseInt(e); });
        summary.text(timeline[ids[0]].title + " - " + timeline[ids[0]].questions[ids[1]].title +
                " - Felt " + ids[2] + " (" + timeline[ids[0]].questions[ids[1]].fields[ids[2]].name + ")");
    });

    var chart = null;
    $(".show-x-day").click(function (e) {
        e.preventDefault();
        $.get("${createLink(action: "videoViewingChart", id: video.id)}?period=" + $(this).attr("data-days"),
                function (data) {
                    if (data.success) {
                        console.log(data);
                        createChart(data.result.labels, data.result.data);
                    }
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