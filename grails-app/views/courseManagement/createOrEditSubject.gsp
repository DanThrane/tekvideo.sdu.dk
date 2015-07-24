<%@ page import="dk.sdu.tekvideo.FaIcon; dk.danthrane.twbs.ButtonStyle" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Nyt emne</title>
    <meta name="layout" content="main" />
    <sdu:requireAjaxAssets />
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
                                        <twbs:button style="${ButtonStyle.LINK}" disabled="true">
                                            <fa:icon icon="${FaIcon.YOUTUBE_PLAY}" />
                                        </twbs:button>
                                        <twbs:button style="${ButtonStyle.LINK}" disabled="true">
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
                <sdu:ajaxSubmitButton style="${ButtonStyle.PRIMARY}" id="save-video-order">
                    <fa:icon icon="${FaIcon.EDIT}" />
                    Gem ændringer
                </sdu:ajaxSubmitButton>
            </g:if>
            <g:else>
                Dette emne har ikke nogle videoer.
            </g:else>
        </g:if>
    </twbs:column>
</twbs:row>

<script>
    $(function () {
        function movementFromDelta(delta) {
            if (delta <= 0) {
                return "-=" + Math.abs(delta);
            } else {
                return "+=" + Math.abs(delta);
            }
        }

        function swapVideos(idxDown, idxUp, callback) {
            var allVideos = $(".video");

            var upperVid = $(allVideos[idxDown]);
            var lowerVid = $(allVideos[idxUp]);

            var upperPos = upperVid.position();
            var lowerPos = lowerVid.position();

            var upperVidMovement = lowerPos.top - upperPos.top;
            var lowerVidMovement = upperPos.top - lowerPos.top;

            upperVid.animate({ top: movementFromDelta(upperVidMovement) });
            lowerVid.animate({ top: movementFromDelta(lowerVidMovement )}, function() {
                callback();
                // Clear the animated properties post-callback
                upperVid.css("top", "initial");
                lowerVid.css("top", "initial");
            });
        }

        $(".video-up").click(function() {
            var thisVideo = $(this).closest(".video");
            var index = thisVideo.index();
            if (index == 0) return;

            var videos = $(".video");

            swapVideos(index - 1, index, function() {
                thisVideo.insertBefore($(videos[index - 1]));
            });
        });

        $(".video-down").click(function() {
            var thisVideo = $(this).closest(".video");
            var index = thisVideo.index();
            var videos = $(".video");

            if (index == videos.length - 1) return;

            swapVideos(index, index + 1, function() {
                thisVideo.insertAfter($(videos[index + 1]));
            });
        });

        AjaxUtil.registerJSONForm("#save-video-order", "${createLink(action: "updateVideos")}", function() {
            var order = $.map($("[data-video-id]"), function (element) {
                return parseInt($(element).attr("data-video-id"));
            });

            return {
                order: order,
                subject: ${command.domain.id}
            };
        });
    });
</script>
</body>
</html>