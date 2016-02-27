<%@ page import="dk.sdu.tekvideo.NodeStatus; dk.sdu.tekvideo.FaIcon; dk.danthrane.twbs.ButtonStyle" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main_fluid">
    <title>Videoer for ${subject.name}</title>
</head>

<body>

<twbs:pageHeader>
    <h3>Videoer <small>${subject.name}</small></h3>
</twbs:pageHeader>

<twbs:row>
    <twbs:column>
        <ol class="breadcrumb">
            <li><g:link uri="/">Hjem</g:link></li>
            <li>
                <sdu:linkToTeacher teacher="${subject.course.teacher}">
                    ${subject.course.teacher}
                </sdu:linkToTeacher>
            </li>
            <li>
                <sdu:linkToCourse course="${subject.course}">
                    ${subject.course.fullName} (${subject.course.name})
                </sdu:linkToCourse>
            </li>
            <li class="active">${subject.name}</li>
        </ol>
    </twbs:column>
</twbs:row>

<g:each status="i" in="${subject.visibleVideos}" var="video">
    <sdu:card>
        <twbs:column cols="2">
            <img src="http://img.youtube.com/vi/${video.youtubeId}/hqdefault.jpg" class="img-responsive"
                 alt="Video thumbnail">
        </twbs:column>
        <twbs:column cols="10">
            <sdu:linkToVideo video="${video}">
                ${video.name}
            </sdu:linkToVideo>
            <p>
                <markdown:renderHtml>${video.description}</markdown:renderHtml>
            </p>
        </twbs:column>
    </sdu:card>
</g:each>

<g:render template="/course/sidebar" model="${[course: subject.course]}"/>

</body>
</html>