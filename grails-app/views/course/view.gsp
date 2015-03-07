<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main">
    <title>Courses</title>
</head>

<body>
    <div class="row">
        <h1>${course.name}</h1>
        <p>${course.description}</p>
    </div>

    <g:each in="${course.subjects}" var="subject">
        <div class="row">
            <div class="col-md-3">
                <img src="http://placehold.it/160x90" alt="Thumbnail">
            </div>
            <div class="col-md-9">
                <g:link action="view" controller="Subject" id="${subject.id}">
                    ${subject.name} (${subject.videos.size()} videoer)
                </g:link>
                <p>
                Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam ac tellus quis odio hendrerit scelerisque. Ut augue massa, pretium at consectetur at, maximus sed nulla. Sed semper tempor maximus. Praesent quis sollicitudin lorem. Maecenas ac leo at dolor faucibus faucibus. Nunc id eros vitae purus semper gravida. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Ut vel cursus quam, vitae ultrices mi. Maecenas hendrerit sem nulla, sed tempor lacus accumsan ac. Fusce facilisis sit amet purus in auctor. Morbi vitae nunc dictum, porta augue placerat, laoreet sem.
                </p>
            </div>
        </div>
    </g:each>

</body>
</html>