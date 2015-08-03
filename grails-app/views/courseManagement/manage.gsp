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
                                <twbs:column cols="8" class="card-link">
                                    <a href="#" class="subject-link" data-id="${subject.id}">
                                        <span class="block-link"></span>
                                    </a>
                                    <a href="#">${subject.name}</a>
                                </twbs:column>
                                <twbs:column cols="4" class="pull-right">
                                    <twbs:column cols="8">
                                        <twbs:linkButton action="editSubject" id="${subject.id}" block="true"
                                                         style="${ButtonStyle.LINK}" size="${ButtonSize.SMALL}">
                                            <fa:icon icon="${FaIcon.EDIT}" />
                                            Rediger
                                        </twbs:linkButton>
                                    </twbs:column>
                                    <twbs:column cols="2">
                                        <twbs:button style="${ButtonStyle.SUCCESS}" class="subject-up">
                                            <fa:icon icon="${FaIcon.ARROW_UP}" />
                                        </twbs:button>
                                    </twbs:column>
                                    <twbs:column cols="2">
                                        <twbs:button style="${ButtonStyle.DANGER}" class="subject-down">
                                            <fa:icon icon="${FaIcon.ARROW_DOWN}" />
                                        </twbs:button>
                                    </twbs:column>
                                </twbs:column>
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
    <twbs:pageHeader>
        <h3>Kontrol panel</h3>
    </twbs:pageHeader>

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
    </g:content>

<script>
    $(function () {
        var listManipulator = new ListManipulator(".subject", ".subject-up", ".subject-down");
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