<%@ page import="dk.danthrane.twbs.Icon; grails.converters.JSON; java.time.LocalDateTime" contentType="text/html;charset=UTF-8" %>
<%@ page import="dk.danthrane.twbs.ButtonTagLib.ButtonStyle; dk.danthrane.twbs.ButtonTagLib.ButtonSize" %>
<html>
<head>
    <meta name="layout" content="admin">
    <title><g:message code="admin.video.summary.title"/></title>
</head>

<body>

<twbs:row>
    <twbs:column>
        <h1 class="page-header"><g:message code="admin.video.summary.title"/></h1>

        <b>TODO: Form bør kun vise emner der tilhører det valgte fag. Faget bliver ignoreret hvis der er et emne valgt</b>
        <form class="form-inline" id="filter-form">
            <twbs:select list="${courses}" name="course" labelText="Fag" allowEmpty="true"/>
            <twbs:select list="${subjects}" name="subject" labelText="Emner" allowEmpty="true"/>
            <twbs:button btnstyle="${ButtonStyle.PRIMARY}" type="submit">Filter</twbs:button>
            <twbs:icon icon="${Icon.REFRESH}" class="loading-indicator spinning" id="loading"/>
        </form>
        <hr/>

        <table class="table table-hover">
            <thead>
            <tr>
                <th><g:message code="admin.video.summary.thead.name"/></th>
                <th><g:message code="admin.video.summary.thead.releaseDate"/></th>
                <th><g:message code="admin.video.summary.thead.views"/></th>
                <th><g:message code="admin.video.summary.thead.answers"/></th>
                <th><g:message code="admin.video.summary.thead.mostCommonIncorrect"/></th>
                <th></th>
            </tr>
            </thead>
            <tbody id="summary-body">
            <g:render template="summaryRows" model="${[summary: summary]}"/>
            </tbody>
        </table>
    </twbs:column>
</twbs:row>

<script type="text/javascript">
    var $loading = $("#loading");
    var $summary = $("#summary-body");

    $("#filter-form").submit(function(e) {
        e.preventDefault();

        var data = $(this).serialize();
        $loading.show();
        $.post("${createLink(action: "videoSummary")}.json", data).done(function(data) {
            $summary.empty();
            $summary.html(data.html);

            for (var i = 0; i < data.ids.length; i++) {
                createMousePanel("#viewTrigger" + data.ids[i], "#viewPanel" + data.ids[i]);
                createMousePanel("#answerTrigger" + data.ids[i], "#answerPanel" + data.ids[i]);
            }
            $loading.hide();
        });
    });

    <sdu:requireMousePanel/>
    <g:each in="${summary}" var="entry">
    <sdu:initMousePanel triggerId="viewTrigger${entry.key.id}" panelId="viewPanel${entry.key.id}"/>
    <sdu:initMousePanel triggerId="answerTrigger${entry.key.id}" panelId="answerPanel${entry.key.id}"/>
    </g:each>
</script>

</body>
</html>