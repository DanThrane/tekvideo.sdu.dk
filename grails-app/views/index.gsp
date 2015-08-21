<%@ page import="dk.danthrane.twbs.GridSize; dk.danthrane.twbs.ButtonStyle; dk.danthrane.twbs.ButtonSize" %>

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
                        <g:link mapping="teaching" params="${[teacher: course.teacher, course: course.name]}">
                            ${course.name} &mdash; ${course.fullName}
                        </g:link>
                    </li>
                </g:each>
            </ul>
        </g:else>
    </sdu:card>
</g:if>

<twbs:pageHeader>
    <h3>Fremhævet videoer</h3>
</twbs:pageHeader>

<g:each in="${featuredVideos}" var="video">
    <sdu:linkCard controller="video" action="viewV" id="${video.id}">
        <twbs:column sm="1" class="card-image">
            <img src="http://img.youtube.com/vi/${video.youtubeId}/hqdefault.jpg" alt="Video thumbnail">
        </twbs:column>
        <twbs:column sm="11">
            <g:link controller="video" action="viewV" id="${video.id}">
                ${video.name}
            </g:link>
            <p>
                <markdown:renderHtml>${video.description}</markdown:renderHtml>
            </p>
        </twbs:column>
    </sdu:linkCard>
</g:each>

</body>
</html>
