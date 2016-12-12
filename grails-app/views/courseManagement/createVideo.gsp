<%@ page import="dk.sdu.tekvideo.NodeStatus; dk.danthrane.twbs.GridSize; dk.danthrane.twbs.InputSize; dk.danthrane.twbs.Icon; dk.danthrane.twbs.ButtonSize; dk.sdu.tekvideo.FaIcon; dk.danthrane.twbs.ButtonStyle" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <g:if test="${isEditing}">
        <title>Redigering af ${video.name}</title>
    </g:if>
    <g:else>
        <title>Ny video til ${course.fullName} (${course.name})</title>
    </g:else>
    <meta name="layout" content="main_fluid"/>
    <asset:javascript src="interact.js"/>
    <asset:stylesheet src="create_video.css"/>
    <sdu:requireAjaxAssets />
    <script src="https://cdnjs.cloudflare.com/ajax/libs/ace/1.1.9/ace.js"></script>
</head>

<body>
<div class="card-stack" id="main-panel-stack">

    %{-- Publishing --}%
    <div class="card-item publish-card">
        <div class="center" id="publish-spinner">
            <fa:icon icon="${FaIcon.SPINNER}" spin="true" size="4"/>
        </div>

        <div class="hide" id="publish-success">
            <twbs:pageHeader><h3>Succes!</h3></twbs:pageHeader>
            <twbs:row>
                <twbs:column cols="2">
                    <span style="color:green">
                        <fa:icon icon="${FaIcon.CHECK}" size="4"/>
                    </span>
                </twbs:column>
                <twbs:column cols="10">
                    Din video er blevet udgivet succesfuldt! Du kan
                    <g:link action="createVideo" id="${params.id}">oprette en ny video.</g:link>
                    <g:if test="${!isEditing}">
                        Den er nu tilgængelig på
                        <sdu:linkToCourse course="${course}">
                            kursus siden.
                        </sdu:linkToCourse>
                    </g:if>

                </twbs:column>
            </twbs:row>
        </div>

        <div class="hide" id="publish-error">
            <twbs:pageHeader><h3>Fejl!</h3></twbs:pageHeader>
            <twbs:column cols="2">
                <span style="color:red">
                    <fa:icon icon="${FaIcon.REMOVE}" size="4"/>
                </span>
            </twbs:column>
            <twbs:column cols="10">
                Der skete en fejl under udgivelse af videoen.

                <blockquote>
                    Dette skyldes sandsynligvis at et felt mangler at blive udfyldt, fx navnet på videoen.
                    UI endnu ikke implementeret til at vise hvilke fejl der er.
                </blockquote>

                <twbs:button style="${ButtonStyle.PRIMARY}" id="publish-try-again">
                    <fa:icon icon="${FaIcon.REFRESH}"/> Prøv igen
                </twbs:button>
            </twbs:column>
        </div>
    </div>

    %{-- Preview --}%
    <div class="card-item preview-card">
        <twbs:pageHeader><h3>Forhåndsvisning</h3></twbs:pageHeader>
        <div class="embed-responsive embed-responsive-16by9">
            <div id="wrapper" style="z-index: 20"></div>

            <div id="preview-player"></div>
        </div>
    </div>

    %{-- Editor --}%
    <div class="card-item active creator-card">
        <g:if test="${isEditing}">
            <twbs:pageHeader><h3>Redigering af ${video.name}</h3></twbs:pageHeader>
        </g:if>
        <g:else>
            <twbs:pageHeader><h3>Ny video til ${course.fullName} (${course.name})</h3></twbs:pageHeader>
        </g:else>

        <twbs:row>
            <twbs:column>
                <ol class="breadcrumb">
                    <li><g:link action="index" controller="courseManagement">Kursusadministration</g:link></li>
                    <li>
                        <g:link action="manage" controller="courseManagement" id="${course.id}">
                            ${course.fullName} (${course.name})
                        </g:link>
                    </li>
                    <g:if test="${isEditing}">
                        <li>
                            <g:link action="manageSubject" controller="courseManagement" id="${video.subject.id}">
                                ${video.subject.name}
                            </g:link>
                        </li>
                        <li class="active">Redigering af ${video.name}</li>
                    </g:if>
                    <g:else>
                        <li class="active">Ny video</li>
                    </g:else>
                </ol>
            </twbs:column>
        </twbs:row>

        <div class="embed-responsive embed-responsive-16by9">
            <div id="fields" style="z-index: 20"></div>

            <div id="player"></div>
        </div>
        <hr>
    </div>
