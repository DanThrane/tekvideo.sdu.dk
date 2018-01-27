<%@ page import="dk.danthrane.twbs.ContextualColor; dk.danthrane.twbs.Validation" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="twbsmain"/>
    <title>TWBS Demo - Progress Bars</title>
</head>

<body>
<h1>Progress Bar Demo</h1>
<twbs:row>
    <twbs:column>
        <h5>Basic</h5>
        <twbs:progressBar value="10" />
        <h5>With label</h5>
        <twbs:progressBar value="20" showLabel="true" />
        <h5>Contextual</h5>
        <g:each in="${ContextualColor.values()}" var="c" status="i">
            <twbs:progressBar context="${c}" value="30" />
        </g:each>
        <h5>Striped</h5>
        <twbs:progressBar striped="true" value="50" />
        <h5>Animated</h5>
        <twbs:progressBar animated="true" value="75" />
        <h5>Stacked</h5>
        <twbs:progressStack id="testing">
            <twbs:progressItem animated="true" value="100" min="10" max="1000" showLabel="true" />
            <twbs:progressItem striped="true" value="30" context="${ContextualColor.DANGER}" />
            <twbs:progressItem value="40" context="${ContextualColor.WARNING}" showLabel="true" />
            <twbs:progressItem striped="true" value="10" context="${ContextualColor.SUCCESS}" />
        </twbs:progressStack>

        <h5>Comparison with Raw HTML</h5>
        <div class="progress">
            <div class="progress-bar" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width: 60%;">
                60%
            </div>
        </div>
    </twbs:column>
</twbs:row>

</body>
</html>