<%@ page import="dk.danthrane.twbs.ButtonStyle; dk.sdu.tekvideo.FaIcon" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Administrering af ${course.fullName} (${course.name})</title>
    <meta name="layout" content="main" />
</head>

<body>
<twbs:row>
    <twbs:column>
        <twbs:pageHeader><h3>Administrering af ${course.fullName} (${course.name})</h3></twbs:pageHeader>

        <h4><fa:icon icon="${FaIcon.WRENCH}" /> Kontrol panel</h4>
        <twbs:row>
            <twbs:column cols="3">
                <twbs:linkButton action="createCourse" style="${ButtonStyle.LINK}" block="true">
                    <fa:icon icon="${FaIcon.PLUS_CIRCLE}" />
                    Opret nyt emne
                </twbs:linkButton>
            </twbs:column>
            <twbs:column cols="3">
                <twbs:linkButton action="createVideo" id="${course.id}" style="${ButtonStyle.LINK}" block="true">
                    <fa:icon icon="${FaIcon.PLAY}" />
                    Opret ny video
                </twbs:linkButton>
            </twbs:column>
        </twbs:row>
        <twbs:row>
            <twbs:column>
                <h4>Emner</h4>
                <g:if test="${course.subjects.isEmpty()}">
                    <p>Kurset har ikke nogen emner</p>
                </g:if>
                <g:else>
                    <g:each in="${course.subjects}" var="subject">
                        <sdu:card>
                            <a href="#" class="subject-link" data-id="${subject.id}">
                                <span class="block-link"></span>
                            </a>
                            <a href="#">${subject.name}</a>
                        </sdu:card>
                        <sdu:card id="subject-data-${subject.id}" class="hide subject-data">
                            <span id="subject-loading-${subject.id}">
                                <fa:icon icon="${FaIcon.SPINNER}" spin="true"  size="4" />
                            </span>
                            <div id="subject-data-content-${subject.id}"></div>
                        </sdu:card>
                    </g:each>
                </g:else>
            </twbs:column>
        </twbs:row>
    </twbs:column>
</twbs:row>

<script>
    $(function () {
        $(".subject-link").click(function (e) {
            e.preventDefault();
            var id = $(this).attr("data-id");
            $(".subject-data").addClass("hide");
            $("#subject-data-" + id).removeClass("hide");
            $("#subject-loading-" + id).removeClass("hide");
            console.log(id);
            $.get("${createLink(action: "getVideos")}/" + id, function (data) {
                console.log(data);
                $("#subject-loading-" + id).addClass("hide");
                $("#subject-data-content-" + id).html(data);
            });
        });
    });
</script>
</body>
</html>