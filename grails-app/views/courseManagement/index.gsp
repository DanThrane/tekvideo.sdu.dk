<%@ page import="dk.sdu.tekvideo.NodeStatus; dk.danthrane.twbs.ButtonSize; dk.danthrane.twbs.ButtonStyle; dk.sdu.tekvideo.FaIcon" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Mine kurser</title>
    <meta name="layout" content="main_fluid"/>
    <sdu:requireAjaxAssets/>
</head>

<body>

<twbs:pageHeader><h3>Mine kurser</h3></twbs:pageHeader>

<g:if test="${activeCourses.isEmpty()}">
    <p>Du har ingen aktive kurser!</p>
</g:if>
<g:else>
    <div class="course-container">
        <g:each in="${activeCourses}" var="course">
            <sdu:card class="course">
                <div data-subject-id="${course.id}" class="hide"></div>
                <twbs:row>
                    <twbs:column cols="8">
                        <fa:icon icon="${(course.localStatus == NodeStatus.VISIBLE) ?
                                FaIcon.EYE : FaIcon.EYE_SLASH}" />
                        ${course.fullName} (${course.name})
                    </twbs:column>
                    <twbs:column cols="4" class="align-right">
                        <twbs:buttonToolbar>
                            <twbs:linkButton action="manage" id="${course.id}"
                                             style="${ButtonStyle.LINK}" size="${ButtonSize.SMALL}">
                                <fa:icon icon="${FaIcon.EDIT}"/>
                            </twbs:linkButton>
                            <twbs:modalButton target="#course-delete-modal" data-id="${course.id}"
                                              style="${ButtonStyle.DANGER}" class="course-delete">
                                <fa:icon icon="${FaIcon.TRASH}"/>
                            </twbs:modalButton>
                        </twbs:buttonToolbar>
                    </twbs:column>
                </twbs:row>
                <hr>
                <twbs:row>
                    <twbs:column>
                        <markdown:renderHtml><sdu:abbreviate>${course.description}</sdu:abbreviate></markdown:renderHtml>
                    </twbs:column>
                </twbs:row>
            </sdu:card>
        </g:each>
    </div>
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

<g:content key="sidebar-right">
    <div class="sidebar-options-no-header">
        <twbs:linkButton action="createCourse" style="${ButtonStyle.LINK}" block="true">
            <fa:icon icon="${FaIcon.PLUS_CIRCLE}"/>
            Opret nyt kursus
        </twbs:linkButton>
        <twbs:linkButton action="importCourse" style="${ButtonStyle.LINK}" block="true">
            <fa:icon icon="${FaIcon.UPLOAD}"/>
            Importer kursus
        </twbs:linkButton>
    </div>
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
                        $($("[data-id=" + courseToDelete + "]").closest(".course")[0]).fadeOut(function() {
                            $(this).remove();
                        });
                    }
                }
        );
    });
</script>

</body>
</html>