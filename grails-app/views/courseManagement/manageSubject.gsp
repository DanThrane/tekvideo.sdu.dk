<%@ page import="dk.danthrane.twbs.NavStyle; dk.sdu.tekvideo.NodeStatus; dk.sdu.tekvideo.FaIcon; dk.danthrane.twbs.ButtonStyle" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Administrering af emne ${subject.name}</title>
    <meta name="layout" content="main_fluid"/>
    <asset:javascript src="list.js"/>
    <sdu:requireAjaxAssets/>
</head>

<body>
<twbs:row>
    <twbs:column>
        <twbs:pageHeader>
            <h3>Administrering af emne <small>${subject.name}</small></h3>
        </twbs:pageHeader>

        <twbs:row>
            <twbs:column>
                <ol class="breadcrumb">
                    <li><g:link action="index" controller="courseManagement">Kursusadministration</g:link></li>
                    <li>
                        <g:link action="manage" controller="courseManagement" id="${subject.course.id}">
                            ${subject.course.fullName} (${subject.course.name})
                        </g:link>
                    </li>
                    <li class="active">${subject.name}</li>
                </ol>
            </twbs:column>
        </twbs:row>

        <sdu:card>
            <g:if test="${excises.isEmpty()}">
                Ingen videoer her.
            </g:if>
            <g:else>

                <div id="video-container">
                    <g:each in="${excises}" var="exercise" status="idx">
                        <div class="video">
                            <div data-video-id="${exercise.id}" class="hide"></div>
                            <twbs:row>
                                <twbs:column cols="8">
                                    <div class="node-header">
                                        <g:if test="${exercise instanceof dk.sdu.tekvideo.Video}">
                                            <g:link action="editVideo" id="${exercise.id}">
                                                ${exercise.name}
                                            </g:link>
                                        </g:if>
                                        <g:if test="${exercise instanceof dk.sdu.tekvideo.WrittenExerciseGroup}">
                                            <g:link action="editWrittenExercise" id="${exercise.id}">
                                                ${exercise.name}
                                            </g:link>
                                        </g:if>
                                    </div>
                                </twbs:column>
                                <twbs:column cols="4" class="align-right">
                                    <twbs:linkButton action="editLinks" id="${exercise.id}" style="${ButtonStyle.INFO}">
                                        <fa:icon icon="${FaIcon.LINK}" />
                                        Rediger links
                                    </twbs:linkButton>

                                    <twbs:buttonGroup>
                                        <twbs:dropdownToggle style="${ButtonStyle.WARNING}">
                                            <fa:icon icon="${FaIcon.ARROW_CIRCLE_RIGHT}"/> Flyt til

                                            <twbs:dropdownMenu>
                                                <twbs:dropdownItem disabled="${status == NodeStatus.VISIBLE}"
                                                                   action="exerciseStatus" id="${exercise.id}"
                                                                   params="${[status: NodeStatus.VISIBLE]}">
                                                    <fa:icon icon="${FaIcon.EYE}"/> Synlige
                                                </twbs:dropdownItem>
                                                <twbs:dropdownItem disabled="${status == NodeStatus.INVISIBLE}"
                                                                   action="exerciseStatus" id="${exercise.id}"
                                                                   params="${[status: NodeStatus.INVISIBLE]}">
                                                    <fa:icon icon="${FaIcon.EYE_SLASH}"/> Usynlige
                                                </twbs:dropdownItem>
                                                <twbs:dropdownItem disabled="${status == NodeStatus.TRASH}"
                                                                   action="exerciseStatus" id="${exercise.id}"
                                                                   params="${[status: NodeStatus.TRASH]}">
                                                    <fa:icon icon="${FaIcon.TRASH}"/> Papirkurv
                                                </twbs:dropdownItem>
                                            </twbs:dropdownMenu>
                                        </twbs:dropdownToggle>
                                    </twbs:buttonGroup>
                                    <div class="node-header-spacer"></div>

                                    <twbs:button style="${ButtonStyle.SUCCESS}" class="video-up">
                                        <fa:icon icon="${FaIcon.ARROW_UP}"/>
                                    </twbs:button>
                                    <twbs:button style="${ButtonStyle.INFO}" class="video-down">
                                        <fa:icon icon="${FaIcon.ARROW_DOWN}"/>
                                    </twbs:button>
                                </twbs:column>
                            </twbs:row>
                            <twbs:row>
                                <g:if test="${meta[idx] != null}">
                                    <twbs:column md="6">
                                        <markdown:renderHtml><sdu:abbreviate>${exercise.description}</sdu:abbreviate></markdown:renderHtml>
                                    </twbs:column>
                                    <twbs:column md="6">
                                        <ul>
                                            <li><b>Video længde:</b> ${meta[idx].duration}</li>
                                            <li><b>Antal emner:</b> ${meta[idx].subjectCount}</li>
                                            <li><b>Antal spørgsmål:</b> ${meta[idx].questionCount}</li>
                                            <li><b>Antal felter:</b> ${meta[idx].fieldCount}</li>
                                        </ul>
                                    </twbs:column>
                                </g:if>
                                <g:else>
                                    <twbs:column md="12">
                                        <markdown:renderHtml><sdu:abbreviate>${exercise.description}</sdu:abbreviate></markdown:renderHtml>
                                    </twbs:column>
                                </g:else>
                            </twbs:row>
                            <hr>
                        </div>
                    </g:each>
                </div>
            </g:else>
        </sdu:card>
    </twbs:column>
