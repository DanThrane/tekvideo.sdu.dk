<%@ page import="dk.sdu.tekvideo.FaIcon; dk.danthrane.twbs.Icon" %>
<%@ page import="dk.danthrane.twbs.ButtonStyle; dk.danthrane.twbs.ButtonSize" %>
<html>
<head>
    <meta name="layout" content="main_fluid">
    <title>Kurser</title>
</head>

<body>


<twbs:pageHeader>
    <h3>Kurser</h3>
</twbs:pageHeader>

<twbs:row>
    <twbs:column>
        <ol class="breadcrumb">
            <li><g:link uri="/">Hjem</g:link></li>
            <li class="active">Kurser</li>
        </ol>
    </twbs:column>
</twbs:row>

<g:each in="${courses}" var="course">
    <sdu:linkCard mapping="teaching" params="${[teacher: course.teacher, course: course.name]}">
        <a href="#">${course.name} &mdash; ${course.fullName}</a>
        <markdown:renderHtml>${course.description}</markdown:renderHtml>
    </sdu:linkCard>
</g:each>

<script type="text/javascript">
    $(function() {
        $("#advanced-search").hide();
        $("#show-advanced-search").click(function(e) {
            e.preventDefault();
            $("#advanced-search").slideToggle();
        });
    });
</script>

</body>
</html>