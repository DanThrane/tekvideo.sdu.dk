<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="grails.converters.JSON; dk.sdu.tekvideo.FaIcon; dk.danthrane.twbs.ButtonStyle; dk.danthrane.twbs.ButtonSize" %>
<html>
<head>
    <meta name="layout" content="main_fluid">
    <title>${course.name}</title>
    <g:render template="/polymer/includePolymer" />

    <link rel="import" href="${createLink(absolute:true, uri:'/assets/')}/components/tv-browser.html">
</head>

<body>

<twbs:pageHeader>
    <h3>
        ${course.fullName} (${course.name})
        <g:if test="${showSignup}">
            <span style="float: right;">
                <g:render template="showsignup" model="${pageScope.variables}" />
            </span>
        </g:if>
    </h3>
</twbs:pageHeader>

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

<tv-browser items='${data as grails.converters.JSON}'></tv-browser>

%{--<g:render template="sidebar" model="${pageScope.variables}"/>--}%
</body>
</html>
