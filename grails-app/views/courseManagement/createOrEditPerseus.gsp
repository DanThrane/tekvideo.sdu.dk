<%@ page import="dk.sdu.tekvideo.NodeStatus; dk.danthrane.twbs.GridSize; dk.danthrane.twbs.InputSize; dk.danthrane.twbs.Icon; dk.danthrane.twbs.ButtonSize; dk.sdu.tekvideo.FaIcon; dk.danthrane.twbs.ButtonStyle" contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main_fluid">

    <title>Perseus Playground</title>
    <fa:require/>
    <asset:stylesheet src="perseus/perseus.css"/>
    <asset:stylesheet src="perseus/khan-site.css"/>
    <asset:stylesheet src="perseus/khan-exercise.css"/>
    <asset:stylesheet src="perseus/editor.css"/>
    <asset:stylesheet src="perseus/perseus-admin-package/devices.min.css"/>

</head>

<body>
<twbs:pageHeader>
    <h3>
        <g:if test="${isEditing}">
            Redigering af ${exercise.name}
        </g:if>
        <g:else>
            Ny perseus opgave
        </g:else>
    </h3>
</twbs:pageHeader>

<twbs:row>
    <twbs:column>
        <ol class="breadcrumb">
            <li><g:link action="index" controller="courseManagement">Mine Kurser</g:link></li>
            <li>
                <g:link action="manage" controller="courseManagement" id="${course.id}">
                    ${course.fullName} (${course.name})
                </g:link>
            </li>
            <g:if test="${isEditing}">
                <li>
                    <g:link action="manageSubject" controller="courseManagement" id="${exercise.parent.id}">
                        ${exercise.parent.name}
                    </g:link>
                </li>
                <li class="active">Redigering af ${exercise.name}</li>
            </g:if>
            <g:else>
                <li class="active">Ny perseus opgave</li>
            </g:else>
        </ol>
    </twbs:column>
</twbs:row>


<div id="perseus-container">
</div>

<script>
    window.rootUrl = "${createLink(absolute:true, uri:'/')}";
</script>

<asset:javascript src="perseus/lib/babel-polyfills.min.js"/>
<asset:javascript src="perseus/lib/underscore.js"/>
<asset:javascript src="perseus/lib/react-with-addons.js"/>
<asset:javascript
        src="perseus/lib/mathjax/2.1/MathJax.js?config=KAthJax-f3c5d145ec6d4e408f74f28e1aad49db&amp;delayStartupUntil=configured"/>
<asset:javascript src="perseus/lib/katex/katex.js"/>
<asset:javascript src="perseus/lib/mathquill/mathquill-basic.js"/>
<asset:javascript src="perseus/lib/kas.js"/>
<asset:javascript src="perseus/lib/i18n.js"/>
<asset:javascript src="perseus/lib/jquery.qtip.js"/>

<asset:javascript src="perseus/demo-perseus.js"/>

<asset:javascript src="video-creator.js"/>
<asset:javascript src="./courseManagement/app.js"/>
<asset:stylesheet src="./vendor/proton/style.min.css"/>

</body>
</html>
