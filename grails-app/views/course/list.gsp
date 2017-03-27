<%@ page import="dk.sdu.tekvideo.FaIcon; dk.danthrane.twbs.Icon" %>
<%@ page import="dk.danthrane.twbs.ButtonStyle; dk.danthrane.twbs.ButtonSize" %>
<html>
<head>
    <meta name="layout" content="main_fluid">
    <title>Kurser</title>

    <g:render template="/polymer/includePolymer" />

    <link rel="import" href="${createLink(absolute:false, uri:'/static/')}/components/tv-browser.html">

</head>

<body>

<twbs:pageHeader>
    <h3>Kurser</h3>
</twbs:pageHeader>

<twbs:row>
    <twbs:column md="12">
        <twbs:row>
            <twbs:column>
                <ol class="breadcrumb">
                    <li><g:link uri="/">Hjem</g:link></li>
                    <li class="active">Kurser</li>
                </ol>
            </twbs:column>
        </twbs:row>

        <tv-browser items='${data as grails.converters.JSON}'></tv-browser>
    </twbs:column>
</twbs:row>

</body>
</html>