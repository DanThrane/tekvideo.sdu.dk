<g:applyLayout name="stats_layout">
    <html>
    <head>
        <title>Fremskridt for ${student.user.username} i ${node.name}</title>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.2.1/Chart.bundle.min.js"></script>
    </head>

    <body>

    <canvas id="chart"
            style="width: 100%; height: 500px">
    </canvas>

    <g:content key="layout-script">
    </g:content>

    </body>
    </html>
</g:applyLayout>
