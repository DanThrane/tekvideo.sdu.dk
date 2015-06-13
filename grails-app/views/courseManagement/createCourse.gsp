<%@ page import="dk.sdu.tekvideo.FaIcon; dk.danthrane.twbs.ButtonStyle" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Nyt kursus</title>
    <meta name="layout" content="main">
</head>

<body>
<twbs:row>
    <twbs:column>
        <twbs:pageHeader><h3>Nyt kursus</h3></twbs:pageHeader>
        <twbs:form action="${createLink(action: "postCourse")}" method="POST">
            <twbs:input name="course.name" bean="${command?.course}" labelText="Fag kode" />
            <twbs:input name="course.fullName" bean="${command?.course}" labelText="Navn" />
            <twbs:textArea name="course.description" bean="${command?.course}" labelText="Beskrivelse" />
            <twbs:input name="course.semester.year" bean="${command?.course?.semester}" labelText="År" />
            <twbs:checkbox name="course.semester.spring" bean="${command?.course?.semester}" labelText="Forårs semester" />
            <twbs:button type="submit" style="${ButtonStyle.PRIMARY}" block="true">
                <fa:icon icon="${FaIcon.PLUS}" />
                Opret nyt kursus
            </twbs:button>
        </twbs:form>
    </twbs:column>
</twbs:row>
</body>
</html>