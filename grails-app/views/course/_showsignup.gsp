<%@ page import="dk.danthrane.twbs.Icon; dk.danthrane.twbs.ButtonStyle" %>
<g:if test="${!inCourse}">
    <twbs:linkButton style="${ButtonStyle.SUCCESS}" controller="course" action="completeSignup" id="${course.id}">
        <fa:icon icon="${dk.sdu.tekvideo.FaIcon.USER_PLUS}" />
        Tilmeld
    </twbs:linkButton>
</g:if>
<g:else>
    <twbs:linkButton style="${ButtonStyle.DANGER}"  controller="course" action="completeSignoff" id="${course.id}">
        Afmeld
    </twbs:linkButton>
</g:else>