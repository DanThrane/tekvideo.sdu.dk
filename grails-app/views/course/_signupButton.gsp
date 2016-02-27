<%@ page import="dk.danthrane.twbs.ButtonSize; dk.sdu.tekvideo.FaIcon; dk.danthrane.twbs.ButtonStyle" %>
<g:if test="${!inCourse}">
    <twbs:linkButton style="${ButtonStyle.SUCCESS}" size="${ButtonSize.SMALL}"
                     class="signup-btn" action="signup" controller="course" id="${course.id}">
        <fa:icon icon="${FaIcon.USER_PLUS}"/> Tilmeld
    </twbs:linkButton>
</g:if>
<g:else>
    <twbs:linkButton style="${ButtonStyle.DANGER}" size="${ButtonSize.SMALL}"
                     class="signup-btn" action="signup" controller="course" id="${course.id}">
        <fa:icon icon="${FaIcon.MINUS}"/> Afmeld
    </twbs:linkButton>
</g:else>

