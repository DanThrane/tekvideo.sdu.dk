<%@ page import="java.time.LocalDateTime; dk.danthrane.twbs.ButtonStyle; dk.sdu.tekvideo.FaIcon" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main_fluid">
    <title>${video.name}</title>
</head>

<body>

<twbs:pageHeader>
    <h3>${video.name}</h3>
</twbs:pageHeader>

<twbs:row>
    <twbs:column>
        <ol class="breadcrumb">
            <li><g:link uri="/">Hjem</g:link></li>
            <li>
                <sdu:linkToTeacher teacher="${video.subject.course.teacher}">
                    ${video.subject.course.teacher}
                </sdu:linkToTeacher>
            </li>
            <li>
                <sdu:linkToCourse course="${video.subject.course}">
                    ${video.subject.course.fullName} (${video.subject.course.name})
                </sdu:linkToCourse>
            </li>
            <li>
                <sdu:linkToSubject subject="${video.subject}">
                    ${video.subject.name}
                </sdu:linkToSubject>
            </li>
            <li class="active">${video.name}</li>
        </ol>
    </twbs:column>
</twbs:row>

<div class="embed-responsive embed-responsive-16by9">
    <div class="wrapper" id="wrapper" style="z-index: 20"></div>
    <div id="player"></div>
</div>

<g:content key="content-below-the-fold">
    <twbs:pageHeader>
        <h5>Kommentarer</h5>
    </twbs:pageHeader>
    <sec:ifLoggedIn>
        <twbs:form method="post" action="${createLink(action: "postComment", id: video.id)}">
            <twbs:textArea labelText="" placeholder="Skriv en kommentar her" rows="5" name="comment"/>
            <twbs:button type="submit" style="${ButtonStyle.PRIMARY}">
                <fa:icon icon="${FaIcon.ENVELOPE}"/>
                Send
            </twbs:button>
        </twbs:form>
    </sec:ifLoggedIn>
    <sec:ifNotLoggedIn>
        Du skal være logget ind for at skrive en kommentar!
    </sec:ifNotLoggedIn>
    <hr>
    <g:each in="${video.comments}" var="comment">
        <div class="comment">
            <twbs:row>
                <twbs:column sm="1">
                    <avatar:gravatar size="80" email="${comment.user.email ?: "no@mail.com"}"/>
                </twbs:column>
                <twbs:column sm="9">
                    <h6>${comment.user.username} <small><date:utilDateFormatter time="${comment.dateCreated}"/></small>
                    </h6>

                    <p>${comment.contents}</p>
                </twbs:column>
                <twbs:column sm="1" class="pull-right">
                    <sec:ifAllGranted roles="ROLE_TEACHER">
                        <twbs:modalButton target="#comment-delete-modal" data-comment-id="${comment.id}"
                                          style="${ButtonStyle.DANGER}" class="subject-delete">
                            <fa:icon icon="${FaIcon.TRASH}"/>
                        </twbs:modalButton>
                    </sec:ifAllGranted>
                </twbs:column>
            </twbs:row>
            <hr>
        </div>
    </g:each>
</g:content>

<twbs:modal id="comment-delete-modal">
    <twbs:modalHeader>Er du sikker?</twbs:modalHeader>
    Dette vil slette kommentaren fra denne video!
    <twbs:modalFooter>
        <twbs:button data-dismiss="modal">Annulér</twbs:button>
        <twbs:button style="${ButtonStyle.DANGER}" id="comment-delete-button">Slet kommentar</twbs:button>
    </twbs:modalFooter>
</twbs:modal>

<g:content key="sidebar-right">
    <twbs:pageHeader>
        <h3>Indhold</h3>
    </twbs:pageHeader>

    <ul id="videoNavigation" class="video-navigation">
    </ul>

    <div id="similar-resources">
        <g:if test="${!video.similarResources.empty}">
            <twbs:pageHeader><h4>Se også</h4></twbs:pageHeader>
            <ul>
                <g:each in="${video.similarResources}">
                    <li><a href="${it.link}">${it.title}</a></li>
                </g:each>
            </ul>
        </g:if>
    </div>

    <div class="sidebar-pull-bottom">
        <twbs:button style="${ButtonStyle.PRIMARY}" block="true" id="checkAnswers">
            <fa:icon icon="${FaIcon.CHECK}"/>
            Tjek svar
        </twbs:button>
    </div>
</g:content>

<script type="text/javascript">
    $(document).ready(function () {
        var commentToDelete = null;

        var player = new InteractiveVideoPlayer();
        player.playerElement = $("#player");
        player.wrapperElement = $("#wrapper");
        player.navigationElement = $("#videoNavigation");
        player.checkButton = $("#checkAnswers");

        player.startPlayer("${raw(video.youtubeId)}",
                ${video.videoType}
                <g:if test="${video.timelineJson}">
                , ${raw(video.timelineJson)}
                </g:if>);

        %{-- Used by the event framework to determine the origin of the event --}%
        events.setMetaData({
            "video": ${video.id}
        });

        events.emit({"kind": "VISIT_VIDEO"}, true);

        $("#comment-delete-modal").on("show.bs.modal", function (e) {
            commentToDelete = $(e.relatedTarget).data("comment-id");
        });

        $("#comment-delete-button").click(function () {
            $("[data-comment-id=" + commentToDelete + "]").closest(".comment")[0].remove();
            $("#comment-delete-modal").modal("hide");
            var data = {comment: commentToDelete};
            Util.postJson("${createLink(action: "deleteComment", id: video.id)}?comment=" + commentToDelete, data, {});
        });
    });
</script>
</body>
</html>