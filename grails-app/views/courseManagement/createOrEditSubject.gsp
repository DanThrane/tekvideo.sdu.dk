<%@ page import="dk.sdu.tekvideo.FaIcon; dk.danthrane.twbs.ButtonStyle" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Nyt emne</title>
    <meta name="layout" content="main" />
</head>

<body>
<twbs:row>
    <twbs:column>
        %{-- TODO Refactor this --}%
        <twbs:pageHeader>
            <h3>
                <g:if test="${isEditing}">
                    Administreing af emne
                    <small>${command.domain.name}</small>
                </g:if>
                <g:else>
                    Nyt emne
                </g:else>
            </h3>
        </twbs:pageHeader>

        <h4>Grundlæggende detaljer</h4>
        <twbs:form method="POST" action="${createLink(action: "postSubject", id: course.id)}">
            <sducrud:hiddenFields />

            <twbs:input name="domain.name" bean="${command?.domain}" labelText="Navn" />
            <twbs:textArea name="domain.description" bean="${command?.domain}" labelText="Beskrivelse"
                           rows="10" />

            <sducrud:saveButton />
        </twbs:form>

        <hr>

        <g:if test="${isEditing}">
            <h4>Videoer</h4>

            <blockquote>
                Help text goes here
            </blockquote>

            <g:if test="${!command.domain.videos?.isEmpty()}">
                <div id="video-container">
                    <g:each in="${command.domain.videos}" var="video">
                        <sdu:card class="video">
                            <div data-video-id="${video.id}"></div>
                            <twbs:row>
                                <twbs:column cols="8">
                                    <h6>${video.name}</h6>
                                </twbs:column>
                                <twbs:column cols="4" class="align-right">
                                    <twbs:buttonToolbar>
                                        <twbs:button style="${ButtonStyle.LINK}">
                                            <fa:icon icon="${FaIcon.YOUTUBE_PLAY}" />
                                        </twbs:button>
                                        <twbs:button style="${ButtonStyle.LINK}">
                                            <fa:icon icon="${FaIcon.EDIT}" />
                                        </twbs:button>
                                        <twbs:button style="${ButtonStyle.SUCCESS}" class="video-up">
                                            <fa:icon icon="${FaIcon.ARROW_UP}" />
                                        </twbs:button>
                                        <twbs:button style="${ButtonStyle.DANGER}" class="video-down">
                                            <fa:icon icon="${FaIcon.ARROW_DOWN}" />
                                        </twbs:button>
                                    </twbs:buttonToolbar>
                                </twbs:column>
                            </twbs:row>
                        </sdu:card>
                    </g:each>
                </div>
                <twbs:button style="${ButtonStyle.PRIMARY}" id="save-video-order">
                    <fa:icon icon="${FaIcon.EDIT}" />
                    Gem ændringer
                </twbs:button>
            </g:if>
            <g:else>
                Dette emne har ikke nogle videoer.
            </g:else>
        </g:if>
    </twbs:column>
</twbs:row>

<script>
    $(function () {
        $(".video-up").click(function() {
            var thisVideo = $(this).closest(".video");
            var index = thisVideo.index();
            if (index == 0) return;

            var videos = $(".video");
            thisVideo.slideUp(200, function () {
                thisVideo.insertBefore($(videos[index - 1])).slideDown(200);
            });

        });

        $(".video-down").click(function() {
            var thisVideo = $(this).closest(".video");
            var index = thisVideo.index();
            var videos = $(".video");

            if (index == videos.length - 1) return;
            thisVideo.slideUp(200, function () {
                thisVideo.insertAfter($(videos[index + 1])).slideDown(200);
            });
        });

        $("#save-video-order").click(function (e) {
            e.preventDefault();
            var order = $("[data-video-id]").map(function (e, r) {
                return parseInt($(r).attr("data-video-id"))
            });

            var data = {
                order: order,
                course: ${course.id}
            };

            Util.postJson("${createLink(action: "updateVideos")}", data, {
                success: function (data) {
                    console.log("success!");
                },
                error: function (data) {
                    console.log("error!");
                },
                complete: function() {
                    console.log("complete!");
                }
            });
        });
    });
</script>
</body>
</html>