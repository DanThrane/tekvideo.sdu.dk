<%@ page import="dk.danthrane.twbs.ButtonSize; dk.danthrane.twbs.ButtonStyle; dk.sdu.tekvideo.FaIcon" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Mine kurser</title>
    <meta name="layout" content="main_fluid"/>
</head>

<body>

<div class="card-stack" id="stack">
    <div class="card-item active step-1">
        <twbs:pageHeader><h3>Importer kursus <small>1 af 2: Vælg et kursus</small></h3></twbs:pageHeader>

        <g:each in="${courses}" var="course">
            <sdu:card>
                <sdu:linkToCourse course="${course}">
                    ${course.name} &mdash; ${course.fullName}
                </sdu:linkToCourse>
                <hr>
                <markdown:renderHtml>${course.description}</markdown:renderHtml>
                <hr>
                <twbs:row>
                    <twbs:column>
                        <div class="pull-right">
                            <twbs:linkButton url="${sdu.createLinkToCourse(course: course, absolute: true)}"
                                             style="${ButtonStyle.INFO}" size="${ButtonSize.SMALL}">
                                <fa:icon icon="${FaIcon.SEARCH}"/>
                                Mere info
                            </twbs:linkButton>
                            <twbs:button style="${ButtonStyle.PRIMARY}" size="${ButtonSize.SMALL}"
                                         data-id="${course.id}" data-name="${course.name}"
                                         data-full-name="${course.fullName}" class="import-course">
                                <fa:icon icon="${FaIcon.HAND_O_UP}"/>
                                Importér dette kursus
                            </twbs:button>
                        </div>
                    </twbs:column>
                </twbs:row>

            </sdu:card>
        </g:each>
    </div>

    <div class="card-item step-2">
        <twbs:pageHeader><h3>Importer kursus <small>2 af 2: Rediger detaljer</small></h3></twbs:pageHeader>

        <twbs:form method="POST" action="${createLink(action: "submitImportCourse")}">
            <twbs:input name="newCourseName" labelText="Kursus kode" bean="${command}"/>
            <twbs:input name="newCourseFullName" labelText="Kursus navn" bean="${command}"/>
            <twbs:input name="newSemester" labelText="Semester (År)" bean="${command}">
                <g:content key="addon-left">
                    <twbs:inputGroupAddon>
                        <twbs:checkbox name="newSemesterSpring" labelText="Forår" bean="${command}"/>
                    </twbs:inputGroupAddon>
                </g:content>
            </twbs:input>
            <twbs:textArea name="newDescription" bean="${command}" labelText="Beskrivelse" rows="10" />
            <twbs:checkbox labelText="Synligt for studerende" name="visible" value="${command?.visible}" />
            <twbs:input name="course.id" id="course" type="hidden" showLabel="false" bean="${command?.course}" />
            <twbs:linkButton style="${ButtonStyle.INFO}" elementId="reselect-course" url="#">
                <fa:icon icon="${FaIcon.BACKWARD}"/>
                Vælg nyt kursus
            </twbs:linkButton>
            <twbs:button type="submit" style="${ButtonStyle.PRIMARY}">
                <fa:icon icon="${FaIcon.UPLOAD}"/>
                Importer kursus
            </twbs:button>
        </twbs:form>
    </div>
</div>

<script>
    $(function () {
        var stack = new CardStack("#stack");

        <g:if test="${command != null}">
        stack.select(".step-2");
        </g:if>

        $(".import-course").click(function () {
            $("#course").val($(this).data("id"));
            $("#newCourseName").val($(this).data("name"));
            $("#newCourseFullName").val($(this).data("full-name"));
            stack.select(".step-2");
        });

        $("#reselect-course").click(function (e) {
            e.preventDefault();
            stack.select(".step-1");
        });
    });
</script>

</body>
</html>