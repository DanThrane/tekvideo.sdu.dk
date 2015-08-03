<%@ page import="dk.danthrane.twbs.ButtonStyle; dk.sdu.tekvideo.FaIcon" %>
<g:content key="sidebar-right">
    <div class="sidebar-options-no-header">
        <twbs:linkButton style="${ButtonStyle.LINK}" block="true"
                         controller="course" action="signup" id="${course.id}">
            <fa:icon icon="${FaIcon.USER_PLUS}" />
            Tilmelding/Afmelding
        </twbs:linkButton>
    </div>
</g:content>