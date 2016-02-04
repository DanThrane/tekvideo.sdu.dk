//= require bootstrap-helper

var Editor = {};
(function (exports) {
    var player = null;
    var currentTimeline = [];
    var editingSubject = null;
    var editingQuestion = null;
    var editingField = null;
    var editingFieldIndex = -1;
    var videoId = null;
    var publishEndpoint = null;
    var videoInfoEndpoint = null;
    var isEditing = false;
    var editingId = null;
    var isYouTube = false;

    var scaleHeight;
    var scaleWidth;

    var BASELINE_WIDTH = 640; // Wide 360p (Standard on YouTube)
    var BASELINE_HEIGHT = 360;

    var attributesStack = new CardStack("#attributes-stack");

    function initSize() {
        var maxWidth = -1;
        var maxHeight = -1;
        $("#player").children().each(function (index, element) {
            var $element = $(element);
            var height = $element.height();
            var width = $element.width();
            if (width > maxWidth) maxWidth = width;
            if (height > maxHeight) maxHeight = height;
        });

        scaleHeight = 1 / (maxHeight / BASELINE_HEIGHT);
        scaleWidth = 1 / (maxWidth / BASELINE_WIDTH);
    }


    function setPublishEndpoint(endpoint) {
        publishEndpoint = endpoint;
    }

    function setVideoInfoEndpoint(endpoint) {
        videoInfoEndpoint = endpoint;
    }

    function parseYouTubeID(url) {
        if (url.length == 11) {
            return url;
        }
        var regExp = /^.*(youtu.be\/|v\/|u\/\w\/|embed\/|watch\?v=|&v=)([^#&\?]*).*/;
        var match = url.match(regExp);
        if (match && match[2].length == 11) {
            return {id: match[2], type: true};
        } else {
            return null;
        }
    }

    function parseVimeoID(url) {
        var regExp = /^.*(vimeo\.com\/)(\d+)\??.*/;
        var match = url.match(regExp);
        if (match) {
            return {id: match[2], type: false};
        } else {
            return null;
        }
    }

    function parseVideoID(url) {
        if (url.toLowerCase().indexOf("youtu") != -1) return parseYouTubeID(url);
        else if (url.toLowerCase().indexOf("vimeo") != -1) return parseVimeoID(url);
        return null;
    }

    function displayVideo(type, vidId) {
        $("#videoId").val(vidId);
        videoId = vidId;
        isYouTube = type;
        if (player !== null) {
            Popcorn.destroy(player);
            $("#player").html("");
        }
        var constructor = (isYouTube) ? Popcorn.HTMLYouTubeVideoElement : Popcorn.HTMLVimeoVideoElement;
        var wrapper = constructor("#player");
        wrapper.src = (isYouTube) ?
        "http://www.youtube.com/watch?v=" + videoId + "&controls=0" :
        "http://player.vimeo.com/video/" + videoId;
        player = Popcorn(wrapper);
        player.on("canplay", function() { initSize(); });
    }

    function startEditing() {
        $("#fields").addClass("fields-active");
        $("#stopEdit").removeClass("disabled").removeAttr("disabled")
    }

    function stopEditing() {
        $("#fields").removeClass("fields-active");
        $("#stopEdit").addClass("disabled").attr("disabled", "disabled")
    }

    function setEditing(videoId) {
        isEditing = true;
        editingId = videoId;
    }

    function publishVideo() {
        MainPanel.showPublishing();
        var name = $("#videoName").val();
        var description = $("#description").val();
        var youtubeId = videoId;
        var timelineJson = JSON.stringify(currentTimeline);
        var type = isYouTube;
        var data = {
            name: name,
            youtubeId: youtubeId,
            timelineJson: timelineJson,
            isEditing: isEditing,
            description: description,
            videoType: type,
            subject: $("#subject").val(),
            visible: $("#visible").is(":checked")
        };

        if (isEditing) {
            data.editing = editingId;
        }

        Util.postJson(publishEndpoint, data, {
            success: function (data) {
                $("#publish-success").removeClass("hide");
                console.log("success!");
                console.log(data);
            },
            error: function (data) {
                $("#publish-error").removeClass("hide");
                console.log("error!");
                console.log(data);
            },
            complete: function () {
                $("#publish-spinner").addClass("hide");
            }
        });
    }

    function init() {
        Fields.init();
        Questions.init();
        Preview.init();
        Timeline.init();

        $(window).resize(function () { initSize() });
        setTimeout(function () { initSize(); }, 2000);

        $("#videoId").on('change textInput input', function () {
            var parsed = parseVideoID($(this).val());
            if (parsed !== null) {
                displayVideo(parsed.type, parsed.id);
                $.getJSON(videoInfoEndpoint + "/" + parsed.id + "?type=" + parsed.type, function (data) {
                    $("#videoName").val(data.result.title);
                    $("#description").val(data.result.description);
                });
            }
        });

        $("#stopEdit").click(function () {
            Fields.removeAllFields();
            stopEditing();
        });

        $("#publish-video").click(function () {
            publishVideo();
        });

        $("#publish-try-again").click(function () {
            $("#publish-spinner").removeClass("hide");
            $("#publish-error").addClass("hide");
            MainPanel.showEditor();
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

        var questionStack = new CardStack("#field-type-stack");
        var editor = null;
        var defaultEditorText = null;



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
                    placeFieldAt(domField, field.leftoffset * (1 / scaleWidth), field.topoffset * (1 / scaleHeight));
                }
            }
        }

        function resetExpressionField(value) {
            // Do not attempt to re-use a DOM element already initialized by
            // Mathquill, as this will make the event listeners start crashing
            // causing a total browser freeze.
            var expressionContainer = $("#expression-container");
            expressionContainer.html("<span>" + value + "</span>");
            expressionContainer.find("span").mathquill("editable");
        }

        /**
         * Resets the field options UI to its original state. Each field type
         * should implement some logic here to reset the UI
         */
        function resetFields() {
            // Expressions
            resetExpressionField("");

            // Custom (JS)
            editor.setValue(defaultEditorText);

            // Between
            bootstrap.removeFormValidation($("#between-field-type-card"));
            $("#betweenMinValue").val("");
            $("#betweenMaxValue").val("");

            // Text
            $("#textFieldExact").val("");
        }

        function editField(fieldIndex) {
            // Display the attribute section
            attributesStack.select(ATTRIBUTE_PANEL_MAIN);

            // Set the application state
            editingField = editingQuestion.fields[fieldIndex];
            editingFieldIndex = fieldIndex;

            // Sets the fields in the attribute UI to match our current action
            $(FIELD_NAME).val(editingField.name);
            var fieldTypeId = getIdForFieldAnswer(editingField.answer);
            $(FIELD_TYPE + " option")[fieldTypeId].selected = true;

            // Displays the options associated with the selected field type
            showSelectedFieldTypeAttributes();

            // Load values associated with the field
            switch (fieldTypeId) {
                case FIELD_TYPE_IDS.expression:
                    resetExpressionField(editingField.answer.value);
                    break;
                case FIELD_TYPE_IDS.custom:
                    editor.setValue(editingField.answer.jsValidator);
                    break;
                case FIELD_TYPE_IDS.between:
                    $("#betweenMinValue").val(editingField.answer.min);
                    $("#betweenMaxValue").val(editingField.answer.max);
                    break;
                case FIELD_TYPE_IDS.equal:
                    $("#textFieldIgnoreCase").prop('checked', editingField.answer.ignoreCase);
                    $("#textFieldExact").val(editingField.answer.value);
                    break;
            }
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
                        value: parseTex($("#expression-container").find("span").mathquill("latex"))
                    };
                    break;
                case FIELD_TYPE_IDS.custom:
                    editingField.answer = {
                        type: "custom",
                        jsValidator: editor.getValue()
                    };
                    break;
                case FIELD_TYPE_IDS.between:
                    bootstrap.removeFormValidation($("#between-field-type-card"));
                    var hasErrors = false;
                    var minValueElement = $("#betweenMinValue");
                    var min = parseFloat(minValueElement.val().replace(",", "."));
                    var maxValueElement = $("#betweenMaxValue");
                    var max = parseFloat(maxValueElement.val().replace(",", "."));

                    if (isNaN(min)) {
                        bootstrap.addValidationClass(minValueElement, bootstrap.VALIDATION_ERROR);
                        hasErrors = true;
                    }
                    if (isNaN(max)) {
                        bootstrap.addValidationClass(maxValueElement, bootstrap.VALIDATION_ERROR);
                        hasErrors = true;
                    }
                    if (hasErrors) return;
                    editingField.answer = {
                        type: "between",
                        min: min,
                        max: max
                    };
                    break;
                case FIELD_TYPE_IDS.equal:
                    editingField.answer = {
                        type: "equal",
                        value: $("#textFieldExact").val(),
                        ignoreCase: $("#textFieldIgnoreCase").prop("checked")
                    };
                    console.log(editingField);
                    break;
            }

            clearEditingField();
            Questions.editQuestion(editingSubject, editingQuestion);
            displayAllFields(editingQuestion);
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
            field.css({
                minWidth: 90 * (1 / scaleWidth),
                minHeight: 20 * (1 / scaleHeight)
            });
            $("#fields").append(field);
            startEditing();
            return field;
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
                // Reset the UI to its original state
                resetFields();
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

            editor = ace.edit("editor");
            editor.setTheme("ace/theme/ambiance");
            editor.getSession().setMode("ace/mode/javascript");
            defaultEditorText = editor.getValue();

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
                        elementRect: {top: 0, left: 0, bottom: 1, right: 1}
                    },

                    // call this function on every dragmove event
                    onmove: dragMoveListener
                });

            function dragMoveListener(event) {
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
                    editingField.leftoffset = x * scaleWidth;
                    editingField.topoffset = y * scaleHeight;
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
        var player = null;
        function init() {
            $("#start-preview").click(function () {
                events.disable();
                if (player !== null) player.destroy();
                player = new InteractiveVideoPlayer();
                player.playerElement = $("#preview-player");
                player.wrapperElement = $("#wrapper");
                player.navigationElement = $("#videoNavigation");
                player.checkButton = $("#checkAnswers");

                player.startPlayer(videoId, isYouTube, currentTimeline);
                MainPanel.showPreview();
                $("#attributes-stack").hide();
                $("#sidebar-right-timeline").hide();
            });
            $("#stop-preview").click(function () {
                MainPanel.showEditor();
                $("#attributes-stack").show();
                $("#sidebar-right-timeline").show();
                if (player !== null) player.destroy();
            });
        }

        exports.init = init;
    })(Preview);

    var MainPanel = {};
    (function (exports) {
        var mainStack = new CardStack("#main-panel-stack");
        var sidebarStack = new CardStack("#sidebar-stack");

        function showEditor() {
            mainStack.select(".creator-card");
            sidebarStack.select(".creator-card");
        }

        function showPreview() {
            mainStack.select(".preview-card");
            sidebarStack.select(".preview-card");
        }

        function showPublishing() {
            mainStack.select(".publish-card");
        }

        exports.showEditor = showEditor;
        exports.showPreview = showPreview;
        exports.showPublishing = showPublishing;
    })(MainPanel);

    var Timeline = {};
    (function (exports) {
        function setTimeline(timeline) {
            currentTimeline = timeline;
            sortTimeline();
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
            sortTimeline();
            render();
        }

        function sortTimeline() {
            currentTimeline.sort(function (a, b) {
                return a.timecode - b.timecode;
            });
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
    exports.setPublishEndpoint = setPublishEndpoint;
    exports.setVideoInfoEndpoint = setVideoInfoEndpoint;
    exports.setEditing = setEditing;
})(Editor);
