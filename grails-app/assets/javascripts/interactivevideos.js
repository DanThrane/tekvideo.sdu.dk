var ivids = {};

(function(ivids) {
    var player = null;
    var timeline;
    var videoId;
    var isYouTube;
    var questions = [];

    var selector;

    var BASELINE_WIDTH = 800;
    var BASELINE_HEIGHT = 600;
    var scaleWidth = 1;
    var scaleHeight = 1;

    function bootstrap(playerSelector, vidId, videoType, tline) {
        selector = playerSelector;
        timeline = tline;
        isYouTube = videoType;
        videoId = vidId;

        if (player !== null) {
            Popcorn.destroy(player);
            $(playerSelector).html("");
            removeAllQuestions();
        }

        if (tline !== undefined) {
            // The time line may be undefined, in this case the video should simply be displayed
            initQuestions();
            buildNavigation();
        }

        var constructor = (isYouTube) ? Popcorn.HTMLYouTubeVideoElement : Popcorn.HTMLVimeoVideoElement;
        var wrapper = constructor(playerSelector);
        wrapper.src = (isYouTube) ?
        "http://www.youtube.com/watch?v=" + videoId :
        "http://player.vimeo.com/video/" + videoId;
        player = Popcorn(wrapper);

        player.on("timeupdate", handleTimeUpdate);
        player.on("seeked", handleSeeked);
        player.on("play", function() {
            removeAllQuestions(); // Remove all questions when we continue playing
        });
        player.on("pause", function() {
            events.emit({
                kind: "PAUSE_VIDEO",
                video: document.location.href,
                timecode: player.currentTime()
            }, true);
        });
        player.on("loadstart", function() {
            initializeSize();
            console.log("loadstart?");
        });
        $(window).resize(function () { initializeSize() });
        setTimeout(function() { initializeSize(); }, 2000);
        initEventHandlers();
    }

    function initializeSize() {
        var maxWidth = -1;
        var maxHeight = -1;
        $(selector).children().each(function (index, element) {
            var $element = $(element);
            var height = $element.height();
            var width = $element.width();
            if (width > maxWidth) maxWidth = width;
            if (height > maxHeight) maxHeight = height;
        });
        console.log(maxWidth);
        console.log(maxHeight);
        $("#wrapper").width(maxWidth).height(maxHeight);
        scaleHeight = maxHeight / BASELINE_HEIGHT;
        scaleWidth = maxWidth / BASELINE_WIDTH;
        console.log(scaleHeight);
        console.log(scaleWidth);
    }

    ivids.initializeSize = initializeSize;

    function handleSeeked() {
        for (var i = questions.length - 1; i >= 0; i--) {
            questions[i].visible = false;
            questions[i].shown = false;
        }
        hideAllFields();
        removeAllQuestions();
        handleTimeUpdate();
    }

    function handleTimeUpdate() {
        var timestamp = player.currentTime();
        for (var i = questions.length - 1; i >= 0; i--) {
            var q = questions[i];
            if (q.timecode !== Math.round(timestamp) && q.visible) {
                removeAllQuestions();
                q.visible = false;
            } else if (q.timecode === Math.round(timestamp) && !q.visible && !q.shown) {
                // Will cause a seek event, which in turn will reset everything
                player.pause();
                q.visible = true;
                q.shown = true;
                for(var k = 0; k < q.fields.length; k++) {
                    var field = q.fields[k];
                    placeInputField(
                        field.name,
                        field.answer,
                        field.topoffset,
                        field.leftoffset);
                }
            }
        }
    }

    function initEventHandlers() {
        $("#checkAnswers").click(function (e) {
            e.preventDefault();
            var question = getVisibleQuestion();
            for (var i = 0; i < question.fields.length; i++) {
                var field = question.fields[i];
                var input = $("#" + field.name);
                var val = parseTex(input.mathquill("latex"));
                input.removeClass("correct error");
                var correct = validateAnswer(field, val);
                if (correct) {
                    input.addClass("correct");
                } else {
                    input.addClass("error");
                }
                if (val.length > 0) {
                    events.emit({
                        kind: "ANSWER_QUESTION",
                        answer: val,
                        correct: correct
                    });
                }
            }
            events.flush();
        });
        $("#frameOverlay").mousemove(function (event) {
            var parentOffset = $(this).parent().offset();
            var x = parseInt(event.pageX - parentOffset.left);
            var y = parseInt(event.pageY - parentOffset.top);
            var msg = x + ", " + y;
            $("#cursorPosition").text(msg);
        });
    }

    function initQuestions() {
        for (var i = timeline.length - 1; i >= 0; i--) {
            var item = timeline[i];
            questions = questions.concat(item.questions);
        }
    }

    function buildNavigation() {
        var nav = $("#videoNavigation");
        nav.html("<ul></ul>");
        for (var i = 0; i < timeline.length; i++) {
            var item = timeline[i];
            var nodeId = "navitem" + String(i);
            var node = $(createNavItem(item, nodeId));

            nav.append(node);
            $("#" + nodeId).click(handleNavigationClick(item));

            if (item.questions.length > 0) {
                var list = $("<ul></ul>");
                nav.append(list);
                for (var j = 0; j < item.questions.length; j++) {
                    var question = item.questions[j];
                    var questionId = "navitem" + String(i) + String(j);
                    var questionNode = $(createNavItem(question, questionId));

                    list.append(questionNode);
                    $("#" + questionId).click(handleNavigationClick(question));
                }
            }
        }
    }

    function handleNavigationClick(item) {
        return function(e) {
            var skippedAt = player.currentTime();
            e.preventDefault();
            player.currentTime(item.timecode);
            events.emit({
                kind: "SKIP_TO_CONTENT",
                video: document.location.href,
                label: item.title,
                videoTimeCode: player.currentTime(),
                skippedAt: skippedAt
            }, true);
        };
    }

    /**
     * @brief               Places a single input field used in questions.
     * @details
     *
     * @param fieldId       Type: String. The ID of the field.
     * @param evalFunction  Type: (string) => boolean. The function used to evaluate answers.
     * @param offsetTop     Type: Integer. Y offset relative to top-left corner of player.
     * @param offsetLeft    Type: Integer. X offset relative to top-left corner of player.
     */
    function placeInputField(fieldId, evalFunction, offsetTop, offsetLeft) {
        $("#wrapper").append(createInputField(fieldId));
        var field = $("#" + fieldId);
        field.css({
            position: "absolute",
            top: (offsetTop * scaleHeight) + "px",
            left: (offsetLeft * scaleWidth + 15) + "px",
            minWidth: 90 * scaleWidth,
            minHeight: 20 * scaleHeight
        });
        field.mathquill("editable");
    }

    function validateAnswer(field, value) {
        var answer = field.answer;
        switch (answer.type) {
            case "expression":
                var expectedValue = KAS.parse(answer.value);
                var givenAnswer = KAS.parse(value);
                var result = KAS.compare(expectedValue.expr, givenAnswer.expr, answer.options);
                return result.equal;
            case "between":
                var floatVal = parseFloat(value);
                return floatVal >= answer.min && floatVal <= answer.max;
            case "equal":
                if (answer.ignoreCase) {
                    value = value.toLowerCase();
                    answer.value = answer.value.toLowerCase();
                }
                console.log(answer);
                console.log(value);
                console.log(answer.value);
                console.log(answer.ignoreCase);
                return value === answer.value;
            case "in-list":
                return answer.value.indexOf(value) >= 0;
            case "in-expression-list":
                var givenAnswer = KAS.parse(value);
                for (var i = 0; i < answer.value.length; i++) {
                    var expr = KAS.parse(answer.value[i]); // TODO Answer should be cached
                    var result = KAS.compare(expr.expr, givenAnswer.expr, answer.options);
                    if (result.equal) return true;
                }
                return false;
            case "custom":
                // Custom validators are hopefully just a temporary feature, that is never going to be needed.
                // So much wrong with the following code snippet.
                if (answer.validator === undefined) {
                    var id = "temp_eval_func";
                    answer.validator = eval("function " + id + "() {" + answer.jsValidator + "\n}" + id + "();");
                }
                return answer.validator(value);
        }
    }

    function getVisibleQuestion() {
        for (var i = 0; i < timeline.length; i++) {
            var item = timeline[i];
            for (var j = 0; j < item.questions.length; j++) {
                var question = item.questions[j];
                if (question.visible) {
                    return question;
                }
            }
        }
        return null;
    }

    function removeAllQuestions() {
        $("#wrapper").find(".question").remove();
        hideAllFields();
    }

    function hideAllFields() {
        for (var i = questions.length - 1; i >= 0; i--) {
            questions[i].visible = false;
        }
    }

    function createInputField(id) {
        return '<span class="question" id="' + id + '"></span>';
    }

    function createNavItem(item, id) {
        return '<li><a href="#" id="' + id + '">' + formatTime(item.timecode) +  ' - ' +  item.title + '</a></li>';
    }

    function formatTimeUnit(unit) {
        if (unit < 10) return "0" + Math.floor(unit);
        return Math.floor(unit);
    }

    /**
     * @brief       Formats time into a human readable string.
     * @details     The returned string will be in the format [DD:][HH:]MM:SS
     *
     * @param time  number Time in seconds.
     * @return      string Time as a string
     */
    function formatTime(time) {
        var totalSecs = time;
        var totalMins = totalSecs / 60;
        var totalHours = totalMins / 60;
        var totalDays = totalHours / 24;

        var secs = totalSecs % 60;
        var mins = totalMins % 60;
        var hours = totalHours % 24;
        var result = "";

        if (totalDays >= 1) result += formatTimeUnit(totalDays) + ":";
        if (hours >= 1) result += formatTimeUnit(hours) + ":";
        result += formatTimeUnit(mins) + ":";
        result += formatTimeUnit(secs);
        return result;
    }

    ivids.bootstrap = bootstrap;
    ivids.player = function() { return player; };
    return ivids;
})(ivids);