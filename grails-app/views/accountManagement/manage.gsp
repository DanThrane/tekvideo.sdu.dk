<%@ page import="dk.sdu.tekvideo.FaIcon; dk.danthrane.twbs.Icon" %>
<%@ page import="dk.danthrane.twbs.ButtonStyle; dk.danthrane.twbs.ButtonSize" %>
<html>
<head>
    <meta name="layout" content="main_fluid">
    <title>Kontoadministration</title>
</head>

<body>

<twbs:pageHeader>
    <h3>Kontoadministration</h3>
</twbs:pageHeader>

<twbs:table responsive="true" hover="true">
    <thead>
    <twbs:th>Brugernavn</twbs:th>
    <twbs:th>Email</twbs:th>
    <twbs:th>E-learn ID</twbs:th>
    </thead>
    <tbody>
    <g:each in="${users}" var="user">
        <twbs:tr>
            <twbs:td>${user.username}</twbs:td>
            <twbs:td>${user.email}</twbs:td>
            <twbs:td>${user.elearnId}</twbs:td>
        </twbs:tr>
    </g:each>
    </tbody>
</twbs:table>

</body>
</html>