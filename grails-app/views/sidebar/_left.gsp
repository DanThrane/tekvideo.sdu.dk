<%@ page import="dk.danthrane.twbs.ButtonStyle; dk.sdu.tekvideo.FaIcon" %>
<twbs:pageHeader><h6>Mine fag</h6></twbs:pageHeader>
<g:if test="${courses.empty}">
    Du er ikke tilmeldt nogle fag! Hvorfor <g:link controller="course" action="list">tilmelder du dig ikke et?</g:link>
</g:if>
<g:else>
    <ul>
        <g:each in="${courses}" var="course">
            <li>
                <sdu:linkToCourse course="${course}">
                    ${course.fullName} (${course.name})
                </sdu:linkToCourse>
            </li>
        </g:each>
    </ul>
</g:else>
<twbs:pageHeader><h6>Om TekVideo</h6></twbs:pageHeader>
<twbs:linkButton url="https://github.com/DanThrane/tekvideo.sdu.dk" style="${ButtonStyle.LINK}" block="true">
    <fa:icon icon="${FaIcon.GITHUB}"/>
    GitHub
</twbs:linkButton>
<twbs:linkButton controller="about" style="${ButtonStyle.LINK}" block="true">
    <fa:icon icon="${FaIcon.USERS}"/>
    Om TekVideo
</twbs:linkButton>