<html>

<head>
    <title>Glemt kodeord</title>
    <meta name="layout" content="main"/>
</head>

<body>
<div class="form-group">
    <twbs:container>
        <twbs:row>
            <twbs:column cols="8" offset="2">
                <twbs:pageHeader><h2>Glemt kodeord</h2></twbs:pageHeader>
                <g:form action='forgotPassword' name="forgotPasswordForm" autocomplete='off'>
                    <g:if test='${emailSent}'>
                        <g:message code='spring.security.ui.forgotPassword.sent'/>
                    </g:if>

                    <g:else>
                        <twbs:input labelText="Brugernavn" name="username">
                            Indtast dit brugernavn, og vi vil sende dig en mail til den tilknyttet e-mail
                        </twbs:input>

                        <s2ui:submitButton elementId='reset' form='forgotPasswordForm' class="btn btn-primary"
                                           messageCode='Send anmodning'/>
                    </g:else>

                </g:form>
            </twbs:column>
        </twbs:row>
    </twbs:container>
</div>

<script>
    $(document).ready(function () {
        $('#username').focus();
    });
</script>

</body>
</html>