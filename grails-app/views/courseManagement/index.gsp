<%@ page import="dk.danthrane.twbs.ButtonStyle; dk.sdu.tekvideo.FaIcon" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Mine kurser</title>
    <meta name="layout" content="main_fluid" />
</head>

<body>

<twbs:row>
    <twbs:column>
        <twbs:pageHeader><h3>Mine kurser</h3></twbs:pageHeader>

        <twbs:row>
            <twbs:column cols="6">
                <h4><fa:icon icon="${FaIcon.CALENDAR}" /> Aktive fag</h4>
                <g:if test="${activeCourses.isEmpty()}">
                    Du har ingen aktive kurser!
                </g:if>
                <g:else>
                    <ul>
                        <g:each in="${activeCourses}" var="course">
                            <li><g:link action="manage" id="${course.id}">${course.fullName} (${course.name})</g:link></li>
                        </g:each>
                    </ul>
                </g:else>
            </twbs:column>
            <twbs:column cols="6">
                <h4> <fa:icon icon="${FaIcon.FIRE}" /> Populære videoer</h4>
                <ol>
                    <li><a href="#">Video 1</a></li>
                    <li><a href="#">Video 2</a></li>
                    <li><a href="#">Video 3</a></li>
                    <li><a href="#">Video 4</a></li>
                    <li><a href="#">Video 5</a></li>
                </ol>
            </twbs:column>
        </twbs:row>
    </twbs:column>
</twbs:row>

<g:content key="sidebar-right">
    <div class="sidebar-options-no-header">
        <twbs:linkButton action="createCourse" style="${ButtonStyle.LINK}" block="true">
            <fa:icon icon="${FaIcon.PLUS_CIRCLE}" />
            Opret nyt kursus
        </twbs:linkButton>
    </div>
</g:content>

</body>
</html>