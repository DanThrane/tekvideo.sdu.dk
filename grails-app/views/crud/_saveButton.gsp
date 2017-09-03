<%@ page import="dk.sdu.tekvideo.FaIcon; dk.danthrane.twbs.ButtonStyle" %>
<twbs:button type="submit" style="${ButtonStyle.PRIMARY}">
    <g:if test="${isEditing}">
        <fa:icon icon="${FaIcon.EDIT}" />
        Gem ændringer
    </g:if>
    <g:else>
        <fa:icon icon="${FaIcon.PLUS}" />
        Opret
    </g:else>
</twbs:button>