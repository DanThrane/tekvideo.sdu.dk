<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="admin">
    <title>Test Title</title>
</head>

<body>

<twbs:row>
    <twbs:column cols="8">
        <h1 class="page-header">Didn't crash :-)</h1>
        <g:each in="${summary}" var="videoSummary">
            ${videoSummary}
        </g:each>
    </twbs:column>
    <twbs:column cols="4">
    </twbs:column>
</twbs:row>

</body>
</html>