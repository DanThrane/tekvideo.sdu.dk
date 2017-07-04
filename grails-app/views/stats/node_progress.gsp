<g:applyLayout name="stats_layout">
    <html>
    <head>
        <title>Fremskridt</title>
    </head>

    <body>

    <div class="fixed-table" id="demo">
        <header class="fixed-table-header">
            <table class="table table-bordered">
                <thead>
                <twbs:tr>
                    <g:each in="${table.head}" var="th">
                        <twbs:th>${th.title}</twbs:th>
                    </g:each>
                </twbs:tr>
                </thead>
            </table>
        </header>
        <aside class="fixed-table-sidebar">
            <table class="table table-bordered">
                <tbody>
                <g:each in="${table.body}" var="row">
                    <twbs:tr>
                        <twbs:td>${row.user.toString()}</twbs:td>
                    </twbs:tr>
                </g:each>
                </tbody>
            </table>
        </aside>

        <div class="fixed-table-body">
            <table class="table table-bordered">
                <tbody>
                <g:each in="${table.body}" var="row">
                    <twbs:tr>
                        <g:each in="${row.cells}" var="cell" status="idx">
                            <twbs:td class="${cell.status.styleName}">
                                ${cell.score} / ${table.head[idx].maxScore}
                            </twbs:td>
                        </g:each>
                    </twbs:tr>
                </g:each>
                </tbody>
            </table>
        </div>
    </div>

    <g:content key="layout-script">
        <script>
            $(function () {
                var demo, fixedTable;

                fixedTable = function (el) {
                    var $body, $header, $sidebar;
                    var $el = $(el);
                    $body = $el.find('.fixed-table-body');
                    $sidebar = $el.find('.fixed-table-sidebar table');
                    $header = $el.find('.fixed-table-header table');

                    var desiredWidth = $body.parent().width() - $sidebar.width() - 32;
                    $body.width(desiredWidth);
                    $header.parent().width(desiredWidth);

                    $(window).resize(function () {
                        var desiredWidth = $body.parent().width() - $sidebar.width() - 32;
                        $body.width(desiredWidth);
                        $header.parent().width(desiredWidth);
                    });

                    return $($body).scroll(function () {
                        $($sidebar).css('margin-top', -$($body).scrollTop());
                        return $($header).css('margin-left', -$($body).scrollLeft());
                    });
                };

                demo = new fixedTable($('#demo'));

            });
        </script>
    </g:content>

    </body>
    </html>
</g:applyLayout>
