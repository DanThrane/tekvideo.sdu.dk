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

                    <div class="card-stack" id="field-type-stack">
                        <div class="card-item active" id="no-field-type-card">
                            1
                        </div>
                        <div class="card-item" id="between-field-type-card">
                            2
                        </div>
                        <div class="card-item" id="text-field-type-card">
                            3
                        </div>
                        <div class="card-item" id="userdefined-field-type-card">
                            4
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
        var editingSubject = null;
        var editingQuestion = null;
        var editingField = null;
        var editingFieldIndex = -1;

        var attributesStack = new CardStack("#attributes-stack");

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
            var form = attributesStack.select("#subject-form-card");
            form.find("#subjectName").val(subject.title);
            form.find("#subjectTimecode").val(formatTimestamp(subject.timecode * 1000));
            editingSubject = subject;
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
                    Questions.editQuestion(subject, item);
                });
                questions.append(question);
            });
        }

        function addSubjectEntry(subject) {
            currentTimeline.push(subject);
            renderTimeline();
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

        $("#deleteSubject").click(function () {
            currentTimeline.splice(currentTimeline.indexOf(editingSubject), 1);
            editingSubject = null;
            renderTimeline();
            attributesStack.hideAll();
        });

        $("#subject-form").submit(function (e) {
            e.preventDefault();
            editingSubject.timecode = parseTimecode($("#subjectTimecode").val());
            editingSubject.title = $("#subjectName").val();
            renderTimeline();
        });

        $("#backToQuestion").click(function () {
            Questions.editQuestion(editingSubject, editingQuestion);
            Fields.clearEditingField();
            $(".draggableField.active").removeClass("active");
        });

        $("#stopEdit").click(function () {
            Fields.removeAllFields();
            stopEditing();
        });

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

        function init() {
            Fields.init();
            Questions.init();
        }

        var Questions = {};
        (function (exports) {
            var FIELD_NAME = "#questionName";
            var FIELD_TIMECODE = "#questionTimecode";

            function deleteQuestion(question) {
                var number = editingSubject.questions.indexOf(question);
                if (number !== -1) {
                    editingSubject.questions.splice(number, 1);
                }
            }

            function editQuestion(subject, question) {
                editingSubject = subject;
                editingQuestion = question;
                $("#questionName").val(question.title);
                $("#questionTimecode").val(formatTimestamp(question.timecode * 1000));
                Fields.displayAllFields(question);
                attributesStack.select("#question-form-card");
            }

            function init() {
                $("#question-form").submit(function (e) {
                    e.preventDefault();
                    editingQuestion.title = $(FIELD_NAME).val();
                    editingQuestion.timecode = parseTimecode($(FIELD_TIMECODE).val());
                    renderTimeline();
                });

                $("#addQuestion").click(function () {
                    editingSubject.questions.push({
                        title: "Unavngivet spørgsmål",
                        timecode: editingSubject.timecode,
                        fields: []
                    });
                    renderTimeline();
                });

                $("#deleteQuestion").click(function () {
                    deleteQuestion(editingQuestion);
                });
            }

            exports.init = init;
            exports.editQuestion = editQuestion;
        })(Questions);

        var Fields = {};
        (function (exports) {
            var CARDS_FIELD_TYPE_ATTRIBUTES = ["#no-field-type-card", "#between-field-type-card", "#text-field-type-card",
                "#userdefined-field-type-card", "#expression-field-type-card"];
            var FIELD_TYPES = ["none", "between", "equal", "custom", "expression"];
            var FIELD_TYPE_IDS = {
                "none": 0,
                "between": 1,
                "equal": 2,
                "custom": 3,
                "expression": 4
            };

            var ATTRIBUTE_PANEL_MAIN = "#field-form-card";
            var FIELD_NAME = "#fieldName";
            var FIELD_TYPE = "#fieldType";

            // Expression attributes
            var FIELD_EXPRESSION = "#expression-field";

            var questionStack = new CardStack("#field-type-stack");

            /**
             * Displays the card item which holds attribute for the currently selected field type
             */
            function showSelectedFieldTypeAttributes() {
                questionStack.select(CARDS_FIELD_TYPE_ATTRIBUTES[getSelectedFieldTypeId()]);
            }

            function getSelectedFieldTypeId() {
                return $(FIELD_TYPE)[0].selectedIndex;
            }

            /**
             * Displays all the fields belonging to a specific question
             */
            function displayAllFields(question) {
                removeAllFields();
                console.log(question);
                if (question !== null) {
                    for (var i = 0; i < question.fields.length; i++) {
                        var field = question.fields[i];
                        var domField = createField(i, field);
                        placeFieldAt(domField, field.leftoffset, field.topoffset);
                    }
                }
            }

            function editField(fieldIndex) {
                attributesStack.select(ATTRIBUTE_PANEL_MAIN);
                editingField = editingQuestion.fields[fieldIndex];
                editingFieldIndex = fieldIndex;

                $(FIELD_NAME).val(editingField.name);
                var fieldTypeId = getIdForFieldAnswer(editingField.answer);
                $(FIELD_TYPE + " option")[fieldTypeId].selected = true;
                showSelectedFieldTypeAttributes();

                // TODO Don't hardcode
                switch (fieldTypeId) {
                    case FIELD_TYPE_IDS.expression:
                        var expressionField = $(FIELD_EXPRESSION);
                        if (editingField.answer && editingField.answer.value) {
                            expressionField.text(editingField.answer.value);
                        } else {
                            expressionField.text("");
                        }
                        expressionField.mathquill("editable");
                        break;
                }
            }

            function getIdForFieldAnswer(answer) {
                if (answer === undefined || answer == null) return 0;
                for (var i = 0; i < FIELD_TYPES.length; i++) {
                    if (FIELD_TYPES[i] === answer.type) {
                        return i;
                    }
                }
                return 0;
            }

            function createField(id, fieldObject) {
                console.log("Creating field!");
                var rawTemplate = $("#fieldTemplate").html().format(id, fieldObject.name);
                var field = $(rawTemplate);
                $("#fields").append(field);
                startEditing();
                console.log(field);
                return field;
            }

            function saveField() {
                if (!editingField) return;

                // Movement is saved whenever we move the field around
                editingField.name = $(FIELD_NAME).val();

                switch (getSelectedFieldTypeId()) {
                    case FIELD_TYPE_IDS.none:
                        editingField.answer = {
                            type: "none"
                        };
                        break;
                    case FIELD_TYPE_IDS.expression:
                        editingField.answer = {
                            type: "expression",
                            value: parseTex($(FIELD_EXPRESSION).mathquill("latex"))
                        };
                        break;
                }
                console.log(editingField);

                clearEditingField();
                Questions.editQuestion(editingSubject, editingQuestion);
                displayAllFields(editingQuestion);
            }

            function clearEditingField() {
                editingField = null;
                editingFieldIndex = -1;
            }

            function init() {
                showSelectedFieldTypeAttributes();

                $("#fieldType").change(function () {
                    showSelectedFieldTypeAttributes();
                });

                $(".addField").click(function () {
                    var id = editingQuestion.fields.length;
                    var fieldObject = {
                        name: "field-" + id,
                        topoffset: 0,
                        leftoffset: 0
                    };
                    editingQuestion.fields.push(fieldObject);
                    createField(id, fieldObject);
                });

                $("#field-form").submit(function (e) {
                    e.preventDefault();
                    saveField();
                });

                $(FIELD_EXPRESSION).mathquill("editable");

                initDragging();
            }

            function placeFieldAt(target, x, y) {
                // translate the element
                target.css("transform", "translate(" + x + "px, " + y + "px)");

                // update the position attributes
                target.attr('data-x', x);
                target.attr('data-y', y);
            }

            function initDragging() {
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
                    var x = parseInt((parseInt(target.attr('data-x')) || 0) + event.dx);
                    var y = parseInt((parseInt(target.attr('data-y')) || 0) + event.dy);
                    var targetIndex = parseInt(target.attr("data-id"));

                    placeFieldAt(target, x, y);
                    $(".draggableField").removeClass("active");
                    $(target).addClass("active");

                    if (editingField === null || editingFieldIndex !== targetIndex) {
                        editField(targetIndex);
                    }

                    if (editingField !== null) {
                        editingField.leftoffset = x;
                        editingField.topoffset = y;
                    }
                }
            }

            function removeAllFields() {
                $("#fields").find(".draggableField").remove();
            }

            exports.init = init;
            exports.displayAllFields = displayAllFields;
            exports.removeAllFields = removeAllFields;
            exports.clearEditingField = clearEditingField;
        })(Fields);

        // Initialize the application
        init();
    });
</script>
</body>
</html>