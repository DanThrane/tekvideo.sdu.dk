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
                        <g:each in="${video.subjects.stream().limit(3).collect(Collectors.toList())}" var="subject" status="i">
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
            <canvas id="chart" style="width: 100%; height: 500px"></canvas>
        </section>
        <section id="svar" class="group">
            <h3>Svar</h3>

        </section>
    </twbs:column>
    <twbs:column cols="4">
        <nav class="bs-docs-sidebar">
            <ul id="sidebar" class="nav nav-stacked fixed">
                <li><a href="#info">Video info</a></li>
                <li><a href="#visninger">Visninger</a></li>
                <li><a href="#svar">Svar</a></li>
            </ul>
        </nav>
    </twbs:column>
</twbs:row>


<script type="text/javascript">
    $('body').scrollspy({
        target: '.bs-docs-sidebar',
        offset: 40
    });


    var data = {
        labels: ${raw((statistics.labels as JSON).toString())},
        datasets: [
            {
                label: "Gæst",
                fillColor: "rgba(220,220,220,0.2)",
                strokeColor: "rgba(220,220,220,1)",
                pointColor: "rgba(220,220,220,1)",
                pointStrokeColor: "#fff",
                pointHighlightFill: "#fff",
                pointHighlightStroke: "rgba(220,220,220,1)",
                data: ${raw(statistics.data as JSON).toString()}
            }
        ]
    };

    var ctx = $("#chart").get(0).getContext("2d");
    // This will get the first returned node in the jQuery collection.
    var myNewChart = new Chart(ctx).Line(data);
    console.log(ctx);
</script>

</body>
</html>