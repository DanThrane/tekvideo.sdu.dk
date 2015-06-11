<%@ page import="dk.danthrane.twbs.NavBarPlacement" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Admin | <g:layoutTitle/></title>
    <asset:stylesheet src="application.css"/>
    <asset:stylesheet src="admin.css"/>
    <asset:javascript src="application.js"/>
    <script src="//cdnjs.cloudflare.com/ajax/libs/Chart.js/1.0.2/Chart.min.js"></script>
    <g:layoutHead/>
</head>

<body>

<twbs:navbar placement="${NavBarPlacement.FIXED_TO_TOP}" inverse="true">
    <g:content key="navbar-brand">
        Admin Panel
    </g:content>
</twbs:navbar>


<twbs:container fluid="true">
    <twbs:row>
        <twbs:column cols="2" class="sidebar">
            <twbs:nav class="nav-sidebar">
                <twbs:navItem active="${active == "home"}" controller="admin" action="index">
                    Hjem
                </twbs:navItem>
                <twbs:navItem active="${active == summary}" controller="admin" action="videoSummary">
                    Opsummering
                </twbs:navItem>
            </twbs:nav>
        </twbs:column>
        <twbs:column cols="10" offset="2" class="main">
            <g:layoutBody/>
        </twbs:column>
    </twbs:row>
</twbs:container>
</body>
</html>