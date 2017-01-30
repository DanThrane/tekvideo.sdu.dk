<%@ page import="java.time.LocalDateTime; dk.danthrane.twbs.ButtonStyle; dk.sdu.tekvideo.FaIcon" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main_fluid">
    <title>Stats</title>

    <g:render template="/polymer/includePolymer"/>
    <link rel="import" href="${createLink(absolute:true, uri:'/assets/')}/components/tv-browser.html">
</head>
<body>

<g:content key="sidebar-left">
    Sidebar goes here.
</g:content>

<twbs:pageHeader>
    <h3>Stats</h3>
</twbs:pageHeader>

<twbs:row>
    <twbs:column>
        <ol class="breadcrumb">
            <li class="active">Stats</li>
        </ol>
    </twbs:column>
</twbs:row>

<twbs:row>
    <twbs:column>
        <tv:browser items="${items}" />
    </twbs:column>
</twbs:row>

</body>
</html>
