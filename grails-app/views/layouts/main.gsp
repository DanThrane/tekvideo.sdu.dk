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
            <twbs:navitem>
                <g:link method="POST" controller='login' action=''>Log ind</g:link>
            </twbs:navitem>
        </sec:ifNotLoggedIn>
        <sec:ifLoggedIn>
            <sec:ifAllGranted roles="ROLE_TEACHER">
                <twbs:navitem>
                    <g:link controller="admin">Admin Panel</g:link>
                </twbs:navitem>
            </sec:ifAllGranted>
            <twbs:navitem>
                <g:link method="POST" controller='logout' action=''>Log ud</g:link>
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
<script>
    $(function() {
        var start = Date.now();
        events.configure("${createLink(controller: "event", action: "register")}");
        events.start();

        events.emit({
            "kind": "VISIT_SITE",
            "url": document.location.href,
            "ua": navigator.userAgent
        }, true);

        window.onbeforeunload = function() {
            events.emit({
                "kind": "EXIT_SITE",
                "url": document.location.href,
                "time": Date.now() - start
            });
            events.flush(false);
        };
    });
</script>
</body>
</html>