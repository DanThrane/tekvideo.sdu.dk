<%@ page import="grails.converters.JSON; dk.sdu.tekvideo.NodeStatus; dk.sdu.tekvideo.FaIcon; dk.danthrane.twbs.ButtonStyle" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main_fluid">
    <title>Opgaver for ${subject.name}</title>

    <g:render template="/polymer/includePolymer" />
    <link rel="import" href="${createLink(absolute:true, uri:'/assets/')}/components/tv-browser.html">
</head>

<body>

<twbs:pageHeader>
    <h3>
        Opgaver <small>${subject.name}</small>
        <g:if test="${showSignup}">
            <span style="float: right;">
                <g:render template="/course/showsignup" model="${pageScope.variables}" />
            </span>
        </g:if>
    </h3>
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

<tv-browser items='${data as JSON}'></tv-browser>


%{--<g:render template="/course/sidebar" model="${[course: subject.course]}"/>--}%

</body>
</html>