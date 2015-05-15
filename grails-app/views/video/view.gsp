<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main">
    <title>${video.name}</title>
</head>

<body>

<twbs:row>
    <h2>${video.name}</h2>
</twbs:row>

<twbs:row>
    <twbs:column cols="9">
        <div id="wrapper">
            <g:if test="${debugMode}">
                <div id="frameOverlay" style="width: 800px; height: 600px;"></div>
            </g:if>
            <div id="player" style="width: 800px; height: 600px;"></div>
        </div>
    </twbs:column>
    <twbs:column cols="3">
        <h3>Indhold</h3>

        <ul id="videoNavigation">
        </ul>
    </twbs:column>
</twbs:row>
<br>

<twbs:row>
    <twbs:linkButton id="checkAnswers">Tjek svar</twbs:linkButton>
</twbs:row>

<g:if test="${debugMode}">
    <twbs:row>
        <h3>Debug info:</h3>
        <b>Mouse position:</b> <span id="cursorPosition"></span>
    </twbs:row>
</g:if>

<script type="text/javascript">
    $(document).ready(function() {
        ivids.bootstrap(
                "#player",
                "${raw(video.youtubeId)}"
                <g:if test="${video.timelineJson}">
                    , ${raw(video.timelineJson)}
                </g:if>
        );

        events.emit({
            "kind": "VISIT_VIDEO",
            "teacher": "${params.teacher}",
            "course": "${params.course}",
            "subject": "${params.subject}",
            "video": ${params.videoId}
        }, true);
    });
</script>
</body>
</html>