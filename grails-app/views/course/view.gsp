<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="dk.sdu.tekvideo.FaIcon; dk.danthrane.twbs.ButtonStyle; dk.danthrane.twbs.ButtonSize" %>
<html>
<head>
    <meta name="layout" content="main_fluid">
    <title>${course.name}</title>
</head>
<body>

<twbs:pageHeader>
    <h3>Emner <small>${course.fullName} (${course.name})</small></h3>
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

<g:each in="${course.visibleSubjects}" var="subject">
    <sdu:linkCard mapping="teaching" params="${[teacher: course.teacher.user.username, course: course.name,
                                                subject: subject.name]}">
        <twbs:column cols="12">
            <g:link mapping="teaching" params="${[teacher: course.teacher.user.username, course: course.name,
                                                  subject: subject.name]}">
                ${subject.name} (${subject.visibleVideos.size()} videoer)
            </g:link>

            <markdown:renderHtml>${subject.description}</markdown:renderHtml>
            
        </twbs:column>
    </sdu:linkCard>
</g:each>

<g:render template="sidebar" model="${pageScope.variables}" />
</body>
</html>