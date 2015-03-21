<html>

<head>
    <title><g:message code='spring.security.ui.login.title'/></title>
    <meta name='layout' content="main"/>
</head>

<body>

<div class="container">
    <div class="row">
        <form action='${postUrl}' method='POST' id="loginForm" name="loginForm" autocomplete='off' class="form-signin">
            <h2 class="form-signin-heading">Please sign in</h2>
            <label for="inputUsername" class="sr-only"><g:message code='spring.security.ui.login.username'/></label>
            <input id="inputUsername" class="form-control" placeholder="Username" name="j_username" required autofocus>
            <label for="inputPassword" class="sr-only">Password</label>
            <input type="password" id="inputPassword" class="form-control" placeholder="Password" name="j_password" required>
            <div class="checkbox">
                <label>
                    <input type="checkbox" name="${rememberMeParameter}" id="remember_me"> Remember me
                </label>
            </div>
            <div class="row">
                <div class="col-md-6">
                    <s2ui:linkButton elementId='register' controller='register' messageCode='spring.security.ui.login.register' class="btn btn-link btn-block"/>
                </div>
                <div class="col-md-6">
                    <s2ui:submitButton elementId='loginButton' form='loginForm' messageCode='spring.security.ui.login.login' class="btn btn-primary btn-block"/>
                </div>
            </div>
        </form>
    </div>
</div>

<script>
    $(document).ready(function() {
        $('#username').focus();
    });

    <s2ui:initCheckboxes/>
</script>

</body>
</html>
