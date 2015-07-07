var Editor = {};
(function (exports) {
    var player = null;
    var currentTimeline = [];
    var editingSubject = null;
    var editingQuestion = null;
    var editingField = null;
    var editingFieldIndex = -1;
    var videoId = null;

    var attributesStack = new CardStack("#attributes-stack");

    function parseYouTubeID(url) {
        if (url.length == 11) {
            return url;
        }
        var regExp = /^.*(youtu.be\/|v\/|u\/\w\/|embed\/|watch\?v=|&v=)([^#&\?]*).*/;
        var match = url.match(regExp);
        if (match && match[2].length == 11) {
            return match[2];
        } else {
            return null;
        }
    }

    function displayVideo(youtubeId) {
        $("#youtubeId").val(youtubeId);
        videoId = youtubeId;
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

    function init() {
        Fields.init();
        Questions.init();
        Preview.init();
        Timeline.init();

        $("#youtubeId").on('change textInput input', function () {
            var id = parseYouTubeID($(this).val());
            if (id !== null) {
                displayVideo(id);
            }
        });

        $("#stopEdit").click(function () {
            Fields.removeAllFields();
            stopEditing();
        });
    }

    var Questions = {};
    (function (exports) {
        var FIELD_NAME = "#questionName";
        var FIELD_TIMECODE = "#questionTimecode";

        function deleteQuestion(question) {
            var number = editingSubject.questions.indexOf(question);
            if (number !== -1) {
                editingSubject.questions.splice(number, 1);
                Timeline.render();
            }
        }

        function editQuestion(subject, question) {
            Fields.stopEditingFields();
            editingSubject = subject;
            editingQuestion = question;
            $("#questionName").val(question.title);
            $("#questionTimecode").val(Timeline.formatTimestamp(question.timecode * 1000));
            Fields.displayAllFields(question);
            attributesStack.select("#question-form-card");
        }

        function init() {
            $("#question-form").submit(function (e) {
                e.preventDefault();
                editingQuestion.title = $(FIELD_NAME).val();
                editingQuestion.timecode = Timeline.parseTimecode($(FIELD_TIMECODE).val());
                Timeline.render();
            });

            $("#addQuestion").click(function () {
                editingSubject.questions.push({
                    title: "Unavngivet spørgsmål",
                    timecode: editingSubject.timecode,
                    fields: []
                });
                Timeline.render();
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
        var editor = null;

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
                case FIELD_TYPE_IDS.custom:
                    if (editingField.answer && editingField.answer.jsValidator) {
                        editor.setValue(editingField.answer.jsValidator);
                    }
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
            var rawTemplate = $("#fieldTemplate").html().format(id, fieldObject.name);
            var field = $(rawTemplate);
            $("#fields").append(field);
            startEditing();
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
                case FIELD_TYPE_IDS.custom:
                    editingField.answer = {
                        type: "custom",
                        jsValidator: editor.getValue()
                    };
                    break;
            }

            clearEditingField();
            Questions.editQuestion(editingSubject, editingQuestion);
            displayAllFields(editingQuestion);
        }

        function clearEditingField() {
            editingField = null;
            editingFieldIndex = -1;
        }

        function deleteField(field) {
            var number = editingQuestion.fields.indexOf(field);
            if (number !== -1) {
                editingQuestion.fields.splice(number, 1);
                attributesStack.select(ATTRIBUTE_PANEL_MAIN);
                displayAllFields(editingQuestion);
                clearEditingField();
            }
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

            $("#deleteField").click(function () {
                deleteField(editingField);
            });

            $("#field-form").submit(function (e) {
                e.preventDefault();
                saveField();
            });

            $(FIELD_EXPRESSION).mathquill("editable");

            editor = ace.edit("editor");
            editor.setTheme("ace/theme/ambiance");
            editor.getSession().setMode("ace/mode/javascript");

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

        function stopEditingFields() {
            stopEditing();
            removeAllFields();
            clearEditingField();
        }

        function removeAllFields() {
            $("#fields").find(".draggableField").remove();
        }

        exports.init = init;
        exports.displayAllFields = displayAllFields;
        exports.removeAllFields = removeAllFields;
        exports.clearEditingField = clearEditingField;
        exports.stopEditingFields = stopEditingFields;
    })(Fields);

    var Preview = {};
    (function (exports) {
        function init() {
            $("#start-preview").click(function () {
                events.disable();
                ivids.bootstrap("#preview-player", videoId, currentTimeline);
                MainPanel.showPreview();
            });
            $("#stop-preview").click(function () {
                MainPanel.showEditor();
            });
        }

        exports.init = init;
    })(Preview);

    var MainPanel = {};
    (function (exports) {
        var mainStack = new CardStack("#main-panel-stack");

        function showEditor() {
            mainStack.select("#creator-card");
        }

        function showPreview() {
            mainStack.select("#preview-card");
        }

        exports.showEditor = showEditor;
        exports.showPreview = showPreview;
    })(MainPanel);

    var Timeline = {};
    (function (exports) {
        function setTimeline(timeline) {
            currentTimeline = timeline;
            render();
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

        function render() {
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
                    $(".question.active,.subject.active").removeClass("active");
                    $(this).parent().addClass("active");
                    console.log($(this));
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
            Fields.stopEditingFields();
        }

        function insertQuestions(template, subject) {
            var questions = template.find(".admin-time-questions");
            subject.questions.forEach(function (item) {
                var rawTemplate = $("#questionTemplate").html()
                    .format(item.title, formatTimestamp(item.timecode * 1000));
                var question = $(rawTemplate);

                question.find(".admin-timeline-question-link").click(function (e) {
                    e.preventDefault();
                    $(".question.active,.subject.active").removeClass("active");
                    $(this).parent().addClass("active");
                    player.currentTime(item.timecode);
                    Questions.editQuestion(subject, item);
                });
                questions.append(question);
            });
        }

        function addSubjectEntry(subject) {
            currentTimeline.push(subject);
            render();
        }

        function init() {
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
                render();
                attributesStack.hideAll();
            });

            $("#subject-form").submit(function (e) {
                e.preventDefault();
                editingSubject.timecode = parseTimecode($("#subjectTimecode").val());
                editingSubject.title = $("#subjectName").val();
                render();
            });

            $("#backToQuestion").click(function () {
                Questions.editQuestion(editingSubject, editingQuestion);
                Fields.clearEditingField();
                $(".draggableField.active").removeClass("active");
            });
        }

        exports.init = init;
        exports.render = render;
        exports.formatTimestamp = formatTimestamp;
        exports.parseTimecode = parseTimecode;
        exports.setTimeline = setTimeline;
    })(Timeline);

    exports.init = init;
    exports.Fields = Fields;
    exports.Questions = Questions;
    exports.Preview = Preview;
    exports.Timeline = Timeline;
    exports.displayVideo = displayVideo;
})(Editor);
