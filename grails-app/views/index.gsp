<%@ page import="dk.danthrane.twbs.ButtonTagLib.ButtonStyle; dk.danthrane.twbs.ButtonTagLib.ButtonSize" %>
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
            <twbs:linkButton controller="register" action="index" btnstyle="${ButtonStyle.PRIMARY}" size="${ButtonSize.LARGE}">
                Ny bruger &raquo;
            </twbs:linkButton>
            <twbs:linkButton controller="login" action="index" btnstyle="${ButtonStyle.PRIMARY}" size="${ButtonSize.LARGE}">
                Log ind &raquo;
            </twbs:linkButton>
        </p>
    </div>
</div>

<!-- Additional info here -->
<twbs:container class="marketing">
    <twbs:row>
        <twbs:column cols="4" type="lg">
            <img src="http://lorempixel.com/200/200/people/" class="img-circle" alt="Generic placeholder image" width="140" height="140" />
            <h2>Header</h2>
            <p>Message</p>
        </twbs:column>
        <twbs:column cols="4" type="lg">
            <img src="http://lorempixel.com/200/200/city/" class="img-circle" alt="Generic placeholder image" width="140" height="140" />
            <h2>Header</h2>
            <p>Message</p>
        </twbs:column>
        <twbs:column cols="4" type="lg">
            <img src="http://lorempixel.com/200/200/nature/" class="img-circle" alt="Generic placeholder image" width="140" height="140" />
            <h2>Header</h2>
            <p>Message</p>
        </twbs:column>
    </twbs:row>
</twbs:container>
</body>
</html>
