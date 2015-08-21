<%@ page import="dk.danthrane.twbs.ButtonStyle" %>
<html>

<head>
    <title>Log ind</title>
    <meta name='layout' content="main"/>
</head>

<body>

<twbs:row>
    <twbs:form action="${postUrl}" method="POST" id="loginForm" name="loginForm" autocomplete="off" class="form-signin">
        <twbs:pageHeader>
            <h1>Log ind</h1>
        </twbs:pageHeader>
        <twbs:input name="j_username" labelText="Brugernavn" />
        <twbs:input name="j_password" type="password" labelText="Kodeord" />
        <twbs:checkbox name="rememberMeParameter" id="remember_me" labelText="Forbliv logget ind" />
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
</twbs:row>

</body>
</html>
