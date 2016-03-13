<%@ page import="dk.sdu.tekvideo.FaIcon; dk.danthrane.twbs.GridSize; dk.danthrane.twbs.ButtonStyle; dk.danthrane.twbs.ButtonSize" %>

<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main_fluid"/>
    <title>Hjem</title>
</head>

<body>

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

<g:each in="${featuredVideos}" var="breakdown">
    <sdu:card>
        <twbs:column sm="1">
            <img src="http://img.youtube.com/vi/${breakdown.video.youtubeId}/hqdefault.jpg"
                 class="img-responsive"
                 alt="Video thumbnail">
            <fa:icon icon="${FaIcon.EYE}"/> ${breakdown.viewCount} &nbsp;
            <fa:icon icon="${FaIcon.COMMENTS}"/> ${breakdown.commentCount}
        </twbs:column>
        <twbs:column sm="11">
            <sdu:linkToVideo video="${breakdown.video}">
                ${breakdown.video.name}
            </sdu:linkToVideo>

            <markdown:renderHtml>${breakdown.video.description}</markdown:renderHtml>
        </twbs:column>
    </sdu:card>
</g:each>

</body>
</html>
