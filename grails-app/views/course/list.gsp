<%@ page import="dk.sdu.tekvideo.FaIcon; dk.danthrane.twbs.Icon" %>
<%@ page import="dk.danthrane.twbs.ButtonStyle; dk.danthrane.twbs.ButtonSize" %>
<html>
<head>
    <meta name="layout" content="main_fluid">
    <title>Kurser</title>
</head>

<body>

<twbs:pageHeader>
    <h3>Kurser</h3>
</twbs:pageHeader>

<twbs:row>
    <twbs:column md="8">
        <twbs:row>
            <twbs:column>
                <ol class="breadcrumb">
                    <li><g:link uri="/">Hjem</g:link></li>
                    <li class="active">Kurser</li>
                </ol>
            </twbs:column>
        </twbs:row>

        <g:each in="${courses}" var="course">
            <sdu:card>
                <h5>
                    <sdu:linkToCourse course="${course}">${course.name} &mdash; ${course.fullName}</sdu:linkToCourse>
                        <twbs:button style="${ButtonStyle.SUCCESS}" size="${ButtonSize.SMALL}" class="signup-btn">
                            <fa:icon icon="${FaIcon.USER_PLUS}"/> Tilmeld
                        </twbs:button>
                </h5>
                <markdown:renderHtml>${course.description}</markdown:renderHtml>
            </sdu:card>
        </g:each>
    </twbs:column>
</twbs:row>

<nav>
    <ul class="pagination">
        <li>
            <a href="#" aria-label="Previous">
                <span aria-hidden="true">&laquo;</span>
            </a>
        </li>
        <li><a href="#">1</a></li>
        <li class="active"><a href="#">2</a></li>
        <li><a href="#">3</a></li>
        <li><a href="#">4</a></li>
        <li><a href="#">5</a></li>
        <li>
            <a href="#" aria-label="Next">
                <span aria-hidden="true">&raquo;</span>
            </a>
        </li>
    </ul>
</nav>

</body>
</html>