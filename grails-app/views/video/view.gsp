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
            %{--<div id="frameOverlay" style="width: 800px; height: 600px;"></div>--}%
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
<twbs:row>
    <h3>Debug info:</h3>
    <b>Mouse position:</b> <span id="cursorPosition"></span>
</twbs:row>

<script type="text/javascript">
    $(document).ready(function() {
        ivids.bootstrap("#player", "${raw(video.youtubeId)}", ${raw(video.timelineJson)});
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