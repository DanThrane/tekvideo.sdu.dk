<%@ page import="dk.danthrane.twbs.ButtonStyle" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Nyt emne</title>
    <meta name="layout" content="main" />
</head>

<body>
<twbs:row>
    <twbs:column>
        <twbs:form method="POST" action="${createLink(action: "postSubject", id: course.id)}">
            <twbs:input name="subject.name" bean="${command?.subject}" labelText="Navn" />
            <twbs:button type="submit" style="${ButtonStyle.PRIMARY}">
                Lav emne
            </twbs:button>
        </twbs:form>
    </twbs:column>
</twbs:row>
</body>
</html>