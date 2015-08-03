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
        <h3>Mine fag</h3>
    </twbs:pageHeader>

    <sdu:card>
        <g:each in="${student.c}">

        </g:each>
    </sdu:card>
</g:if>

<twbs:pageHeader>
    <h3>Fremhævet videoer</h3>
</twbs:pageHeader>

<g:each in="${featuredVideos}" var="video">
    <sdu:linkCard controller="video" action="viewV" id="${video.id}">
        <twbs:column cols="1">
            <img src="http://img.youtube.com/vi/${video.youtubeId}/hqdefault.jpg" class="img-responsive" alt="Video thumbnail">
        </twbs:column>
        <twbs:column cols="11">
            <g:link controller="video" action="viewV" id="${video.id}">
                ${video.name}
            </g:link>
            <p>
                ${video.description}
            </p>
        </twbs:column>
    </sdu:linkCard>
</g:each>

</body>
</html>
