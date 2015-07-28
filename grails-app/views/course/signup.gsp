<%@ page import="dk.danthrane.twbs.Icon" %>
<%@ page import="dk.danthrane.twbs.ButtonStyle; dk.danthrane.twbs.ButtonSize" %>
<html>
<head>
    <meta name="layout" content="main">
    <title>Tilmelding til ${course.fullName} (${course.name})</title>
</head>

<body>

<twbs:row>
    <twbs:column>
        <h3>Tilmelding til ${course.fullName} (${course.name})</h3>
    </twbs:column>
</twbs:row>

<twbs:row>
    <twbs:column cols="3">
        <h5><twbs:icon icon="${Icon.USER}" /> Identitet</h5>
        Du er logget ind som: <b>${student.user.username}</b><br />
        <g:if test="${inCourse}">
            Du er allerede tilmeldt dette kursus!
        </g:if>
    </twbs:column>
    <twbs:column cols="3">
        <h5><twbs:icon icon="${Icon.EDUCATION}" /> Kursus</h5>
        <g:link mapping="teaching" params="${[teacher: course.teacher, course: course]}">
            ${course.fullName} (${course.name})
        </g:link>
    </twbs:column>
    <twbs:column cols="3">
        <h5><twbs:icon icon="${Icon.TIME}" /> Semester/år</h5>
        <sdu:semesterString semester="${course.semester}" />
    </twbs:column>
    <twbs:column cols="3">
        <h5><twbs:icon icon="${Icon.SIGNAL}" /> Tilmeldte</h5>
        Der er ${studentCount} tilmeldt faget.
    </twbs:column>
</twbs:row>

<br /> <!-- Oh, how I love to style using br tags -->

<twbs:row>
    <twbs:column>
        <h5>Beskrivelse</h5>
        <p>${course.description}</p>
    </twbs:column>
</twbs:row>

<twbs:row>
    <twbs:column>
        <div class="pull-right">
            <g:if test="${!inCourse}">
                <twbs:linkButton style="${ButtonStyle.DANGER}" controller="course" action="list">
                    <twbs:icon icon="${Icon.REMOVE}" />
                    Afvis
                </twbs:linkButton>
                <twbs:linkButton style="${ButtonStyle.SUCCESS}" controller="course" action="completeSignup" id="${course.id}">
                    <twbs:icon icon="${Icon.OK}" />
                    Godkend tilmelding
                </twbs:linkButton>
            </g:if>
            <g:else>
                <twbs:linkButton style="${ButtonStyle.DANGER}"  controller="course" action="completeSignoff" id="${course.id}">
                    <twbs:icon icon="${Icon.TRASH}" />
                    Afmeld kurset
                </twbs:linkButton>
            </g:else>
        </div>
    </twbs:column>
</twbs:row>

</body>
</html>