</div>

<g:content key="content-below-the-fold">
%{-- Attributes --}%
    <div class="card-stack" id="attributes-stack">
        %{-- Edit subject --}%
        <div id="subject-form-card" class="card-item">
            <h4><fa:icon icon="${FaIcon.BOOK}"/> Redigér emne</h4>
            <twbs:buttonGroup justified="true">
                <twbs:button class="addQuestion">
                    <fa:icon icon="${FaIcon.PLUS}"/> Tilføj spørgsmål
                </twbs:button>
                <twbs:button style="${ButtonStyle.DANGER}" id="deleteSubject">
                    <fa:icon icon="${FaIcon.TRASH}"/> Slet
                </twbs:button>
            </twbs:buttonGroup>
            <hr>
            <twbs:form id="subject-form">
                <twbs:input name="subjectName" labelText="Navn"/>
                <twbs:input name="subjectTimecode" labelText="Tidskode">
                    For eksempel: <code>2:20</code>
                </twbs:input>
                <twbs:button type="submit" style="${ButtonStyle.SUCCESS}" block="true">
                    <fa:icon icon="${FaIcon.CHECK}"/>
                    Gem ændringer
                </twbs:button>
            </twbs:form>
        </div>
        %{-- Edit question --}%
        <div id="question-form-card" class="card-item">
            <h4><fa:icon icon="${FaIcon.QUESTION}"/> Redigér spørgsmål</h4>
            <twbs:buttonGroup justified="true">
                <twbs:button id="backToSubject">
                    <fa:icon icon="${FaIcon.BACKWARD}"/> Tilbage til emne
                </twbs:button>
                <twbs:button class="addQuestion" style="${ButtonStyle.INFO}">
                    <fa:icon icon="${FaIcon.PLUS}"/> Tilføj spørgsmål
                </twbs:button>
                <twbs:button class="addField" style="${ButtonStyle.SUCCESS}">
                    <fa:icon icon="${FaIcon.PLUS}"/> Tilføj felt
                </twbs:button>
                <twbs:button style="${ButtonStyle.DANGER}" id="deleteQuestion">
                    <fa:icon icon="${FaIcon.TRASH}"/> Slet
                </twbs:button>
            </twbs:buttonGroup>
            <hr>
            <twbs:form id="question-form">
                <twbs:input name="questionName" labelText="Navn"/>
                <twbs:input name="questionTimecode" labelText="Tidskode">
                    For eksempel: <code>2:20</code>
                </twbs:input>
                <twbs:button type="submit" style="${ButtonStyle.SUCCESS}" block="true">
                    <fa:icon icon="${FaIcon.CHECK}"/>
                    Gem ændringer
                </twbs:button>
            </twbs:form>
        </div>
        %{-- Edit field --}%
        <div id="field-form-card" class="card-item">
            <h4><fa:icon icon="${FaIcon.FILE}"/>  Redigér felt</h4>
            <twbs:buttonGroup justified="true">
                <twbs:button id="backToQuestion">
                    <fa:icon icon="${FaIcon.BACKWARD}"/> Tilbage til spørgsmål
                </twbs:button>
                <twbs:button class="addField" style="${ButtonStyle.SUCCESS}">
                    <fa:icon icon="${FaIcon.PLUS}"/> Tilføj nyt felt
                </twbs:button>
                <twbs:button style="${ButtonStyle.DANGER}" id="deleteField">
                    <fa:icon icon="${FaIcon.TRASH}"/> Slet
                </twbs:button>
            </twbs:buttonGroup>
            <hr>
            <twbs:form id="field-form">
                <twbs:input name="fieldName" labelText="Felt ID">
                    Hvis du bruger et JavaScript felt, så vil du kunne henvise til feltet ved hjælp af dette ID
                </twbs:input>
                <twbs:select name="fieldType" labelText="Spørgsmåls type"
                             list="${["Ingen", "Mellem", "Tekst", "Brugerdefineret (JavaScript)",
                                      "Matematisk udtryk"]}"/>

                <div class="card-stack" id="field-type-stack">
                    <div class="card-item active" id="no-field-type-card">
                    </div>

                    <div class="card-item" id="between-field-type-card">
                        <twbs:input name="betweenMinValue" labelText="Mindste værdi"/>
                        <twbs:input name="betweenMaxValue" labelText="Højeste værdi"/>
                    </div>

                    <div class="card-item" id="text-field-type-card">
                        <twbs:input name="textFieldExact" labelText="Tekst">
                            <g:content key="addon-left">
                                <twbs:inputGroupAddon>
                                    <twbs:checkbox name="textFieldIgnoreCase"
                                                   labelText="Case insensitive"/>
                                </twbs:inputGroupAddon>
                            </g:content>
                            Spørgsmålet er svaret korrekt, kun når input er præcis det skrevet i ovenstående
                            felt.
                        </twbs:input>
                    </div>

                    <div class="card-item" id="userdefined-field-type-card">
                        <div id="editor">function validator(value) {
                        return value === "6" || utilCheck();
                        }

                        // Utility functions can be defined too
                        function utilCheck() {
                        // It is possible to reference other fields using their ID
                        var valueOfOtherField = $("#field-2").text();
                        return valueOfOtherField === "hello";
                        }

                        return validator; // Return the validator function</div>
                    </div>

                    <div class="card-item" id="expression-field-type-card">
                        <label>Udtryk</label> <br/>

                        <div id="expression-container">
                        </div>
                        <br/>
                    </div>
                </div>
                <twbs:button type="submit" block="true" style="${ButtonStyle.SUCCESS}" id="field-save">
                    <fa:icon icon="${FaIcon.CHECK}"/>
                    Gem ændringer
                </twbs:button>
            </twbs:form>
        </div>
    </div>
