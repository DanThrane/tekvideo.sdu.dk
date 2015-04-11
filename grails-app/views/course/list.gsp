<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main">
    <title>Fag</title>
</head>

<body>

<g:each in="${courses}" var="course">
    <g:link mapping="teaching" params="${[teacher: course.teacher.user.username, course: course.name]}">
        <twbs:row>
            <b>${course.name}</b>
            <p>${course.description}</p>
        </twbs:row>
    </g:link>
</g:each>

</body>
</html>