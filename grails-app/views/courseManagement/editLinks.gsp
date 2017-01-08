<%@ page import="dk.danthrane.twbs.NavStyle; dk.sdu.tekvideo.NodeStatus; dk.sdu.tekvideo.FaIcon; dk.danthrane.twbs.ButtonStyle" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Administrering af links for ${exercise.name}</title>
    <meta name="layout" content="main_fluid"/>
</head>

<body>
<twbs:row>
    <twbs:column>
        <twbs:pageHeader>
            <h3>Administrering af links <small>${exercise.name}</small></h3>
        </twbs:pageHeader>

        <twbs:row>
            <twbs:column>
                <ol class="breadcrumb">
                    <li><g:link action="index" controller="courseManagement">Kursusadministration</g:link></li>
                    <li>
                        <g:link action="manage" controller="courseManagement" id="${exercise.subject.course.id}">
                            ${exercise.subject.course.fullName} (${exercise.subject.course.name})
                        </g:link>
                    </li>
                    <li><g:link action="manageSubject" controller="courseManagement"
                                id="${exercise.subject.id}">${exercise.subject.name}</g:link></li>
                    <li class="active">Redigering af links for ${exercise.name}</li>
                </ol>
            </twbs:column>
        </twbs:row>

        <twbs:row>
            <twbs:column>
                <h4>Eksisterende links</h4>
                <g:if test="${exercise.similarResources.isEmpty()}">
                    Der er ingen eksisterende links
                </g:if>
                <g:else>
                    <ul>
                        <g:each in="${exercise.similarResources}" var="it">
                            <li>
                                <b>${it.title}:</b> ${it.link} [<g:link action="deleteSimilarResource"
                                                                        id="${exercise.id}"
                                                                        params="${[link: it.id]}">Slet</g:link>]
                            </li>
                        </g:each>
                    </ul>
                </g:else>
            </twbs:column>
        </twbs:row>
        <hr>
        <twbs:row>
            <twbs:column>
                <h4>Tilføj nyt link</h4>
            </twbs:column>
        </twbs:row>
        <twbs:row>
            <twbs:column md="4">
                <twbs:form action="${createLink(action: "postSimilarResource")}" method="post">
                    <input type="hidden" name="id" value="${exercise.id}">
                    <twbs:input name="title" labelText="Titel"/>
                    <twbs:input name="link" labelText="Link"/>
                    <twbs:button style="${ButtonStyle.SUCCESS}">Tilføj</twbs:button>
                </twbs:form>
            </twbs:column>
        </twbs:row>

    </twbs:column>
</twbs:row>

<g:content key="sidebar-left">
    <div id="tree-container"></div>
</g:content>

<script>
    var baseUrl = "${createLink(absolute:true, uri:'/')}";

    $(function () {
        var tree = new ManagementTreeView("#tree-container", "${createLink(absolute:true, uri:'/')}");
        tree.init();
    });
</script>

<asset:javascript src="./courseManagement/app.js"/>
<asset:stylesheet src="./vendor/proton/style.min.css"/>
</body>
</html>