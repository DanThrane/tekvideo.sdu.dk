<%@ page import="dk.sdu.tekvideo.twbs.Icon" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main">
    <title>Kurser</title>
</head>

<body>


<twbs:row>
    <twbs:column>
        <ol class="breadcrumb">
            <li><g:link uri="/">Hjem</g:link></li>
            <li class="active">Kurser</li>
        </ol>
    </twbs:column>
</twbs:row>

<h3>Kurser</h3>

<twbs:row>
    <twbs:column>
        <twbs:input name="course" labelText="Søg" disabled="true" />
    </twbs:column>
</twbs:row>

<div id="advanced-search">
    <twbs:row>
        <twbs:column cols="6">
            <twbs:select name="area" labelText="Område" disabled="true" list="${["Område 1", "Område 2"]}" />
            <twbs:select name="semester" labelText="Semester" disabled="true" list="${["Forår", "Efterår"]}" />
        </twbs:column>
        <twbs:column cols="6">
            <twbs:input name="teacher" labelText="Underviser" disabled="true" />
            <twbs:input name="year" labelText="År" disabled="true" value="2015" />
        </twbs:column>
    </twbs:row>
</div>
<twbs:row>
    <twbs:column>
        <twbs:linkButton btnstyle="link" id="show-advanced-search">
            Avanceret søgning
        </twbs:linkButton>
        <div class="pull-right">
            <twbs:linkButton btnstyle="primary" class="disabled">
                <twbs:icon icon="${Icon.SEARCH}" />
                Søg!
            </twbs:linkButton>
        </div>
    </twbs:column>
</twbs:row>

<hr />

<strong>Resultater:</strong>

<g:each in="${courses}" var="course">
    <twbs:row>
        <twbs:column>
            <g:link mapping="teaching" params="${[teacher: course.teacher.user.username, course: course.name]}">
                <b>${course.name} &mdash; ${course.fullName}</b>
            </g:link>
            <p>${course.description}</p>
        </twbs:column>
    </twbs:row>
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