<!DOCTYPE html>
<html>
<head>
    <title>TekVideo | <g:layoutTitle default="Title"/></title>
    <g:layoutHead/>
    <asset:stylesheet src="application.css"/>
    <asset:javascript src="application.js"/>
</head>
<body>


<twbs:navbar title="TekVideo">
    <twbs:navcontainer>
        <twbs:navitem><g:link action="list" controller="course">Fag</g:link></twbs:navitem>
    </twbs:navcontainer>

    <twbs:navcontainer location="right">
        <sec:ifNotLoggedIn>
            <twbs:navitem active="true">
                <g:link method="POST" controller='login' action=''>Login</g:link>
            </twbs:navitem>
        </sec:ifNotLoggedIn>
        <sec:ifLoggedIn>
            <twbs:navitem>
                <g:link method="POST" controller='logout' action=''>Logout</g:link>
            </twbs:navitem>
        </sec:ifLoggedIn>
    </twbs:navcontainer>
</twbs:navbar>

<twbs:container>
    <g:if test="${flash.error}">
        <twbs:alert type="danger">${flash.error}</twbs:alert>
    </g:if>
    <g:if test="${flash.warning}">
        <twbs:alert type="warning">${flash.warning}</twbs:alert>
    </g:if>
    <g:if test="${flash.success}">
        <twbs:alert type="success">${flash.success}</twbs:alert>
    </g:if>
    <g:if test="${flash.message}">
        <twbs:alert type="info">${flash.message}</twbs:alert>
    </g:if>
    <g:layoutBody/>
</twbs:container>
</body>
</html>