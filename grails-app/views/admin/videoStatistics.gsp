<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="admin">
    <title>Test Title</title>
</head>

<body>

<twbs:row>
    <twbs:column cols="8">
        <h1 class="page-header">Video Statistik</h1>
        <g:each in="${events}" var="event">
            ${event.course} - ${event.user?.username} <br>
        </g:each>
    </twbs:column>
    <twbs:column cols="4">
        <p>Sidebar til opsummering</p>
    </twbs:column>
</twbs:row>

</body>
</html>