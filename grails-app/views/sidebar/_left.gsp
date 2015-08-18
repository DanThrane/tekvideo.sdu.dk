<%@ page import="dk.danthrane.twbs.ButtonStyle; dk.sdu.tekvideo.FaIcon" %>
<twbs:pageHeader><h6>Mine fag</h6></twbs:pageHeader>
<g:if test="${courses.empty}">
    Du er ikke tilmeldt nogle fag! Hvorfor <g:link controller="course" action="list">tilmelder du dig ikke et?</g:link>
</g:if>
<g:else>
    <ul>
        <g:each in="${courses}" var="course">
            <li>
                <g:link mapping="teaching" params="${[teacher: course.teacher, course: course]}">
                    ${course.fullName} (${course.name})
                </g:link>
            </li>
        </g:each>
    </ul>
</g:else>
<twbs:pageHeader><h6>Om TekVideo</h6></twbs:pageHeader>
<twbs:linkButton url="https://github.com/DanThrane/interactive-videos-grails" style="${ButtonStyle.LINK}" block="true">
    <fa:icon icon="${FaIcon.GITHUB}"/>
    GitHub
</twbs:linkButton>
<twbs:linkButton url="mailto:foo@bar" style="${ButtonStyle.LINK}" block="true">
    <fa:icon icon="${FaIcon.ENVELOPE}"/>
    foo@bar.dk
</twbs:linkButton>
