<%@ page import="dk.sdu.tekvideo.twbs.ButtonTagLib" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Front Page</title>
    <asset:javascript src="home.js"/>
</head>
<body>

<!-- Major frontpage picture -->
<div class="jumbotron">
    <div class="container">
        <h1>Velkommen til TekVideo</h1>
        <p>Besked</p>
        <p>Flere beskeder</p>
        <ul>
            <li><strong>Username:</strong> Admin</li>
            <li><strong>Password:</strong> password</li>
        </ul>
        <p>
            <twbs:linkButton controller="register" action="index" btnstyle="primary" size="${ButtonTagLib.ButtonSize.LARGE}">
                Ny bruger &raquo;
            </twbs:linkButton>
            <twbs:linkButton controller="login" action="index" btnstyle="primary" size="${ButtonTagLib.ButtonSize.LARGE}">
                Log ind &raquo;
            </twbs:linkButton>
        </p>
    </div>
</div>

<!-- Additional info here -->
<twbs:container class="marketing">
    <twbs:row>
        <twbs:column cols="4" type="lg">
            <g:img dir="images" file="#" class="img-circle" alt="Generic placeholder image" width="140" height="140"/>
            <h2>Header</h2>
            <p>Message</p>
        </twbs:column>
        <twbs:column cols="4" type="lg">
            <g:img dir="images" file="#" class="img-circle" alt="Generic placeholder image" width="140" height="140"/>
            <h2>Header</h2>
            <p>Message</p>
        </twbs:column>
        <twbs:column cols="4" type="lg">
            <g:img dir="images" file="#" class="img-circle" alt="Generic placeholder image" width="140" height="140"/>
            <h2>Header</h2>
            <p>Message</p>
        </twbs:column>
    </twbs:row>
</twbs:container>
</body>
</html>
