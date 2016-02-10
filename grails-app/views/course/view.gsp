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
                <sdu:linkToTeacher teacher="${course.teacher}">
                    ${course.teacher}
                </sdu:linkToTeacher>
            </li>
            <li class="active">${course.fullName} (${course.name})</li>
        </ol>
    </twbs:column>
</twbs:row>

<g:each in="${course.visibleSubjects}" var="subject">
    <sdu:card>
        <twbs:column cols="12">
            <sdu:linkToSubject subject="${subject}">
                ${subject.name} (${subject.visibleVideos.size()} videoer)
            </sdu:linkToSubject>

            <markdown:renderHtml>${subject.description}</markdown:renderHtml>
            
        </twbs:column>
    </sdu:card>
</g:each>

<g:render template="sidebar" model="${pageScope.variables}" />
</body>
</html>