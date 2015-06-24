<%@ page import="dk.danthrane.twbs.GridSize; dk.danthrane.twbs.InputSize; dk.danthrane.twbs.Icon; dk.danthrane.twbs.ButtonSize; dk.sdu.tekvideo.FaIcon; dk.danthrane.twbs.ButtonStyle" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Ny video til ${course.fullName} (${course.name})</title>
    <meta name="layout" content="main" />
    <asset:javascript src="interact.js" />
</head>

<body>
<twbs:row>
    <twbs:column>
        <twbs:pageHeader><h3>Ny video</h3></twbs:pageHeader>
    </twbs:column>
</twbs:row>
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
    </twbs:column>
</twbs:row>

<hr>

<twbs:row>
    %{-- Attributes --}%
    <twbs:column cols="9">
        %{-- Edit subject --}%
        <div id="subject-form-card" class="layout-card hide">
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
        <div id="question-form-card" class="layout-card hide">
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
        <div id="field-form-card" class="layout-card hide">
            <h4><fa:icon icon="${FaIcon.FILE}" />  Redigér felt</h4>
            <twbs:buttonGroup justified="true">
                <twbs:button id="backToQuestion">
                    <fa:icon icon="${FaIcon.BACKWARD}" /> Tilbage til spørgsmål
                </twbs:button>
                <twbs:button class="addField" style="${ButtonStyle.SUCCESS}">
                    <fa:icon icon="${FaIcon.PLUS}" /> Tilføj nyt felt
                </twbs:button>
                <twbs:button style="${ButtonStyle.DANGER}" id="deleteQuestion">
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
            </twbs:form>
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

%{-- Templates --}%

<div id="fieldTemplate" class="hide">
    <div class="draggableField" data-id="{0}">
        <strong>{1}</strong>
    </div>
</div>

<div id="questionTemplate" class="hide">
    <twbs:row>
        <twbs:column cols="10" type="${GridSize.SM}" class="block-link-container">
            <a href="#" class="admin-timeline-question-link"><span class="block-link"></span></a>
            <h6>{0} <small>{1}</small></h6>
        </twbs:column>
        <twbs:column cols="2" type="${GridSize.SM}">
            <div class="pull-right admin-subject-margin-fix">
                <twbs:dropdownToggle size="${ButtonSize.XTRA_SMALL}">
                    <twbs:dropdownMenu>
                        <twbs:dropdownItem url="#">
                            <fa:icon icon="${FaIcon.FONT}" />
                            Omdøb
                        </twbs:dropdownItem>
                        <twbs:dropdownItem url="#">
                            <fa:icon icon="${FaIcon.TRASH}" />
                            Slet
                        </twbs:dropdownItem>
                    </twbs:dropdownMenu>
                </twbs:dropdownToggle>
            </div>
        </twbs:column>
    </twbs:row>
</div>

<div id="subjectTemplate" class="hide">
    <div class="admin-timeline-subject" data-id="{2}">
        <twbs:row>
            <twbs:column cols="10" type="${GridSize.SM}" class="block-link-container">
                <a href="#" class="admin-timeline-subject-link"><span class="block-link"></span></a>
                <h5>{0} <small>{1}</small></h5>
            </twbs:column>
            <twbs:column cols="2" type="${GridSize.SM}">
                <div class="pull-right admin-subject-margin-fix">
                    <twbs:dropdownToggle size="${ButtonSize.XTRA_SMALL}">
                        <twbs:dropdownMenu>
                            <twbs:dropdownItem url="#">
                                <fa:icon icon="${FaIcon.PLUS}" />
                                Tilføj spørgsmål
                            </twbs:dropdownItem>
                            <twbs:dropdownItem url="#">
                                <fa:icon icon="${FaIcon.FONT}" />
                                Omdøb
                            </twbs:dropdownItem>
                            <twbs:dropdownDivider />
                            <twbs:dropdownItem url="#">
                                <fa:icon icon="${FaIcon.TRASH}" />
                                Slet
                            </twbs:dropdownItem>
                        </twbs:dropdownMenu>
                    </twbs:dropdownToggle>
                </div>
            </twbs:column>
        </twbs:row>
        <div class="admin-time-questions"></div>
    </div>
</div>

%{-- End templates --}%

