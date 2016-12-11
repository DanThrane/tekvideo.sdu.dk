<%@ page import="grails.converters.JSON; dk.sdu.tekvideo.FaIcon; dk.danthrane.twbs.GridSize; dk.danthrane.twbs.ButtonStyle; dk.danthrane.twbs.ButtonSize" %>

<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main_fluid"/>
    <title>Hjem</title>

    <g:render template="/polymer/includePolymer" />

    <link rel="import" href="${createLink(absolute:true, uri:'/assets/')}/components/exercise-editor/tve-group-renderer.html">
    <style>
    .polymer {
        font-family: 'Roboto', 'Noto', sans-serif;
        line-height: 1.5;
    }
    </style>
</head>

<body class="polymer">

<tve-group-renderer id="renderer"></tve-group-renderer>
<script>
    var renderer = document.getElementById('renderer');
    var exercisePoolObj = {
        <g:each in="${subExercises}" var="item">
        ${item.id}: ${raw(item.exercise)},
        </g:each>
    };

    %{-- Working around the fact that we don't store the assignments in a (server-side) structured format --}%
    var exercisePool = [];
    for (var key in exercisePoolObj) {
        var obj = exercisePoolObj[key];
        obj.identifier = key;
        exercisePool.push(obj);
    }

    renderer.exercisePool = exercisePool;
    renderer.display(0);

    renderer.addEventListener("grade", function(e) {
        console.log(e.detail);
    });

    renderer.addEventListener("backToMenu", function() {
        document.location = "${sdu.createLinkToSubject(subject: exercise.subject)}";
    });
</script>

</body>
</html>
