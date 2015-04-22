<%@ page import="dk.sdu.tekvideo.twbs.Icon; java.time.LocalDateTime" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="admin">
    <title><g:message code="admin.video.summary.title"/></title>
</head>

<body>

<twbs:row>
    <twbs:column cols="8">
        <h1 class="page-header"><g:message code="admin.video.summary.title"/></h1>

        <strong><g:message code="admin.video.summary.filter"/></strong>
        <hr/>

        <table class="table table-hover">
            <thead>
            <tr>
                <th><g:message code="admin.video.summary.thead.name"/></th>
                <th><g:message code="admin.video.summary.thead.releaseDate"/></th>
                <th><g:message code="admin.video.summary.thead.views"/></th>
                <th><g:message code="admin.video.summary.thead.answers"/></th>
                <th><g:message code="admin.video.summary.thead.correctAnswers"/></th>
                <th><g:message code="admin.video.summary.thead.mostCommonIncorrect"/></th>
                <th></th>
            </tr>
            </thead>
            <g:each in="${summary}" var="entry">
                <tr>
                    <td>${entry.key.name}</td>
                    <td><date:oldDtFormatter date="${entry.key.dateCreated}"/></td>
                    <td>${entry.value.totalViews}</td>
                    <td>${entry.value.answersGiven}</td>
                    <td>${entry.value.correctAnswers}</td>
                    <td>${entry.value.mostCommonIncorrect}</td>
                    <td>
                        <twbs:linkButton action="videoDetailed" btnstyle="action">
                            <twbs:icon icon="${Icon.SEARCH}"/>
                        </twbs:linkButton>
                    </td>
                </tr>
            </g:each>
        </table>
    </twbs:column>
    <twbs:column cols="4">
        <h3><g:message code="admin.video.summary.sidebar.header"/></h3>
    </twbs:column>
</twbs:row>

</body>
</html>