<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main">
    <title>Videoer for ${subject.name}</title>
</head>

<body>

%{-- Should be automatic --}%
<twbs:row>
    <twbs:column>
        <ol class="breadcrumb">
            <li><g:link uri="/">Hjem</g:link></li>
            <li><g:link controller="teacher" action="list" id="${params.teacher}">${params.teacher}</g:link></li>
            <li>
                <g:link mapping="teaching" params="${[teacher: params.teacher, course: params.course]}">
                    ${subject.course.fullName} (${subject.course.name})
                </g:link>
            </li>
            <li class="active">${subject.name}</li>
        </ol>
    </twbs:column>
</twbs:row>

<twbs:row>
    <twbs:column><h3>Videoer for ${subject.name}</h3></twbs:column>
</twbs:row>

<g:each status="i" in="${subject.videos}" var="video">
    <twbs:row class="video-row">
        <twbs:column cols="3">
            <img src="http://img.youtube.com/vi/${video.youtubeId}/hqdefault.jpg" class="img-responsive" alt="Video thumbnail">
        </twbs:column>
        <twbs:column cols="9">
            <g:link mapping="teaching" params="${[teacher: params.teacher, subject: params.subject,
                                                  course: params.course, vidid: i]}">
                ${video.name}
            </g:link>
            <p>
                Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam ac tellus quis odio hendrerit scelerisque. Ut augue massa, pretium at consectetur at, maximus sed nulla. Sed semper tempor maximus. Praesent quis sollicitudin lorem. Maecenas ac leo at dolor faucibus faucibus. Nunc id eros vitae purus semper gravida. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Ut vel cursus quam, vitae ultrices mi. Maecenas hendrerit sem nulla, sed tempor lacus accumsan ac. Fusce facilisis sit amet purus in auctor. Morbi vitae nunc dictum, porta augue placerat, laoreet sem.
            </p>
        </twbs:column>
    </twbs:row>
</g:each>

</body>
</html>