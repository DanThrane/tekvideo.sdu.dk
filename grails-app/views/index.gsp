<%@ page import="dk.sdu.tekvideo.FaIcon; dk.danthrane.twbs.GridSize; dk.danthrane.twbs.ButtonStyle; dk.danthrane.twbs.ButtonSize" %>

<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main_fluid"/>
    <title>Hjem</title>

    <script>
        // setup Polymer options
        window.Polymer = {lazyRegister: true, dom: 'shadow'};

        // load webcomponents polyfills
        (function() {
            if ('registerElement' in document
                    && 'import' in document.createElement('link')
                    && 'content' in document.createElement('template')) {
                // browser has web components
            } else {
                // polyfill web components
                var e = document.createElement('script');
                e.src = '${createLink(absolute:true, uri:'/assets/')}/bower_components/webcomponentsjs/webcomponents-lite.min.js';
                document.head.appendChild(e);
            }
        })();

    </script>


    <link rel="import" href="${createLink(absolute:true, uri:'/assets/')}/components/tekvideo-exercise-card.html">
    <style>
        .polymer {
            font-family: 'Roboto', 'Noto', sans-serif;
            line-height: 1.5;
        }

        tekvideo-exercise-card {
            margin: 15px 15px 15px 0px;
        }
    </style>
</head>

<body class="polymer">

<g:if test="${student}">
    <twbs:pageHeader>
        <h3>Mine fag <small>for ${student.user.username}</small></h3>
    </twbs:pageHeader>

    <sdu:card>
        <g:if test="${courses.empty}">
            Du er ikke tilmeldt nogle fag endnu!
        </g:if>
        <g:else>
            <ul class="list-unstyled">
                <g:each in="${courses}" var="course">
                    <li>
                        <sdu:linkToCourse course="${course}">
                            ${course.name} &mdash; ${course.fullName}
                        </sdu:linkToCourse>
                    </li>
                </g:each>
            </ul>
        </g:else>
    </sdu:card>
</g:if>

<twbs:pageHeader>
    <h3>Fremhævede videoer</h3>
</twbs:pageHeader>

<div style="display: flex; flex-wrap: wrap;">
<g:each in="${featuredVideos}" var="breakdown" status="index">
    <tekvideo-exercise-card
            href="${sdu.createLinkToVideo(video: breakdown.video)}"
            title="${breakdown.video.name}"
            comment-count=${breakdown.commentCount}
            view-count=${breakdown.viewCount}
            author="${breakdown.video.subject.course.teacher.toString()}"
            course="${breakdown.video.subject.course.name}"
            subject="${breakdown.video.subject.name}"
            video-id="${breakdown.video.youtubeId}"
            time="--:--">
        <sdu:abbreviate>${breakdown.video.description}</sdu:abbreviate>
    </tekvideo-exercise-card>
</g:each>
</div>
</body>
</html>
