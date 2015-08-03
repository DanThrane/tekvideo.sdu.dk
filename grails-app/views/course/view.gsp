<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="dk.danthrane.twbs.ButtonStyle; dk.danthrane.twbs.ButtonSize" %>
<html>
<head>
    <meta name="layout" content="main">
    <title>${course.name}</title>
</head>
<body>

<twbs:pageHeader>
    <h3>Emner &mdash; ${course.fullName} (${course.name})</h3>
</twbs:pageHeader>

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
        <twbs:column cols="12">
            <g:link mapping="teaching" params="${[teacher: course.teacher.user.username, course: course.name,
                                                  subject: subject.name]}">
                ${subject.name} (${subject.videos.size()} videoer)
            </g:link>
            <p>
                ${subject.description}
            </p>
        </twbs:column>
    </sdu:linkCard>
</g:each>

</body>
</html>