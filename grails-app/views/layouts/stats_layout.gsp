<%@ page import="grails.converters.JSON; java.time.LocalDateTime; dk.danthrane.twbs.ButtonStyle; dk.sdu.tekvideo.FaIcon" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main_fluid">
    <title>Stats - <g:layoutTitle /></title>
    <g:layoutHead />
</head>

<body>

<g:content key="sidebar-left">
    Sidebar goes here.
</g:content>

<twbs:pageHeader>
    <h3>Stats <small><g:layoutTitle /></small></h3>
</twbs:pageHeader>

<twbs:row>
    <twbs:column>
        <ol class="breadcrumb">
            <g:each in="${breadcrumbs}" var="crumb">
                <g:if test="${crumb.active}">
                    <li class="active">${crumb.title}</li>
                </g:if>
                <g:else>
                    <li><a href="${crumb.url}">${crumb.title}</a></li>
                </g:else>
            </g:each>
        </ol>
    </twbs:column>
</twbs:row>

<g:layoutBody />

</body>
</html>
