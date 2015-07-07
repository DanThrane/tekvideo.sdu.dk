<%@ page import="dk.danthrane.twbs.GridSize; dk.danthrane.twbs.InputSize; dk.danthrane.twbs.Icon; dk.danthrane.twbs.ButtonSize; dk.sdu.tekvideo.FaIcon; dk.danthrane.twbs.ButtonStyle" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Ny video til ${course.fullName} (${course.name})</title>
    <meta name="layout" content="main" />
    <asset:javascript src="interact.js" />
    <asset:stylesheet src="create_video.css" />
    <script src="https://cdnjs.cloudflare.com/ajax/libs/ace/1.1.9/ace.js"></script>
</head>

<body>
<div class="card-stack" id="main-panel-stack">
    <div class="card-item" id="preview-card">
        <twbs:pageHeader><h3>Forhåndsvisning</h3></twbs:pageHeader>
        <twbs:row>
            <twbs:column cols="9">
                <div id="wrapper">
                    <div id="preview-player" style="width: 800px; height: 600px;"></div>
                </div>
            </twbs:column>
            <twbs:column cols="3">
                <h3>Indhold</h3>

                <ul id="videoNavigation">
                </ul>

                <twbs:button block="true" style="${ButtonStyle.PRIMARY}" id="stop-preview">
                    <fa:icon icon="${FaIcon.STREET_VIEW}" /> Stop forhåndsvisning
                </twbs:button>
            </twbs:column>
        </twbs:row>
        <br>

        <twbs:row>
            <twbs:linkButton elementId="checkAnswers">Tjek svar</twbs:linkButton>
        </twbs:row>
    </div>
    <div class="card-item active" id="creator-card">
        <twbs:pageHeader><h3>Ny video</h3></twbs:pageHeader>
        <twbs:row>
            <twbs:column cols="9">
                <div id="wrapper">
                    <div id="fields"></div>
                    <div id="player" style="width: 800px; height: 600px;"></div>
                </div>
            </twbs:column>
            <twbs:column cols="3">
                <h4>Kontrol panel</h4>
                <twbs:input name="youtubeId" labelText="YouTube Link">
                    For eksempel: <code>https://www.youtube.com/watch?v=DXUAyRRkI6k</code> eller <code>DXUAyRRkI6k</code>
                </twbs:input>
                <twbs:button block="true" style="${ButtonStyle.INFO}" id="stopEdit">
                    <fa:icon icon="${FaIcon.EDIT}" /> Stop redigering
                </twbs:button>
                <twbs:button block="true" style="${ButtonStyle.PRIMARY}" id="start-preview">
                    <fa:icon icon="${FaIcon.STREET_VIEW}" /> Start forhåndsvisning
                </twbs:button>
            </twbs:column>
        </twbs:row>

        <hr>

        <twbs:row>
        %{-- Attributes --}%
            <twbs:column cols="9">
                <div class="card-stack" id="attributes-stack">
                    %{-- Edit subject --}%
                    <div id="subject-form-card" class="card-item">
                        <h4><fa:icon icon="${FaIcon.BOOK}" /> Redigér emne</h4>
                        <twbs:buttonGroup justified="true">
                            <twbs:button id="addQuestion">
                                <fa:icon icon="${FaIcon.PLUS}" /> Tilføj spørgsmål
                            </twbs:button>
                            <twbs:button style="${ButtonStyle.DANGER}" id="deleteSubject">
                                <fa:icon icon="${FaIcon.TRASH}" /> Slet
                            </twbs:button>
                        </twbs:buttonGroup>
                        <hr>
                        <twbs:form id="subject-form">
                            <twbs:input name="subjectName" labelText="Navn" />
                            <twbs:input name="subjectTimecode" labelText="Tidskode">
                                For eksempel: <code>2:20</code>
                            </twbs:input>
                            <twbs:button type="submit" style="${ButtonStyle.SUCCESS}">
                                <fa:icon icon="${FaIcon.CHECK}" />
                                Gem ændringer
                            </twbs:button>
                        </twbs:form>
                    </div>
                    %{-- Edit question --}%
                    <div id="question-form-card" class="card-item">
                        <h4><fa:icon icon="${FaIcon.QUESTION}" /> Redigér spørgsmål</h4>
                        <twbs:buttonGroup justified="true">
                            <twbs:button class="addField">
                                <fa:icon icon="${FaIcon.PLUS}" /> Tilføj felt
                            </twbs:button>
                            <twbs:button style="${ButtonStyle.DANGER}" id="deleteQuestion">
                                <fa:icon icon="${FaIcon.TRASH}" /> Slet
                            </twbs:button>
                        </twbs:buttonGroup>
                        <hr>
                        <twbs:form id="question-form">
                            <twbs:input name="questionName" labelText="Navn" />
                            <twbs:input name="questionTimecode" labelText="Tidskode">
                                For eksempel: <code>2:20</code>
                            </twbs:input>
                            <twbs:button type="submit" style="${ButtonStyle.SUCCESS}">
                                <fa:icon icon="${FaIcon.CHECK}" />
                                Gem ændringer
                            </twbs:button>
                        </twbs:form>
                    </div>
                    %{-- Edit field --}%
                    <div id="field-form-card" class="card-item">
                        <h4><fa:icon icon="${FaIcon.FILE}" />  Redigér felt</h4>
                        <twbs:buttonGroup justified="true">
                            <twbs:button id="backToQuestion">
                                <fa:icon icon="${FaIcon.BACKWARD}" /> Tilbage til spørgsmål
                            </twbs:button>
                            <twbs:button class="addField" style="${ButtonStyle.SUCCESS}">
                                <fa:icon icon="${FaIcon.PLUS}" /> Tilføj nyt felt
                            </twbs:button>
                            <twbs:button style="${ButtonStyle.DANGER}" id="deleteField">
                                <fa:icon icon="${FaIcon.TRASH}" /> Slet
                            </twbs:button>
                        </twbs:buttonGroup>
                        <hr>
                        <twbs:form id="field-form">
                            <twbs:input name="fieldName" labelText="Felt ID">
                                Hvis du bruger et JavaScript felt, så vil du kunne henvise til feltet ved hjælp af dette ID
                            </twbs:input>
                            <twbs:select name="fieldType" labelText="Spørgsmåls type"
                                         list="${["Ingen", "Mellem", "Tekst" , "Brugerdefineret (JavaScript)",
                                                  "Matematisk udtryk"]}" />

                            <div class="card-stack" id="field-type-stack">
                                <div class="card-item active" id="no-field-type-card">
                                </div>
                                <div class="card-item" id="between-field-type-card">
                                    2
                                </div>
                                <div class="card-item" id="text-field-type-card">
                                    3
                                </div>
                                <div class="card-item" id="userdefined-field-type-card">
                                    <div id="editor">function validator(value) {
    return value === "6" || utilCheck();
}

