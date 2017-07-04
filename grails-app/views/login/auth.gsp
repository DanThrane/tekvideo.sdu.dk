<%@ page import="dk.danthrane.twbs.ButtonStyle" %>
<html>

<head>
    <title>Log ind</title>
    <meta name='layout' content="main_fluid"/>
</head>

<body>

<twbs:row>
    <twbs:column>
        <twbs:pageHeader>
            <h3>Log ind</h3>
        </twbs:pageHeader>
    </twbs:column>
</twbs:row>

<twbs:row>
    <twbs:column md="4">
        <i>Med en TekVideo bruger:</i>
        <twbs:form action="${postUrl}" method="POST" id="loginForm" name="loginForm" autocomplete="off">
            <twbs:input name="username" labelText="Brugernavn" />
            <twbs:input name="password" type="password" labelText="Kodeord" />
            <twbs:checkbox name="rememberMeParameter" id="remember_me" labelText="Forbliv logget ind" />

            <twbs:linkButton controller="register" action="forgotPassword" style="${ButtonStyle.LINK}" block="true">
                Glemt kodeord?
            </twbs:linkButton>
            <twbs:row>
                <twbs:column cols="6">
                    <twbs:linkButton elementId="register" controller="register" style="${ButtonStyle.LINK}" block="true">
                        Ny bruger?
                    </twbs:linkButton>
                </twbs:column>
                <twbs:column cols="6">
                    <twbs:button type="submit" style="${ButtonStyle.PRIMARY}" block="true">
                        Log ind
                    </twbs:button>
                </twbs:column>
            </twbs:row>
        </twbs:form>
    </twbs:column>
    <twbs:column md="4">
        <i>Eller ved hjælp af:</i>
        <twbs:linkButton controller="sso" action="index" style="${ButtonStyle.DEFAULT}" block="true">
            <asset:image src="sdu_branch.png" class="sdu-logo-button" /> SDU SSO
        </twbs:linkButton>
        <div style="height: 20px;"></div>
    </twbs:column>
</twbs:row>

</body>
</html>