</g:content>

%{-- Sidebar --}%
<g:content key="sidebar-right">
    <div>
        <div class="card-stack" id="sidebar-stack">
            <div class="card-item preview-card">
                <twbs:pageHeader>
                    <h3>Indhold</h3>
                </twbs:pageHeader>

                <ul id="videoNavigation" class="video-navigation"></ul>

                <hr>
                <twbs:button block="true" style="${ButtonStyle.DANGER}" id="stop-preview">
                    <fa:icon icon="${FaIcon.STREET_VIEW}"/> Stop forhåndsvisning
                </twbs:button>

                <twbs:button style="${ButtonStyle.PRIMARY}" block="true" id="checkAnswers">
                    <fa:icon icon="${FaIcon.CHECK}"/>
                    Tjek svar
                </twbs:button>
            </div>

            <div class="creator-card card-item active">
                <twbs:pageHeader><h3>Kontrol panel</h3></twbs:pageHeader>

                <twbs:input name="videoName" labelText="Navn"/>
                <twbs:input name="videoId" labelText="YouTube/Vimeo Link">
                    For eksempel: <code>https://www.youtube.com/watch?v=DXUAyRRkI6k</code>
                </twbs:input>
                <twbs:select name="subject" labelText="Emne" list="${subjects}"/>
                <twbs:textArea name="description" labelText="Beskrivelse"/>
                <twbs:checkbox labelText="Synligt for studerende" name="visible"
                               value="${video?.localStatus == NodeStatus.VISIBLE}"/>
                <twbs:button block="true" style="${ButtonStyle.INFO}" id="stopEdit" disabled="true">
                    <fa:icon icon="${FaIcon.UNLOCK}"/> Lås video op
                </twbs:button>
                <hr>
                <twbs:button block="true" style="${ButtonStyle.PRIMARY}" id="start-preview">
                    <fa:icon icon="${FaIcon.STREET_VIEW}"/> Start forhåndsvisning
                </twbs:button>
                <twbs:button block="true" style="${ButtonStyle.SUCCESS}" id="publish-video">
                    <fa:icon icon="${FaIcon.CHECK}"/> Udgiv video
                </twbs:button>
                <p class="help-block">
                    Dette vil gemme videoen og gøre den synlig for alle brugere
                </p>
                <g:if test="${isEditing}">
                    <hr>
                    <twbs:pageHeader><h4>Se også afsnit</h4></twbs:pageHeader>
                    <ul id="similar-resources">
                        <g:each in="${video.similarResources}">
                            <div>
                                <b>Title: ${it.title}</b> <br/>
                                <b>Link: ${it.link}</b>
                                <sdu:ajaxSubmitButton style="${ButtonStyle.DANGER}" class="similar-resource-delete" data-id="${it.id}">
                                    <fa:icon icon="${FaIcon.TRASH}"/>
                                    Slet
                                </sdu:ajaxSubmitButton>
                            </div>
                        </g:each>
                    </ul>
                </g:if>
            </div>
        </div>
    </div>
    <hr>
