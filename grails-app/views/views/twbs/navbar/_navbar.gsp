<nav class="navbar ${navType} ${navPlacement}">
    <div class="container-fluid">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar-collapse">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <g:link uri="/" class="navbar-brand">
                <g:selectContent key="navbar-brand" />
            </g:link>
        </div>

        <div class="collapse navbar-collapse" id="navbar-collapse">
            ${raw(body())}
        </div>

    </div>
</nav>