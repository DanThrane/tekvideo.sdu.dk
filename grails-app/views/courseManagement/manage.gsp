<%@ page import="dk.sdu.tekvideo.NodeStatus; dk.danthrane.twbs.ButtonSize; dk.danthrane.twbs.ButtonStyle; dk.sdu.tekvideo.FaIcon" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Administrering af ${course.fullName} (${course.name})</title>
    <meta name="layout" content="main_fluid"/>
    <asset:javascript src="list.js"/>
    <sdu:requireAjaxAssets/>
</head>

<body>
<twbs:pageHeader><h3>Administrering af ${course.fullName} (${course.name})</h3></twbs:pageHeader>

<twbs:row>
    <twbs:column>
        <h4>Emner</h4>
        <g:if test="${course.subjects.isEmpty()}">
            <p>Kurset har ikke nogen emner</p>
        </g:if>
        <g:else>
            <div class="subject-container">
                <g:each in="${course.activeSubjects}" var="subject">
                    <sdu:card class="subject">
                        <div data-subject-id="${subject.id}" class="hide"></div>
                        <twbs:row>
                            <twbs:column cols="8">
                                <fa:icon icon="${(subject.localStatus == NodeStatus.VISIBLE) ?
                                        FaIcon.EYE : FaIcon.EYE_SLASH}" />
                                ${subject.name}
                            </twbs:column>
                            <twbs:column cols="4" class="align-right">
                                <twbs:buttonToolbar>
                                    <twbs:linkButton action="editSubject" id="${subject.id}"
                                                     style="${ButtonStyle.LINK}" size="${ButtonSize.SMALL}">
                                        <fa:icon icon="${FaIcon.EDIT}"/>
                                    </twbs:linkButton>
                                    <twbs:modalButton target="#subject-delete-modal" data-id="${subject.id}"
                                                      style="${ButtonStyle.DANGER}" class="subject-delete">
                                        <fa:icon icon="${FaIcon.TRASH}"/>
                                    </twbs:modalButton>
                                    <twbs:button style="${ButtonStyle.SUCCESS}" class="subject-up">
                                        <fa:icon icon="${FaIcon.ARROW_UP}"/>
                                    </twbs:button>
                                    <twbs:button style="${ButtonStyle.INFO}" class="subject-down">
                                        <fa:icon icon="${FaIcon.ARROW_DOWN}"/>
                                    </twbs:button>
                                </twbs:buttonToolbar>
                            </twbs:column>
                        </twbs:row>
                        <hr>
                        <twbs:row>
                            <twbs:column>
                                <markdown:renderHtml><sdu:abbreviate>${subject.description}</sdu:abbreviate></markdown:renderHtml>
                            </twbs:column>
                        </twbs:row>
                    </sdu:card>
                </g:each>
            </div>
            <sdu:ajaxSubmitButton style="${ButtonStyle.PRIMARY}" id="save-subject-order">
                <fa:icon icon="${FaIcon.EDIT}"/>
                Gem rækkefølge
            </sdu:ajaxSubmitButton>
        </g:else>
    </twbs:column>
</twbs:row>

<twbs:modal id="subject-delete-modal">
    <twbs:modalHeader>Er du sikker?</twbs:modalHeader>
    Dette vil slette emnet fra dette fag!
    <twbs:modalFooter>
        <twbs:button data-dismiss="modal">Annulér</twbs:button>
        <twbs:button style="${ButtonStyle.DANGER}" id="subject-delete-button">Slet emnet</twbs:button>
    </twbs:modalFooter>
</twbs:modal>

<twbs:modal id="subject-share">
    <twbs:modalHeader>Del</twbs:modalHeader>
    <twbs:input value="${createLink([controller: "Course", action: "signup", id: course.id, absolute: true])}"
                name="share-url" class="input-select-all" labelText="URL til tilmeldings siden"/>
    <twbs:input value="${createLink([mapping: "teaching", params: [teacher: course.teacher, course: course],
                                     absolute: true])}"
                name="share-page" class="input-select-all" labelText="URL til fagets side"/>
</twbs:modal>

<g:content key="sidebar-right">
    <div class="sidebar-options-no-header">
        <twbs:linkButton action="editCourse" id="${course.id}" style="${ButtonStyle.LINK}" block="true">
            <fa:icon icon="${FaIcon.EDIT}"/>
            Rediger kursus detaljer
        </twbs:linkButton>
        <twbs:linkButton action="createSubject" id="${course.id}" style="${ButtonStyle.LINK}" block="true">
            <fa:icon icon="${FaIcon.PLUS_CIRCLE}"/>
            Opret nyt emne
        </twbs:linkButton>
        <twbs:linkButton action="createVideo" id="${course.id}" style="${ButtonStyle.LINK}" block="true">
            <fa:icon icon="${FaIcon.PLAY}"/>
            Opret ny video
        </twbs:linkButton>
        <twbs:button id="share-button" style="${ButtonStyle.LINK}" block="true">
            <fa:icon icon="${FaIcon.SHARE}"/>
            Del
        </twbs:button>
    </div>
</g:content>

<script>
    $(function () {
        var listManipulator = new ListManipulator(".subject", ".subject-up", ".subject-down");
        listManipulator.init();

        var subjectToDelete = null;

        $("#subject-delete-modal").on("show.bs.modal", function (e) {
            subjectToDelete = $(e.relatedTarget).data("id");
        });

        $("#subject-delete-button").click(function () {
            $("[data-subject-id=" + subjectToDelete + "]").closest(".subject")[0].remove();
            $("#subject-delete-modal").modal("hide");
        });

        AjaxUtil.registerJSONForm("#save-subject-order", "${createLink(action: "updateSubjects")}", function () {
            var order = listManipulator.map(function (element) {
                return parseInt(element.find("[data-subject-id]").attr("data-subject-id"));
            });

            return {order: order, course: ${course.id}};
        });

        $("#share-button").click(function (e) {
            e.preventDefault();
            $("#subject-share").modal("show");
        });

        $(".input-select-all").click(function () {
            $(this).find("input").select();
        });
    });
</script>
</body>
</html>