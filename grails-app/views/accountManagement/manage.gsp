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
    <th>Brugernavn</th>
    <th>Email</th>
    <th>E-learn ID</th>
    </thead>
    <tbody>
    <g:each in="${users}" var="user">
        <tr>
            <td>${user.username}</td>
            <td>${user.email}</td>
            <td>${user.elearnId}</td>
        </tr>
    </g:each>
    </tbody>
</twbs:table>

</body>
</html>