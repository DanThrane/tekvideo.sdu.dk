<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main">
    <title>Videoer for ${subject.name}</title>
</head>

<body>

<div class="row">
    <h1>Videoer for ${subject.name}</h1>
</div>

<g:each status="i" in="${subject.videos}" var="video">
    <div class="row">
        <div class="col-md-3">
            <img src="http://img.youtube.com/vi/${video.youtubeId}/hqdefault.jpg" class="img-responsive" alt="Video thumbnail">
        </div>
        <div class="col-md-9">
            <g:link mapping="teaching" params="${[teacher: params.teacher, subject: params.subject,
                                                  course: params.course, vidid: i]}">
                ${video.name}
            </g:link>
            <p>
                Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam ac tellus quis odio hendrerit scelerisque. Ut augue massa, pretium at consectetur at, maximus sed nulla. Sed semper tempor maximus. Praesent quis sollicitudin lorem. Maecenas ac leo at dolor faucibus faucibus. Nunc id eros vitae purus semper gravida. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Ut vel cursus quam, vitae ultrices mi. Maecenas hendrerit sem nulla, sed tempor lacus accumsan ac. Fusce facilisis sit amet purus in auctor. Morbi vitae nunc dictum, porta augue placerat, laoreet sem.
            </p>
        </div>
    </div>
</g:each>

</body>
</html>