</twbs:row>

<g:content key="sidebar-right">
    <twbs:pageHeader>
        <h4>Filter</h4>
    </twbs:pageHeader>
    <twbs:nav style="${NavStyle.PILL}" stacked="true">
        <twbs:navItem active="${status == NodeStatus.VISIBLE}" action="manageSubject" id="${subject.id}"
                      params="${[status: NodeStatus.VISIBLE]}">
            <fa:icon icon="${FaIcon.EYE}"/> Synlige
        </twbs:navItem>
        <twbs:navItem active="${status == NodeStatus.INVISIBLE}" action="manageSubject" id="${subject.id}"
                      params="${[status: NodeStatus.INVISIBLE]}">
            <fa:icon icon="${FaIcon.EYE_SLASH}"/> Usynlige
        </twbs:navItem>
        <twbs:navItem active="${status == NodeStatus.TRASH}" action="manageSubject" id="${subject.id}"
                      params="${[status: NodeStatus.TRASH]}">
            <fa:icon icon="${FaIcon.TRASH}"/> Papirkurv
        </twbs:navItem>
    </twbs:nav>

    <twbs:pageHeader>
        <h4>Handlinger</h4>
    </twbs:pageHeader>
    <twbs:linkButton action="editSubject" id="${subject.id}" style="${ButtonStyle.LINK}" block="true">
        <fa:icon icon="${FaIcon.EDIT}"/> Rediger emne detaljer
    </twbs:linkButton>
    <twbs:linkButton action="createVideo" id="${subject.course.id}" params="${[subject: subject.id]}"
                     style="${ButtonStyle.LINK}" block="true">
        <fa:icon icon="${FaIcon.PLUS}"/> Tilføj video
    </twbs:linkButton>
    <twbs:linkButton action="createWrittenExercise" id="${subject.id}"
                     style="${ButtonStyle.LINK}" block="true">
        <fa:icon icon="${FaIcon.PLUS}"/> Tilføj skriftlig opgave
    </twbs:linkButton>
    <hr>
    <sdu:ajaxSubmitButton style="${ButtonStyle.LINK}" id="save-video-order" block="true">
        <fa:icon icon="${FaIcon.EDIT}"/>
        Gem rækkefølge
    </sdu:ajaxSubmitButton>
</g:content>

<g:content key="sidebar-left">
    <div id="tree-container"></div>
</g:content>

<script>
    var baseUrl = "${createLink(absolute:true, uri:'/')}";

    $(function () {
        var list = new ListManipulator(".video", ".video-up", ".video-down");
        list.init();

        AjaxUtil.registerJSONForm("#save-video-order", "${createLink(action: "updateExercises")}", function () {
            var order = list.map(function (element) {
                return parseInt(element.find("[data-video-id]").attr("data-video-id"));
            });

            return {order: order, subject: ${subject.id}};
        });

        var tree = new ManagementTreeView("#tree-container", "${createLink(absolute:true, uri:'/')}");
        tree.init();
    });
</script>

<asset:javascript src="./courseManagement/app.js"/>
<asset:stylesheet src="./vendor/proton/style.min.css"/>
</body>
</html>