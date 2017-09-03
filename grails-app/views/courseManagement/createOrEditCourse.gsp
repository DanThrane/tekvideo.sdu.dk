<%@ page import="dk.sdu.tekvideo.NodeStatus; dk.sdu.tekvideo.FaIcon; dk.danthrane.twbs.ButtonStyle" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Nyt kursus</title>
    <meta name="layout" content="main">
</head>

<body>
<twbs:row>
    <twbs:column>
        <twbs:pageHeader>
            <h3>
                <g:if test="${isEditing}">
                    Administrering af kursus detaljer
                    <small>${command.domain.fullName} (${command.domain.name})</small>
                </g:if>
                <g:else>
                    Nyt kursus
                </g:else>
            </h3>
        </twbs:pageHeader>

        <twbs:form action="${createLink(action: "postCourse")}" method="POST">
            <input type="hidden" name="isEditing" value="${isEditing}" />
            <g:if test="${isEditing}">
                <input type="hidden" name="domain.id" value="${command?.domain?.id}" />
            </g:if>

            <twbs:input name="domain.name" bean="${command?.domain}" labelText="Fag kode" />
            <twbs:input name="domain.fullName" bean="${command?.domain}" labelText="Navn" />
            <twbs:textArea name="domain.description" bean="${command?.domain}" labelText="Beskrivelse" rows="10" />
            <twbs:input name="domain.year" bean="${command?.domain}" labelText="Semester (År)">
                <g:content key="addon-left">
                    <twbs:inputGroupAddon>
                        <twbs:checkbox name="domain.spring" bean="${command?.domain}"
                                       labelText="Forår" />
                    </twbs:inputGroupAddon>
                </g:content>
            </twbs:input>
            <twbs:checkbox labelText="Synligt for studerende" name="visible"
                           value="${command?.domain?.localStatus == NodeStatus.VISIBLE}" />

            <twbs:button type="submit" style="${ButtonStyle.PRIMARY}" block="true">
                <g:if test="${isEditing}">
                    <fa:icon icon="${FaIcon.EDIT}" />
                    Gem ændringer
                </g:if>
                <g:else>
                    <fa:icon icon="${FaIcon.PLUS}" />
                    Opret nyt kursus
                </g:else>

            </twbs:button>
        </twbs:form>
    </twbs:column>
</twbs:row>
</body>
</html>