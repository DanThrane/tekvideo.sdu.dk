<%@ page import="dk.danthrane.twbs.ButtonStyle" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Nyt emne</title>
    <meta name="layout" content="main" />
</head>

<body>
<twbs:row>
    <twbs:column>
        %{-- TODO Refactor this --}%
        <twbs:pageHeader>
            <h3>
                <g:if test="${isEditing}">
                    Administreing af emne
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

            <twbs:input name="subject.name" bean="${command?.subject}" labelText="Navn" />
            <twbs:textArea name="subject.description" bean="${command?.subject}" labelText="Beskrivelse"
                           rows="10" />

            <sducrud:saveButton />
        </twbs:form>

        <g:if test="${isEditing}">
            <h4>Videoer</h4>

            <g:if test="${!course?.subjects?.videos?.isEmpty()}">
                <g:each in="${course.subjects.videos}" var="video">

                </g:each>
            </g:if>
            <g:else>
                Dette emne har ikke nogle videoer.
            </g:else>
        </g:if>
    </twbs:column>
</twbs:row>
</body>
</html>