</g:content>

<g:content key="sidebar-right-below-the-fold">
    <div id="sidebar-right-timeline">
        <h4><fa:icon icon="${FaIcon.VIDEO_CAMERA}"/> Tidslinie</h4>

        <div id="timeline-subjects"></div>
        <br/>
        <twbs:button block="true" style="${ButtonStyle.SUCCESS}" id="addSubject">
            <fa:icon icon="${FaIcon.PLUS}"/>
        </twbs:button>
    </div>
</g:content>

%{-- Templates --}%

<div id="fieldTemplate" class="hide">
    <div class="draggableField" data-id="{0}">
        <span></span>
    </div>
</div>

<div id="questionTemplate" class="hide">
    <twbs:row>
        <twbs:column type="${GridSize.SM}" class="block-link-container question">
            <a href="#" class="admin-timeline-question-link"><span class="block-link"></span></a>
            <h6>{0} <small>{1}</small></h6>
        </twbs:column>
    </twbs:row>
</div>

<div id="subjectTemplate" class="hide">
    <div class="admin-timeline-subject" data-id="{2}">
        <twbs:row>
            <twbs:column type="${GridSize.SM}" class="block-link-container subject">
                <a href="#" class="admin-timeline-subject-link"><span class="block-link"></span></a>
                <h5>{0} <small>{1}</small></h5>
            </twbs:column>
        </twbs:row>
        <div class="admin-time-questions"></div>
    </div>
</div>

%{-- End templates --}%

<g:content key="sidebar-left">
    <div id="tree-container"></div>
</g:content>

<g:content key="layout-script">
    <asset:javascript src="video-creator.js"/>
    <script>
        var baseUrl = "${createLink(absolute:true, uri:'/')}";

        $(document).ready(function () {
            Editor.init();

            <g:if test="${isEditing}">
            $("#videoName").val("${video.name}");
            $("#description").val("${video.description.replace("\n", "\\n")}");
            Editor.displayVideo(${video.videoType}, "${video.youtubeId}");
            Editor.Timeline.setTimeline(${raw(video.timelineJson)});
            Editor.setPublishEndpoint("<g:createLink action="postVideo" params="[edit: video.id]" />");
            Editor.setEditing(${video.id});
            </g:if>
            <g:else>
            Editor.setPublishEndpoint("<g:createLink action="postVideo" />");
            </g:else>
            <g:if test="${subject != null && isEditing}">
            $("#subject").val(${subject.id});
            </g:if>
            <g:if test="${subject != null && !isEditing}">
            $("#subject").val(${subject});
            </g:if>
            Editor.setVideoInfoEndpoint("<g:createLink controller="videoHost" action="info" />");

            var tree = new ManagementTreeView("#tree-container", "${createLink(absolute:true, uri:'/')}");
            tree.init();

            AjaxUtil.registerJSONForm(
                    ".subject-delete-button",
                    "${createLink(action: "deleteSimilarResource")}",
                    function (e) {
                        console.log(e);
                        return {

                        };
                    },
                    {}
            );

        });
    </script>
    <asset:javascript src="./courseManagement/app.js"/>
    <asset:stylesheet src="./vendor/proton/style.min.css"/>
</g:content>
</body>
</html>