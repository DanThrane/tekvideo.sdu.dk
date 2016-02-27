<%@ page import="dk.danthrane.twbs.NavStyle; dk.sdu.tekvideo.NodeStatus; dk.danthrane.twbs.ButtonSize; dk.danthrane.twbs.ButtonStyle; dk.sdu.tekvideo.FaIcon" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Mine kurser</title>
    <meta name="layout" content="main_fluid"/>
    <sdu:requireAjaxAssets/>
</head>

<body>

<twbs:pageHeader><h3>Mine kurser</h3></twbs:pageHeader>

<twbs:row>
    <twbs:column>
        <ol class="breadcrumb">
            <li class="active">Mine Kurser</li>
        </ol>
    </twbs:column>
</twbs:row>

<sdu:card>
    <g:if test="${activeCourses.isEmpty()}">
        <p>Du har ingen aktive kurser!</p>
    </g:if>
    <g:else>
        <g:each in="${activeCourses}" var="course">
            <div data-subject-id="${course.id}" class="hide"></div>
            <twbs:row>
                <twbs:column cols="8">
                    <div class="node-header">
                        <g:link action="manage" id="${course.id}">${course.fullName} (${course.name})</g:link>
                        <ul class="list-inline course-list">
                            <!-- I definitely didn't implement the bullets like this because I'm too lazy ;-) -->
                            <li>&raquo; <sdu:semesterString course="${course}" /></li>
                            <li>&raquo; ${course.subjects.size()} emne(r)</li>
                        </ul>
                    </div>
                </twbs:column>
                <twbs:column cols="4" class="align-right">
                    <twbs:buttonGroup>
                        <twbs:dropdownToggle style="${ButtonStyle.WARNING}">
                            <fa:icon icon="${FaIcon.ARROW_CIRCLE_RIGHT}" /> Flyt til

                            <twbs:dropdownMenu>
                                <twbs:dropdownItem disabled="${status == NodeStatus.VISIBLE}"
                                                   action="courseStatus" id="${course.id}"
                                                   params="${[status: NodeStatus.VISIBLE]}">
                                    <fa:icon icon="${FaIcon.EYE}"/> Synlige
                                </twbs:dropdownItem>
                                <twbs:dropdownItem disabled="${status == NodeStatus.INVISIBLE}"
                                                   action="courseStatus" id="${course.id}"
                                                   params="${[status: NodeStatus.INVISIBLE]}">
                                    <fa:icon icon="${FaIcon.EYE_SLASH}"/> Usynlige
                                </twbs:dropdownItem>
                                <twbs:dropdownItem disabled="${status == NodeStatus.TRASH}"
                                                   action="courseStatus" id="${course.id}"
                                                   params="${[status: NodeStatus.TRASH]}">
                                    <fa:icon icon="${FaIcon.TRASH}"/> Papirkurv
                                </twbs:dropdownItem>
                            </twbs:dropdownMenu>
                        </twbs:dropdownToggle>
                    </twbs:buttonGroup>
                    <twbs:buttonGroup>
                        <twbs:dropdownToggle style="${ButtonStyle.SUCCESS}">
                            <fa:icon icon="${FaIcon.PLUS_CIRCLE}" /> Tilføj

                            <twbs:dropdownMenu>
                                <twbs:dropdownItem action="createSubject" id="${course.id}">
                                    <fa:icon icon="${FaIcon.USERS}" /> Emne
                                </twbs:dropdownItem>
                                <twbs:dropdownItem action="createVideo" id="${course.id}">
                                    <fa:icon icon="${FaIcon.PLAY}" /> Video
                                </twbs:dropdownItem>
                            </twbs:dropdownMenu>
                        </twbs:dropdownToggle>
                    </twbs:buttonGroup>
                </twbs:column>
            </twbs:row>
            <twbs:row>
                <twbs:column>
                    <markdown:renderHtml text="${sdu.abbreviate([:], { course.description })}"/>
                </twbs:column>
            </twbs:row>
            <hr>
        </g:each>

        <twbs:modal id="course-delete-modal">
            <twbs:modalHeader>Er du sikker?</twbs:modalHeader>
            Dette vil slette dette fag!
            <twbs:modalFooter>
                <twbs:button data-dismiss="modal">Annulér</twbs:button>
                <sdu:ajaxSubmitButton style="${ButtonStyle.DANGER}" id="subject-delete-button">
                    <fa:icon icon="${FaIcon.TRASH}"/>
                    Slet fag
                </sdu:ajaxSubmitButton>
            </twbs:modalFooter>
        </twbs:modal>
    </g:else>
</sdu:card>


<g:content key="sidebar-right">
    <twbs:pageHeader>
        <h4>Filter</h4>
    </twbs:pageHeader>
    <twbs:nav style="${NavStyle.PILL}" stacked="true">
        <twbs:navItem active="${status == NodeStatus.VISIBLE}" params="${[status: NodeStatus.VISIBLE]}">
            <fa:icon icon="${FaIcon.EYE}"/> Synlige
        </twbs:navItem>
        <twbs:navItem active="${status == NodeStatus.INVISIBLE}" params="${[status: NodeStatus.INVISIBLE]}">
            <fa:icon icon="${FaIcon.EYE_SLASH}"/> Usynlige
        </twbs:navItem>
        <twbs:navItem active="${status == NodeStatus.TRASH}" params="${[status: NodeStatus.TRASH]}">
            <fa:icon icon="${FaIcon.TRASH}"/> Papirkurv
        </twbs:navItem>
    </twbs:nav>

    <twbs:pageHeader>
        <h4>Handlinger</h4>
    </twbs:pageHeader>

    <twbs:linkButton action="createCourse" style="${ButtonStyle.LINK}" block="true">
        <fa:icon icon="${FaIcon.PLUS_CIRCLE}"/>
        Opret nyt kursus
    </twbs:linkButton>
    <twbs:linkButton action="importCourse" style="${ButtonStyle.LINK}" block="true">
        <fa:icon icon="${FaIcon.UPLOAD}"/>
        Importer kursus
    </twbs:linkButton>
</g:content>

<script>
    $(function () {
        var courseToDelete = null;

        $("#course-delete-modal").on("show.bs.modal", function (e) {
            courseToDelete = $(e.relatedTarget).data("id");
        });

        AjaxUtil.registerJSONForm(
                "#subject-delete-button",
                "${createLink(action: "deleteCourse")}",
                function () {
                    return {course: courseToDelete};
                },
                {
                    success: function () {
                        $("#course-delete-modal").modal("hide");
                        $($("[data-id=" + courseToDelete + "]").closest(".course")[0]).fadeOut(function () {
                            $(this).remove();
                        });
                    }
                }
        );
    });
</script>

</body>
</html>