<%@ page import="dk.sdu.tekvideo.FaIcon; dk.danthrane.twbs.ButtonStyle" contentType="text/html;charset=UTF-8" %>
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
                    <small>${command.course.fullName} (${command.course.name})</small>
                </g:if>
                <g:else>
                    Nyt kursus
                </g:else>
            </h3>
        </twbs:pageHeader>

        <twbs:form action="${createLink(action: "postCourse")}" method="POST">
            <input type="hidden" name="isEditing" value="${isEditing}" />
            <g:if test="${isEditing}">
                <input type="hidden" name="course.id" value="${command?.course?.id}" />
            </g:if>

            <twbs:input name="course.name" bean="${command?.course}" labelText="Fag kode" />
            <twbs:input name="course.fullName" bean="${command?.course}" labelText="Navn" />
            <twbs:textArea name="course.description" bean="${command?.course}" labelText="Beskrivelse" rows="10" />
            <twbs:input name="course.semester.year" bean="${command?.course?.semester}" labelText="Semester (År)">
                <g:content key="addon-left">
                    <twbs:inputGroupAddon>
                        <twbs:checkbox name="course.semester.spring" bean="${command?.course?.semester}"
                                       labelText="Forår" />
                    </twbs:inputGroupAddon>
                </g:content>
            </twbs:input>

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