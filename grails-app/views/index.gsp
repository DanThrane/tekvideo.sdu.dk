<%@ page import="dk.danthrane.twbs.GridSize; dk.danthrane.twbs.ButtonStyle; dk.danthrane.twbs.ButtonSize" %>

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
        <twbs:row>
            <twbs:column cols="3">
                <ul>
                    <li><strong>Username:</strong> Student</li>
                    <li><strong>Password:</strong> password</li>
                </ul>
            </twbs:column>
            <twbs:column cols="3">
                <ul>
                    <li><strong>Username:</strong> Teacher</li>
                    <li><strong>Password:</strong> password</li>
                </ul>
            </twbs:column>
        </twbs:row>
        <p>
            <twbs:linkButton controller="register" action="index" style="${ButtonStyle.PRIMARY}" size="${ButtonSize.LARGE}">
                Ny bruger &raquo;
            </twbs:linkButton>
            <twbs:linkButton controller="login" action="index" style="${ButtonStyle.PRIMARY}" size="${ButtonSize.LARGE}">
                Log ind &raquo;
            </twbs:linkButton>
        </p>
    </div>
</div>

<!-- Additional info here -->
<twbs:container class="marketing">
    <twbs:row>
        <twbs:column cols="4" type="${GridSize.LG}">
            <img src="http://lorempixel.com/200/200/people/" class="img-circle" alt="Generic placeholder image" width="140" height="140" />
            <h2>Header</h2>
            <p>Message</p>
        </twbs:column>
        <twbs:column cols="4" type="${GridSize.LG}">
            <img src="http://lorempixel.com/200/200/city/" class="img-circle" alt="Generic placeholder image" width="140" height="140" />
            <h2>Header</h2>
            <p>Message</p>
        </twbs:column>
        <twbs:column cols="4" type="${GridSize.LG}">
            <img src="http://lorempixel.com/200/200/nature/" class="img-circle" alt="Generic placeholder image" width="140" height="140" />
            <h2>Header</h2>
            <p>Message</p>
        </twbs:column>
    </twbs:row>
</twbs:container>
</body>
</html>
