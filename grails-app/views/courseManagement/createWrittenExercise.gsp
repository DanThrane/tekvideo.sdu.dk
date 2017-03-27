<%@ page import="grails.converters.JSON; dk.sdu.tekvideo.FaIcon; dk.danthrane.twbs.GridSize; dk.danthrane.twbs.ButtonStyle; dk.danthrane.twbs.ButtonSize" %>

<!DOCTYPE html>
<html>
<head>
    <title>Ny opgave til ${subject.name}</title>

    <g:render template="/polymer/includePolymer" />

    <link rel="import" href="${createLink(absolute:false, uri:'/static/')}/components/exercise-editor/tve-group-editor.html">
    <style>
    .polymer {
        font-family: 'Roboto', 'Noto', sans-serif;
        line-height: 1.5;
    }
    body {
        margin: 0 auto;
        max-width: 960px;
        font-family: 'Roboto', 'Noto', sans-serif;
        line-height: 1.5;
        min-height: 100vh;
        background-color: #eee;
    }

    tve-group-editor {
        max-width: 960px;
    }
    </style>

</head>

<body class="polymer">

<tve-group-editor id="editor"></tve-group-editor>

<script>
    var Util = {};
    Util.postJson = function(url, data, callbacks) {
        $.ajax({
            type: "POST",
            url: url,
            async: true,
            data: JSON.stringify(data),
            success: callbacks.success,
            error: callbacks.error,
            complete: callbacks.complete,
            contentType: "application/json",
            dataType: 'json'
        });
    };

    var editor = document.getElementById('editor');

    <g:if test="${isEditing}">
        var exercisePoolObj = {
        <g:each in="${exercise.exercises}" var="item">
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
        editor.exercises = exercisePool;
        editor.name = "${exercise.name}";
        <g:if test="${exercise.thumbnailUrl != null}" >
            editor.thumbnailUrl = "${exercise.thumbnailUrl}";
        </g:if>
        editor.description = "${exercise.description}";
        editor.title = "Redigering af opgave ${exercise.name}";
    </g:if>

    editor.addEventListener("save", function() {
        var data = {};
        // Prepare and validate data
        data.name = editor.name;
        data.description = editor.description;
        data.streakToPass = editor.streakToPass;
        data.thumbnailUrl = editor.thumbnailUrl;
        data.exercises = editor.exercises.map(function(e) {
            var identifier = e.identifier;
            delete e.identifier;
            var exercise = JSON.stringify(e);
            return { identifier: identifier, exercise: exercise };
        });
        data.subject = ${subject.id};
        data.isEditing = ${isEditing};
        <g:if test="${isEditing}" >
        data.editing = ${exercise.id};
        </g:if>

        if (data.name) {
            // Send data
            Util.postJson("${createLink(action: "postWrittenExercise")}", data, {
                success: function () {
                    editor.displaySaveSuccess();
                },
                error: function () {
                    editor.displaySaveFailure();
                }
            });
        } else {
            editor.displayValidationFailure();
        }
    });
</script>

</body>
</html>
