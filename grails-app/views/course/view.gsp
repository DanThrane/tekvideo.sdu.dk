<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="dk.danthrane.twbs.ButtonStyle; dk.danthrane.twbs.ButtonSize" %>
<html>
<head>
    <meta name="layout" content="main">
    <title>${course.name}</title>
</head>

<body>

    %{-- Should be automatic --}%
    <twbs:row>
        <twbs:column>
            <ol class="breadcrumb">
                <li><g:link uri="/">Hjem</g:link></li>
                <li>
                    <g:link mapping="teaching" params="${[teacher: params.teacher]}">
                        ${params.teacher}
                    </g:link>
                </li>
                <li class="active">Emner &mdash; ${course.fullName} (${course.name})</li>
            </ol>
        </twbs:column>
    </twbs:row>

    <twbs:row>
        <twbs:column>
            <h3>Emner &mdash; ${course.fullName} (${course.name})</h3>
        </twbs:column>
    </twbs:row>

    <hr />

    <twbs:row>
        <twbs:column cols="3">
            <twbs:linkButton style="${ButtonStyle.LINK}" controller="course" action="signup" id="${course.id}">
                Vis til/afmeldinger
            </twbs:linkButton>
        </twbs:column>
    </twbs:row>

    <hr />

    <g:each in="${course.subjects}" var="subject">
        <sdu:linkCard mapping="teaching" params="${[teacher: course.teacher.user.username, course: course.name,
                                                      subject: subject.name]}">
            <twbs:column cols="3">
                <img src="http://lorempixel.com/800/400/technics/${subject.name}" class="img-responsive" alt="Thumbnail">
            </twbs:column>
            <twbs:column cols="9">
                <g:link mapping="teaching" params="${[teacher: course.teacher.user.username, course: course.name,
                                                      subject: subject.name]}">
                    ${subject.name} (${subject.videos.size()} videoer)
                </g:link>
                <p>
                    Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam ac tellus quis odio hendrerit scelerisque. Ut augue massa, pretium at consectetur at, maximus sed nulla. Sed semper tempor maximus. Praesent quis sollicitudin lorem. Maecenas ac leo at dolor faucibus faucibus. Nunc id eros vitae purus semper gravida. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Ut vel cursus quam, vitae ultrices mi. Maecenas hendrerit sem nulla, sed tempor lacus accumsan ac. Fusce facilisis sit amet purus in auctor. Morbi vitae nunc dictum, porta augue placerat, laoreet sem.
                </p>
            </twbs:column>
        </sdu:linkCard>
    </g:each>

</body>
</html>