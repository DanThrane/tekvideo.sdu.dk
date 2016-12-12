<%@ page import="java.time.LocalDateTime; dk.danthrane.twbs.ButtonStyle; dk.sdu.tekvideo.FaIcon" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main_fluid">
    <title>${video.name}</title>
</head>

<body>

<twbs:pageHeader>
    <h3>${video.name}</h3>
</twbs:pageHeader>

<twbs:row>
    <twbs:column>
        <ol class="breadcrumb">
            <li><g:link uri="/">Hjem</g:link></li>
            <li>
                <sdu:linkToTeacher teacher="${video.subject.course.teacher}">
                    ${video.subject.course.teacher}
                </sdu:linkToTeacher>
            </li>
            <li>
                <sdu:linkToCourse course="${video.subject.course}">
                    ${video.subject.course.fullName} (${video.subject.course.name})
                </sdu:linkToCourse>
            </li>
            <li>
                <sdu:linkToSubject subject="${video.subject}">
                    ${video.subject.name}
                </sdu:linkToSubject>
            </li>
            <li class="active">${video.name}</li>
        </ol>
    </twbs:column>
</twbs:row>

<div class="embed-responsive embed-responsive-16by9">
    <div class="wrapper" id="wrapper" style="z-index: 20"></div>
    <div id="player"></div>
</div>

<g:content key="content-below-the-fold">
    <exercise:comments exercise="${video}" />
</g:content>


<g:content key="sidebar-right">
    <twbs:pageHeader>
        <h3>Indhold</h3>
    </twbs:pageHeader>

    <ul id="videoNavigation" class="video-navigation">
    </ul>

    <div id="similar-resources">
        <g:if test="${!video.similarResources.empty}">
            <twbs:pageHeader><h4>Se også</h4></twbs:pageHeader>
            <ul>
                <g:each in="${video.similarResources}">
                    <li><a href="${it.link}">${it.title}</a></li>
                </g:each>
            </ul>
        </g:if>
    </div>

    <div class="sidebar-pull-bottom">
        <twbs:button style="${ButtonStyle.PRIMARY}" block="true" id="checkAnswers">
            <fa:icon icon="${FaIcon.CHECK}"/>
            Tjek svar
        </twbs:button>
    </div>
</g:content>

<script type="text/javascript">
    $(document).ready(function () {
        var player = new InteractiveVideoPlayer();
        player.playerElement = $("#player");
        player.wrapperElement = $("#wrapper");
        player.navigationElement = $("#videoNavigation");
        player.checkButton = $("#checkAnswers");

        player.startPlayer("${raw(video.youtubeId)}",
                ${video.videoType}
                <g:if test="${video.timelineJson}">
                , ${raw(video.timelineJson)}
                </g:if>);

        %{-- Used by the event framework to determine the origin of the event --}%
        events.setMetaData({
            "video": ${video.id}
        });

        events.emit({"kind": "VISIT_VIDEO"}, true);
    });
</script>
</body>
</html>