// Utility functions can be defined too
function utilCheck() {
    // It is possible to reference other fields using their ID
    var valueOfOtherField = $("#field-2").val();
    return valueOfOtherField === "hello";
}

return validator; // Return the validator function</div>
                                </div>
                                <div class="card-item" id="expression-field-type-card">
                                    <label>Udtryk</label> <br />
                                    <span id="expression-field"></span>
                                    <br />
                                </div>
                            </div>
                            <twbs:button type="submit" block="true" style="${ButtonStyle.SUCCESS}" id="field-save">
                                <fa:icon icon="${FaIcon.CHECK}" />
                                Gem ændringer
                            </twbs:button>
                        </twbs:form>
                    </div>
                </div>
            </twbs:column>
        %{-- Timeline --}%
            <twbs:column cols="3">
                <h4><fa:icon icon="${FaIcon.VIDEO_CAMERA}" /> Tidslinie</h4>
                <div id="timeline-subjects"></div>
                <br />
                <twbs:button block="true" style="${ButtonStyle.SUCCESS}" id="addSubject">
                    <fa:icon icon="${FaIcon.PLUS}" />
                </twbs:button>
            </twbs:column>
        </twbs:row>
    </div>
</div>

%{-- Templates --}%

<div id="fieldTemplate" class="hide">
    <div class="draggableField" data-id="{0}">
        <strong>{1}</strong>
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

<asset:javascript src="video-creator.js" />
<script>
    $(document).ready(function () {
        Editor.init();

        <g:if test="${params.test}">
        Editor.displayVideo("eiSfEP7gTRw");

        Editor.Timeline.setTimeline([{
            title: "Introduktion",
            timecode: 0,
            questions: []
        }, {
            title: "Subtraktion",
            timecode: 140,
            questions: [
                {
                    title: "Subtraktion med tal",
                    timecode: 155, // 2:35 - 5
                    fields: [
                        {
                            name: "secondq",
                            answer: {
                                type: "expression",
                                value: "5"
                            },
                            topoffset: 170,
                            leftoffset: 290
                        }
                    ]
                }
            ]
        }]);
        </g:if>
    });
</script>
</body>
</html>