<%@ page contentType="text/html;charset=UTF-8" %>
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

%{-- TODO Expand navbar tag lib to support this stuff. For now just use Bootstrap directly --}%

<nav class="navbar navbar-inverse navbar-fixed-top">
    <div class="container-fluid">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="#">Admin Panel</a>
        </div>
        %{--<div id="navbar" class="navbar-collapse collapse">--}%
            %{--<ul class="nav navbar-nav navbar-right">--}%
                %{--<li><a href="#">Dashboard</a></li>--}%
                %{--<li><a href="#">Settings</a></li>--}%
                %{--<li><a href="#">Profile</a></li>--}%
                %{--<li><a href="#">Help</a></li>--}%
            %{--</ul>--}%
            %{--<form class="navbar-form navbar-right">--}%
                %{--<input type="text" class="form-control" placeholder="Search...">--}%
            %{--</form>--}%
        %{--</div>--}%
    </div>
</nav>

<twbs:fluidContainer>
    <div class="row">
        <div class="col-sm-3 col-md-2 sidebar">
            <ul class="nav nav-sidebar">
                <li class="navbar-heading">Videoer</li>
                <twbs:navitem active="true"><a href="#">Foo</a></twbs:navitem>
                <twbs:navitem><a href="#">Bar</a></twbs:navitem>
                <twbs:navitem><a href="#">Baz</a></twbs:navitem>
            </ul>
        </div>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <g:layoutBody/>
        </div>
    </div>
</twbs:fluidContainer>
</body>
</html>