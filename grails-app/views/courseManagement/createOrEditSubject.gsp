<%@ page import="dk.sdu.tekvideo.NodeStatus; dk.sdu.tekvideo.FaIcon; dk.danthrane.twbs.ButtonStyle" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Nyt emne</title>
    <meta name="layout" content="main" />
    <asset:javascript src="list.js" />
    <sdu:requireAjaxAssets />
</head>

<body>
<twbs:row>
    <twbs:column>
        <twbs:pageHeader>
            <h3>
                <g:if test="${isEditing}">
                    Administrering af emne
                    <small>${command.domain.name}</small>
                </g:if>
                <g:else>
                    Nyt emne
                </g:else>
            </h3>
        </twbs:pageHeader>

        <h4>Grundlæggende detaljer</h4>
        <twbs:form method="POST" action="${createLink(action: "postSubject", id: course.id)}">
            <sducrud:hiddenFields />

            <twbs:input name="domain.name" bean="${command?.domain}" labelText="Navn" autofocus="true" />
            <twbs:textArea name="domain.description" bean="${command?.domain}" labelText="Beskrivelse"
                           rows="10" />
            <twbs:checkbox labelText="Synligt for studerende" name="visible"
                           value="${command?.domain?.localStatus == NodeStatus.VISIBLE}" />

            <sducrud:saveButton />
        </twbs:form>
    </twbs:column>
</twbs:row>

</body>
</html>