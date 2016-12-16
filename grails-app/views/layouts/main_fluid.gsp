<%@ page import="dk.danthrane.twbs.ButtonStyle; dk.sdu.tekvideo.FaIcon" contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>TekVideo | <g:layoutTitle default="Title"/></title>
    <g:layoutHead/>
    <asset:stylesheet src="application.css"/>
    <asset:javascript src="application.js"/>
    <fa:require/>
    <link rel="shortcut icon" href="${asset.assetPath(src: "favicon.ico")}"/>

    <script>
        $(function () {
            var start = Date.now();
            events.configure("${createLink(controller: "event", action: "register")}");
            events.start();

            events.emit({
                "kind": "VISIT_SITE",
                "url": document.location.href,
                "ua": navigator.userAgent
            }, true);

            window.onbeforeunload = function () {
                events.emit({
                    "kind": "EXIT_SITE",
                    "url": document.location.href,
                    "ua": navigator.userAgent,
                    "time": Date.now() - start
                });
                events.flush(false);
            };
        });
    </script>
</head>

<body>

<twbs:navbar>
    <g:content key="navbar-brand">
        <asset:image src="sdu_branch.png" class="navbar-logo"/>
        Video
    </g:content>
    <twbs:navbarLinks>
        <twbs:navbarLink action="list" controller="course">Kurser</twbs:navbarLink>

        <sec:ifAllGranted roles="ROLE_TEACHER">
            <twbs:navbarLink controller="courseManagement">Kursusadministration</twbs:navbarLink>
            <twbs:navbarLink controller="accountManagement" action="manage">Kontoadministration</twbs:navbarLink>
            <twbs:navbarLink controller="dashboard">Video Statistikker</twbs:navbarLink>
        </sec:ifAllGranted>
    </twbs:navbarLinks>
    <twbs:navbarPullRight>
        <twbs:navbarLinks>
            <sec:ifNotLoggedIn>
                <twbs:navbarLink method="POST" controller="login" action="">Log ind</twbs:navbarLink>
            </sec:ifNotLoggedIn>
            <sec:ifLoggedIn>
                <twbs:navDropdownToggle>
                    <sdu:username/>
                    <avatar:gravatar email="${sdu.userEmail()}" cssClass="img-rounded"
                                     defaultGravatarUrl="http://www.gravatar.com/avatar/?d=identicon"/>
                    <twbs:dropdownMenu>
                        <twbs:dropdownItem controller="AccountManagement" action="index">
                            <fa:icon icon="${FaIcon.WRENCH}"/>
                            Konto indstillinger
                        </twbs:dropdownItem>
                        <twbs:dropdownDivider/>
                        <twbs:dropdownItem method="POST" controller="logout" action="">Log ud</twbs:dropdownItem>
                    </twbs:dropdownMenu>
                </twbs:navDropdownToggle>
            </sec:ifLoggedIn>
        </twbs:navbarLinks>
    </twbs:navbarPullRight>
</twbs:navbar>

<twbs:container fluid="true">
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
    <sec:ifNotLoggedIn>
        <g:link controller="cas" action="index">
            <twbs:alert type="info" dismissible="false">
                Klik her for at logge ind via SDU SSO.
            </twbs:alert>
        </g:link>
    </sec:ifNotLoggedIn>
</twbs:container>

<div class="flex-grid">
    <div class="maincontent flex-col">
        <g:layoutBody/>
    </div>

    <g:ifContentAvailable key="sidebar-left">
        <div class="flex-col left">
            <g:selectContent key="sidebar-left"/>
            <g:content key="sidebar-left"/>
        </div>
    </g:ifContentAvailable>

    <g:ifContentAvailable key="sidebar-right">
        <div class="flex-col right">
            <g:selectContent key="sidebar-right"/>
            <g:content key="sidebar-right"/>
        </div>
    </g:ifContentAvailable>
</div>

<g:ifContentAvailable key="content-below-the-fold">
    <div class="flex-grid">
        <div class="maincontent flex-col">
            <g:selectContent key="content-below-the-fold"/>
        </div>

        <g:ifContentAvailable key="sidebar-left">
            <g:ifContentNotAvailable key="sidebar-left-below-the-fold">
                <div class="left flex-col"></div>
            </g:ifContentNotAvailable>
        </g:ifContentAvailable>
        <g:ifContentAvailable key="sidebar-left-below-the-fold">
            <div class="left flex-col">
                <g:selectContent key="sidebar-left-below-the-fold"/>
            </div>
        </g:ifContentAvailable>

        <g:ifContentAvailable key="sidebar-right">
            <g:ifContentNotAvailable key="sidebar-right-below-the-fold">
                <div class="right flex-col"></div>
            </g:ifContentNotAvailable>
        </g:ifContentAvailable>
        <g:ifContentAvailable key="sidebar-right-below-the-fold">
            <div class="right flex-col">
                <g:selectContent key="sidebar-right-below-the-fold"/>
            </div>
        </g:ifContentAvailable>
    </div>
</g:ifContentAvailable>

<twbs:container id="footer">
    <twbs:row>
        <twbs:column cols="3">
            <ul class="list-unstyled">
                <li>
                    <a href="https://github.com/DanThrane/tekvideo.sdu.dk">
                        <fa:icon icon="${FaIcon.GITHUB}"/>
                        GitHub
                    </a>
                </li>
                <li>
                    <g:link controller="about">
                        <fa:icon icon="${FaIcon.USERS}"/>
                        Om TekVideo
                    </g:link>
                </li>
            </ul>
        </twbs:column>
        <twbs:column cols="3">
        </twbs:column>
        <twbs:column cols="6">
            <div class="pull-right">
                <asset:image src="sdu_logo.png"/>
            </div>
        </twbs:column>
    </twbs:row>
</twbs:container>

<g:ifContentAvailable key="layout-script">
    <g:selectContent key="layout-script"/>
</g:ifContentAvailable>
</body>
</html>