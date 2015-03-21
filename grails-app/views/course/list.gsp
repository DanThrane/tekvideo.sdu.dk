<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main">
    <title>Fag</title>
</head>

<body>

<g:each in="${courses}" var="course">
    <g:link action="view" id="${course.id}">
        <div class="row">
            <b>${course.name}</b>
            <p>${course.description}</p>
        </div>
    </g:link>
</g:each>

</body>
</html>