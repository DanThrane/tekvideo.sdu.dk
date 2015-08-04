<%@ page import="dk.danthrane.twbs.ButtonSize; dk.danthrane.twbs.ButtonStyle; dk.sdu.tekvideo.FaIcon" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Administrering af ${course.fullName} (${course.name})</title>
    <meta name="layout" content="main_fluid" />
    <asset:javascript src="list.js" />
    <sdu:requireAjaxAssets />
</head>

<body>
<twbs:row>
    <twbs:column>
        <twbs:pageHeader><h3>Administrering af ${course.fullName} (${course.name})</h3></twbs:pageHeader>

        <twbs:row>
            <twbs:column>
                <h4>Emner</h4>
                <g:if test="${course.subjects.isEmpty()}">
                    <p>Kurset har ikke nogen emner</p>
                </g:if>
                <g:else>
                    <div class="subject-container">
                        <g:each in="${course.subjects}" var="subject">
                            <sdu:card class="subject">
                                <div data-subject-id="${subject.id}" class="hide"></div>
                                <twbs:row>
                                    <twbs:column cols="8">
                                        ${subject.name}
                                    </twbs:column>
                                    <twbs:column cols="4" class="align-right">
                                        <twbs:buttonToolbar>
                                            <twbs:linkButton action="editSubject" id="${subject.id}"
                                                             style="${ButtonStyle.LINK}" size="${ButtonSize.SMALL}">
                                                <fa:icon icon="${FaIcon.EDIT}" />
                                            </twbs:linkButton>
                                            <twbs:button style="${ButtonStyle.DANGER}" class="subject-delete">
                                                <fa:icon icon="${FaIcon.TRASH}" />
                                            </twbs:button>
                                            <twbs:button style="${ButtonStyle.SUCCESS}" class="subject-up">
                                                <fa:icon icon="${FaIcon.ARROW_UP}" />
                                            </twbs:button>
                                            <twbs:button style="${ButtonStyle.INFO}" class="subject-down">
                                                <fa:icon icon="${FaIcon.ARROW_DOWN}" />
                                            </twbs:button>
                                        </twbs:buttonToolbar>
                                    </twbs:column>
                                </twbs:row>
                            </sdu:card>
                        </g:each>
                    </div>
                    <sdu:ajaxSubmitButton style="${ButtonStyle.PRIMARY}" id="save-subject-order">
                        <fa:icon icon="${FaIcon.EDIT}" />
                        Gem rækkefølge
                    </sdu:ajaxSubmitButton>
                </g:else>
            </twbs:column>
        </twbs:row>
    </twbs:column>
</twbs:row>

<g:content key="sidebar-right">
    <div class="sidebar-options-no-header">
        <twbs:linkButton action="editCourse" id="${course.id}" style="${ButtonStyle.LINK}" block="true">
            <fa:icon icon="${FaIcon.EDIT}" />
            Rediger kursus detaljer
        </twbs:linkButton>
        <twbs:linkButton action="createSubject" id="${course.id}" style="${ButtonStyle.LINK}" block="true">
            <fa:icon icon="${FaIcon.PLUS_CIRCLE}" />
            Opret nyt emne
        </twbs:linkButton>
        <twbs:linkButton action="createVideo" id="${course.id}" style="${ButtonStyle.LINK}" block="true">
            <fa:icon icon="${FaIcon.PLAY}" />
            Opret ny video
        </twbs:linkButton>
    </div>
</g:content>

<script>
    $(function () {
        var listManipulator = new ListManipulator(".subject", ".subject-up", ".subject-down", ".subject-delete");
        listManipulator.init();

        AjaxUtil.registerJSONForm("#save-subject-order", "${createLink(action: "updateSubjects")}", function() {
            var order = listManipulator.map(function (element) {
                return parseInt(element.find("[data-subject-id]").attr("data-subject-id"));
            });

            return { order: order, course: ${course.id} };
        });
    });
</script>
</body>
</html>