<script>
    $(document).ready(function () {
        var player = null;
        var currentTimeline = [];
        var editing = null;
        var editingQuestion = null;
        var editingField = null;
        var id = 0;

        function parseYouTubeID(url) {
            if (url.length == 11) {
                return url;
            }
            var regExp = /^.*(youtu.be\/|v\/|u\/\w\/|embed\/|watch\?v=|\&v=)([^#\&\?]*).*/;
            var match = url.match(regExp);
            if (match && match[2].length == 11) {
                return match[2];
            } else {
                return null;
            }
        }

        function parseTimecode(t) {
            var split = t.split(":");
            if (split.length == 3) {
                return parseInt(split[0]) * 60 * 60 + parseInt(split[1]) * 60 + parseInt(split[2]);
            } else if (split.length == 2) {
                return parseInt(split[0]) * 60 + parseInt(split[1]);
            } else if (split.length == 1) {
                return parseInt(t);
            } else {
                return null;
            }
        }

        function formatTimestamp(t) {
            var timestamp = new Date(t);
            return "{0}:{1}".format(
                    timestamp.getMinutes().toString().paddingLeft("00"),
                    timestamp.getSeconds().toString().paddingLeft("00")
            );
        }

        function renderTimeline() {
            var timeline = $("#timeline-subjects");
            timeline.html("");
            currentTimeline.forEach(function (subject) {
                var rawTemplate = $("#subjectTemplate").html();
                var template = $(rawTemplate.format(
                        subject.title,
                        formatTimestamp(subject.timecode * 1000),
                        42));
                template.find(".admin-timeline-subject-link").click(function (e) {
                    e.preventDefault();
                    player.currentTime(subject.timecode);
                    editSubject(subject);
                });

                insertQuestions(template, subject);
                timeline.append(template);
            });
        }

        function editSubject(subject) {
            var form = displayCard("#subject-form-card");
            form.find("#subjectName").val(subject.title);
            form.find("#subjectTimecode").val(formatTimestamp(subject.timecode * 1000));
            editing = subject;
        }

        function insertQuestions(template, subject) {
            var questions = template.find(".admin-time-questions");
            subject.questions.forEach(function (item) {
                var rawTemplate = $("#questionTemplate").html()
                        .format(item.title, formatTimestamp(item.timecode * 1000));
                var question = $(rawTemplate);

                question.find(".admin-timeline-question-link").click(function (e) {
                    e.preventDefault();
                    player.currentTime(item.timecode);
                    editQuestion(subject, item);
                });
                questions.append(question);
            });
        }

        function editQuestion(subject, question) {
            editing = subject;
            editingQuestion = question;
            $("#questionName").val(question.title);
            $("#questionTimecode").val(formatTimestamp(question.timecode * 1000));
            displayCard("#question-form-card");
        }

        function addSubjectEntry(subject) {
            currentTimeline.push(subject);
            renderTimeline();
        }

        function displayCard(selector) {
            hideAllCards();
            return $(selector).removeClass("hide");
        }

        function hideAllCards() {
            $(".layout-card").addClass("hide");
        }

        function displayVideo(youtubeId) {
            if (player !== null) {
                Popcorn.destroy(player);
                $("#player").html("");
            }
            var wrapper = Popcorn.HTMLYouTubeVideoElement("#player");
            wrapper.src = "http://www.youtube.com/watch?v=" + youtubeId + "&controls=0";
            player = Popcorn(wrapper);
        }

        function startEditing() {
            $("#fields").addClass("fields-active");
        }

        function stopEditing() {
            $("#fields").removeClass("fields-active");
        }

        $("#youtubeId").on('change textInput input', function () {
            var id = parseYouTubeID($(this).val());
            if (id !== null) {
                displayVideo(id);
            }
        });

        $("#addSubject").click(function () {
            var subject = {
                title: "Unavngivet",
                timecode: parseInt(player.currentTime()),
                questions: []
            };
            addSubjectEntry(subject);
            editSubject(subject);
        });

        $("#addQuestion").click(function () {
            editing.questions.push({
                title: "Unavngivet spørgsmål",
                timecode: editing.timecode,
                fields: []
            });
            renderTimeline();
        });

        $("#deleteSubject").click(function () {
            currentTimeline.splice(currentTimeline.indexOf(editing), 1);
            editing = null;
            renderTimeline();
            hideAllCards();
        });

        $("#subject-form").submit(function (e) {
            e.preventDefault();
            editing.timecode = parseTimecode($("#subjectTimecode").val());
            editing.title = $("#subjectName").val();
            renderTimeline();
        });

        $(".addField").click(function () {
            var fieldObject = {
                name: "field-" + id,
                topoffset: 0,
                leftoffset: 0
            };
            editingQuestion.fields.push(fieldObject);
            var rawTemplate = $("#fieldTemplate").html().format(id, fieldObject.name);
            var field = $(rawTemplate);
            $("#fields").append(field);
            startEditing();
            id++;
        });

        $("#deleteQuestion").click(function () {
            var number = editing.questions.indexOf(editingQuestion);
            if (number !== -1) {
                editing.questions.splice(number, 1);
            }
        });

        $("#question-form").submit(function (e) {
            e.preventDefault();
            // TODO
            renderTimeline();
        });

        $("#backToQuestion").click(function () {
            editQuestion(editing, editingQuestion);
            editingField = null;
            $(".draggableField.active").removeClass("active");
        });

        $("#stopEdit").click(function () {
            $(".draggableField").remove();
            stopEditing();
        });

        // target elements with the "draggable" class
        interact('.draggableField')
                .draggable({
                    // enable inertial throwing
                    inertia: true,
                    // keep the element within the area of it's parent
                    restrict: {
                        restriction: "parent",
                        endOnly: true,
                        elementRect: { top: 0, left: 0, bottom: 1, right: 1 }
                    },

                    // call this function on every dragmove event
                    onmove: dragMoveListener
                });

        function dragMoveListener (event) {
            var target = $(event.target);
            var x = (parseFloat(target.attr('data-x')) || 0) + event.dx;
            var y = (parseFloat(target.attr('data-y')) || 0) + event.dy;

            // translate the element
            target.css("transform", "translate(" + x + "px, " + y + "px)");

            // update the posiion attributes
            target.attr('data-x', x);
            target.attr('data-y', y);

            $(".draggableField").removeClass("active");
            $(target).addClass("active");

            if (editingField === null) {
                displayCard("#field-form-card");
            }
        }

        <g:if test="${params.test}">
        $("#youtubeId").val("eiSfEP7gTRw");
        displayVideo("eiSfEP7gTRw");
        addSubjectEntry({
            title: "Introduktion",
            timecode: 0,
            questions: []
        });

        addSubjectEntry({
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
        });
        </g:if>

    });
</script>
</body>
</html>