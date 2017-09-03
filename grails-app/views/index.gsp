<%@ page import="grails.converters.JSON; dk.sdu.tekvideo.FaIcon; dk.danthrane.twbs.GridSize; dk.danthrane.twbs.ButtonStyle; dk.danthrane.twbs.ButtonSize" %>

<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main_fluid"/>
    <title>Hjem</title>

    <g:render template="/polymer/includePolymer" />

    <sdu:appResourceImport href="components/tekvideo-exercise-card.html" />
    <style>
        .polymer {
            font-family: 'Roboto', 'Noto', sans-serif;
            line-height: 1.5;
        }

        tekvideo-exercise-card {
            width: 30%;
            margin: 15px 15px 15px 0px;
        }
    </style>
</head>

<body class="polymer">

<g:if test="${student}">
    <twbs:pageHeader>
        <h3>Mine fag <small>for ${student.user.username}</small></h3>
    </twbs:pageHeader>

    <sdu:card>
        <g:if test="${courses.empty}">
            Du er ikke tilmeldt nogle fag endnu!
        </g:if>
        <g:else>
            <ul class="list-unstyled">
                <g:each in="${courses}" var="course">
                    <li>
                        <sdu:linkToCourse course="${course}">
                            ${course.name} &mdash; ${course.fullName}
                        </sdu:linkToCourse>
                    </li >
                </g:each>
            </ul>
        </g:else>
    </sdu:card>
</g:if>

</body>
</html>

