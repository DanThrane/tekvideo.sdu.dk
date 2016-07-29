<%@ page import="dk.sdu.tekvideo.NodeStatus; dk.danthrane.twbs.ButtonSize; dk.danthrane.twbs.ButtonStyle; dk.sdu.tekvideo.FaIcon" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Konto indstillinger</title>
    <meta name="layout" content="main_fluid"/>
    <sdu:requireAjaxAssets/>
</head>

<body>
<twbs:pageHeader><h3>Konto indstillinger</h3></twbs:pageHeader>
%{-- TODO We probably want this page to be more informative --}%
<twbs:row>
    <twbs:column md="6">
        <twbs:pageHeader><h5>e-learn</h5></twbs:pageHeader>
        <twbs:form action="${createLink(action: "updateElearn")}" inline="true" method="post">
            <twbs:input name="elearn" value="${user.elearnId}" labelText="Blackboard ID (e-learn)"/>
            <twbs:button type="submit" style="${ButtonStyle.PRIMARY}">
                <fa:icon icon="${FaIcon.SAVE}"/>
            </twbs:button>
        </twbs:form>

        <twbs:pageHeader><h5>Kodeord</h5></twbs:pageHeader>
        <twbs:form action="${createLink(action: "updatePassword")}" method="post">
            <twbs:input type="password" name="currentPassword" labelText="Nuværende kodeord"/>
            <twbs:input type="password" name="newPassword" labelText="Nyt kodeord"/>
            <twbs:input type="password" name="newPassword2" labelText="Nyt kodeord (igen)"/>
            <twbs:button type="submit" style="${ButtonStyle.PRIMARY}">
                <fa:icon icon="${FaIcon.SAVE}"/>
                Opdater kodeord
            </twbs:button>
        </twbs:form>
    </twbs:column>
</twbs:row>

</body>
</html>
