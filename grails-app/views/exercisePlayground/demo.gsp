<%@ page import="grails.converters.JSON; dk.sdu.tekvideo.FaIcon; dk.danthrane.twbs.GridSize; dk.danthrane.twbs.ButtonStyle; dk.danthrane.twbs.ButtonSize" %>

<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main_fluid"/>
    <title>Hjem</title>

    <g:render template="/polymer/includePolymer" />

    <link rel="import" href="${createLink(absolute:true, uri:'/assets/')}/components/tekvideo-exercise-card.html">
    <style>
    .polymer {
        font-family: 'Roboto', 'Noto', sans-serif;
        line-height: 1.5;
    }

    tekvideo-exercise-card {
        width: 30%;
        margin: 15px 15px 15px 0px;
    }
    </style>
</head>

<body class="polymer">

<twbs:pageHeader>
    <h3>Opgaver</h3>
</twbs:pageHeader>

<div style="display: flex; flex-wrap: wrap;">
    <g:each in="${all}" var="exercise" status="index">
        <tekvideo-exercise-card
                url="${g.createLink(action: "show", id: exercise.name)}"
                title="${exercise.name}"
                description="Ingen beskrivelse"
                thumbnail="http://placekitten.com/g/300/30${index}">
        </tekvideo-exercise-card>
    </g:each>
</div>
</body>
</html>
