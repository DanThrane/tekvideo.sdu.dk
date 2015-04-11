<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main">
    <title>${teacher.user.username}</title>
</head>

<body>

<h1>${teacher.user.username}</h1>

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