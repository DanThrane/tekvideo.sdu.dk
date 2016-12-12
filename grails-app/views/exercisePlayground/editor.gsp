<%@ page import="grails.converters.JSON; dk.sdu.tekvideo.FaIcon; dk.danthrane.twbs.GridSize; dk.danthrane.twbs.ButtonStyle; dk.danthrane.twbs.ButtonSize" %>

<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main_fluid"/>
    <title>Hjem</title>

    <g:render template="/polymer/includePolymer" />

    <link rel="import" href="${createLink(absolute:true, uri:'/assets/')}/components/exercise-editor/tv-exercise-editor.html">
    <style>
    .polymer {
        font-family: 'Roboto', 'Noto', sans-serif;
        line-height: 1.5;
    }
    </style>
</head>

<body class="polymer">

<tv-exercise-editor id="editor"></tv-exercise-editor>

<script>
    var editor = document.getElementById('editor');
    editor.addEventListener("save", function() {
        var name = editor.name;
        var documentContent = editor.documentContent;
        var widgets = editor.widgets;

        if (name && documentContent && widgets) {
            Util.postJson("${createLink(action: "createExercise")}", {
                name: name,
                exercise: JSON.stringify({
                    document: documentContent,
                    widgets: widgets
                })
            }, {
                success: function() {
                    alert("Opgave gemt");
                },
                error: function() {
                    alert("Der skete en fejl");
                }
            });
        } else {
            alert("Der mangler information. Har du glemt navn?");
        }
    });
</script>

</body>
</html>
