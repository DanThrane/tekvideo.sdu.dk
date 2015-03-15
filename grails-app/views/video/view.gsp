<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main">
    <title>${video.name}</title>
</head>

<body>
<div class="row">
    <h1>${video.name}</h1>
</div>

<div class="row">
    <div class="col-lg-9">
        <h1>Video</h1>
        <div id="wrapper">
            <div id="frameOverlay" style="width: 800px; height: 600px;"></div>
            <div id="player" style="width: 800px; height: 600px;"></div>
        </div>
    </div>
    <div class="col-lg-3">
        <h3>Indhold</h3>

        <ul id="videoNavigation">
        </ul>
    </div>
</div>
<br />
<div class="row">
    <a class="btn btn-default" id="checkAnswers">Tjek svar</a>
</div>
<div class="row">
    <h3>Debug info:</h3>
    <b>Mouse position:</b> <span id="cursorPosition"></span>
</div>

<script type="text/javascript">
    $(document).ready(function() {
        ivids.bootstrap("#player", "${raw(video.youtubeId)}", ${raw(video.timelineJson)});
    });
</script>
</body>
</html>