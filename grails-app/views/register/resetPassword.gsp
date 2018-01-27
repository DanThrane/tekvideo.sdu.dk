<html>

<head>
    <title>Glemt kodeord</title>
    <meta name="layout" content="main"/>
</head>

<body>
<twbs:pageHeader><h2>Indtast dit nye kodeord</h2></twbs:pageHeader>
<twbs:form action="${createLink(action: "resetPassword")}" name="resetPasswordForm1" autocomplete="off" method="post">
    <g:hiddenField name='t' value='${token}'/>


    <twbs:input name="password" labelText="Nyt kodeord" bean="${command}" type="password"/>
    <twbs:input name="password2" labelText="Nyt kodeord (igen)" bean="${command}" type="password"/>

    <s2ui:submitButton elementId='reset' form='resetPasswordForm1' class="btn btn-primary"
                       messageCode='Opdater min kode'/>

</twbs:form>
</body>
</html>
