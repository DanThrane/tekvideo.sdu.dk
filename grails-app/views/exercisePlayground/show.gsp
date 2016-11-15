<%@ page import="grails.converters.JSON; dk.sdu.tekvideo.FaIcon; dk.danthrane.twbs.GridSize; dk.danthrane.twbs.ButtonStyle; dk.danthrane.twbs.ButtonSize" %>

<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main_fluid"/>
    <title>Hjem</title>

    <g:render template="/polymer/includePolymer" />

    <link rel="import" href="${createLink(absolute:true, uri:'/assets/')}/components/exercise-editor/tve-renderer.html">
    <style>
    .polymer {
        font-family: 'Roboto', 'Noto', sans-serif;
        line-height: 1.5;
    }
    </style>
</head>

<body class="polymer">

<tve-renderer id="renderer" is-interactive></tve-renderer>

<script>
    var exercise = ${raw(exercise.exercise)};

    var renderer = document.getElementById('renderer');
    renderer.content = exercise.document;
    renderer.widgets = exercise.widgets;

    renderer.addEventListener("widget-action", function(e) {
        console.log("Widget action: ", e);
    });

    renderer.addEventListener("grade", function(e) {
        console.log("Grading: ", e);
    });
</script>

</body>
</html>
