<%@ page import="dk.danthrane.twbs.NavStyle; dk.sdu.tekvideo.NodeStatus; dk.sdu.tekvideo.FaIcon; dk.danthrane.twbs.ButtonStyle" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Administreing af emne ${subject.name}</title>
    <meta name="layout" content="main_fluid"/>
    <asset:javascript src="list.js"/>
    <sdu:requireAjaxAssets/>
</head>

<body>
<twbs:row>
    <twbs:column>
        <twbs:pageHeader>
            <h3>Administreing af emne <small>${subject.name}</small></h3>
        </twbs:pageHeader>

        <twbs:row>
            <twbs:column>
                <ol class="breadcrumb">
                    <li><g:link action="index" controller="courseManagement">Mine Kurser</g:link></li>
                    <li>
                        <g:link action="manage" controller="courseManagement" id="${subject.courseId}">
                            ${subject.course.fullName} (${subject.course.name})
                        </g:link>
                    </li>
                    <li class="active">${subject.name}</li>
                </ol>
            </twbs:column>
        </twbs:row>

        <sdu:card>
            <g:if test="${videos.isEmpty()}">
                Ingen videoer her.
            </g:if>
            <g:else>
                <div id="video-container">
                    <g:each in="${videos}" var="video" status="idx">
                        <div class="video">
                            <div data-video-id="${video.id}" class="hide"></div>
                            <twbs:row>
                                <twbs:column cols="8">
                                    <div class="node-header">
                                        <g:link action="editVideo" id="${video.id}">
                                            ${video.name}
                                        </g:link>
                                    </div>
                                </twbs:column>
                                <twbs:column cols="4" class="align-right">
                                    <twbs:buttonGroup>
                                        <twbs:dropdownToggle style="${ButtonStyle.WARNING}">
                                            <fa:icon icon="${FaIcon.ARROW_CIRCLE_RIGHT}"/> Flyt til

                                            <twbs:dropdownMenu>
                                                <twbs:dropdownItem disabled="${status == NodeStatus.VISIBLE}"
                                                                   action="videoStatus" id="${video.id}"
                                                                   params="${[status: NodeStatus.VISIBLE]}">
                                                    <fa:icon icon="${FaIcon.EYE}"/> Synlige
                                                </twbs:dropdownItem>
                                                <twbs:dropdownItem disabled="${status == NodeStatus.INVISIBLE}"
                                                                   action="videoStatus" id="${video.id}"
                                                                   params="${[status: NodeStatus.INVISIBLE]}">
                                                    <fa:icon icon="${FaIcon.EYE_SLASH}"/> Usynlige
                                                </twbs:dropdownItem>
                                                <twbs:dropdownItem disabled="${status == NodeStatus.TRASH}"
                                                                   action="videoStatus" id="${video.id}"
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
                                <twbs:column>
                                    <markdown:renderHtml><sdu:abbreviate>${video.description}</sdu:abbreviate></markdown:renderHtml>
                                </twbs:column>
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
    <hr>
    <sdu:ajaxSubmitButton style="${ButtonStyle.LINK}" id="save-video-order" block="true">
        <fa:icon icon="${FaIcon.EDIT}"/>
        Gem rækkefølge
    </sdu:ajaxSubmitButton>
</g:content>

<script>
    $(function () {
        var list = new ListManipulator(".video", ".video-up", ".video-down");
        list.init();

        AjaxUtil.registerJSONForm("#save-video-order", "${createLink(action: "updateVideos")}", function () {
            var order = list.map(function (element) {
                return parseInt(element.find("[data-video-id]").attr("data-video-id"));
            });

            return {order: order, subject: ${subject.id}};
        });
    });
</script>
</body>
</html>