<%@ page import="dk.danthrane.twbs.ButtonStyle; dk.sdu.tekvideo.FaIcon" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main_fluid">
    <title>${video.name}</title>
</head>

<body>

<twbs:pageHeader>
    <h3>${video.name}</h3>
</twbs:pageHeader>

<div class="embed-responsive embed-responsive-16by9">
    <div id="wrapper" style="z-index: 20"></div>
    <div id="player"></div>
</div>

<g:content key="sidebar-right">
    <twbs:pageHeader>
        <h3>Indhold</h3>
    </twbs:pageHeader>

    <ul id="videoNavigation">
    </ul>

    <div class="sidebar-pull-bottom">
        <twbs:button style="${ButtonStyle.PRIMARY}" block="true" id="checkAnswers">
            <fa:icon icon="${FaIcon.CHECK}" />
            Tjek svar
        </twbs:button>
    </div>
</g:content>

<script type="text/javascript">
    $(document).ready(function() {
        ivids.bootstrap(
                "#player",
                "${raw(video.youtubeId)}",
                ${video.videoType}
                <g:if test="${video.timelineJson}">
                    , ${raw(video.timelineJson)}
                </g:if>
        );

        %{-- Used by the event framework to determine the origin of the event --}%
        events.setMetaData({
            "video": ${video.id}
        });

        events.emit({ "kind": "VISIT_VIDEO" }, true);
    });
</script>
</body>
</html>