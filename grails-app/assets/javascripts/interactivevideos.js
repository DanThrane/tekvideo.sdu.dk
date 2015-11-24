var InteractiveVideoPlayer = (function () {
    var BASELINE_WIDTH = 640; // Wide 360p (Standard on YouTube)
    var BASELINE_HEIGHT = 360;

    function InteractiveVideoPlayer(selector) {
        this.selector = selector;
    }

    InteractiveVideoPlayer.prototype.initPlayer = function (videoId, isYouTube, timeline) {
        var self = this;
        this.timeline = timeline;
        this.isYouTube = isYouTube;
        this.videoId = videoId;
        this.questions = [];

        this.initQuestions();
        this.buildNavigation();

        var constructor = (this.isYouTube) ? Popcorn.HTMLYouTubeVideoElement : Popcorn.HTMLVimeoVideoElement;
        var wrapper = constructor(this.selector);
        wrapper.src = (this.isYouTube) ?
        "http://www.youtube.com/watch?v=" + videoId + "&controls=0" :
        "http://player.vimeo.com/video/" + videoId;
        this.player = Popcorn(wrapper);

        this.player.play();
        this.player.on("timeupdate", function () { self.handleTimeUpdate(); });
        this.player.on("seeked", function () { self.handleSeeked(); });
        this.player.on("play", function () { self.removeAllQuestions() });
        this.player.on("pause", function () {
            events.emit({
                kind: "PAUSE_VIDEO",
                video: document.location.href,
                timecode: self.player.currentTime()
            }, true);
        });

        this.player.on("loadstart", function () { self.initializeSize(); });
        $(window).resize(function () { self.initializeSize(); });
        setTimeout(function () { self.initializeSize(); }, 2000);
        this.initEventHandlers();
    };

    InteractiveVideoPlayer.prototype.destroy = function () {
        Popcorn.destroy(player);
        $(this.selector).html("");
        this.removeAllQuestions();
    };

    InteractiveVideoPlayer.prototype.initializeSize = function () {
        var maxWidth = -1;
        var maxHeight = -1;
        $(this.selector).children().each(function (index, element) {
            var $element = $(element);
            var height = $element.height();
            var width = $element.width();
            if (width > maxWidth) maxWidth = width;
            if (height > maxHeight) maxHeight = height;
        });
        $("#wrapper").width(maxWidth).height(maxHeight);
        this.scaleHeight = maxHeight / BASELINE_HEIGHT;
        this.scaleWidth = maxWidth / BASELINE_WIDTH;
    };

    InteractiveVideoPlayer.prototype.handleSeeked = function () {
        for (var i = this.questions.length - 1; i >= 0; i--) {
            this.questions[i].visible = false;
            this.questions[i].shown = false;
        }
        this.hideAllFields();
        this.removeAllQuestions();
        this.handleTimeUpdate();
    };

    InteractiveVideoPlayer.prototype.handleTimeUpdate = function () {
        var timestamp = this.player.currentTime();
        for (var i = this.questions.length - 1; i >= 0; i--) {
            var q = this.questions[i];
            if (q.timecode !== Math.round(timestamp) && q.visible) {
                this.removeAllQuestions();
                q.visible = false;
            } else if (q.timecode === Math.round(timestamp) && !q.visible && !q.shown) {
                // Will cause a seek event, which in turn will reset everything
                this.player.pause();
                q.visible = true;
                q.shown = true;
                for (var k = 0; k < q.fields.length; k++) {
                    var field = q.fields[k];
                    this.placeInputField(
                        field.name,
                        field.answer,
                        field.topoffset,
                        field.leftoffset);
                }
            }
        }
    };

    InteractiveVideoPlayer.prototype.initEventHandlers = function () {
        var self = this;
        $("#checkAnswers").click(function (e) {
            e.preventDefault();
            var questionID = self.getVisibleQuestionID();
            var question = self.getVisibleQuestion();
            for (var i = 0; i < question.fields.length; i++) {
                var field = question.fields[i];
                var input = $("#" + field.name);
                var val = parseTex(input.mathquill("latex"));
                input.removeClass("correct error");
                var correct = self.validateAnswer(field, val);
                if (correct) {
                    input.addClass("correct");
                } else {
                    input.addClass("error");
                }
                if (val.length > 0) {
                    events.emit({
                        kind: "ANSWER_QUESTION",
                        answer: val,
                        correct: correct,
                        subject: questionID[0],
                        question: questionID[1],
                        field: i
                    });
                }
            }
            events.flush();
        });
    };

    InteractiveVideoPlayer.prototype.handleNavigationClick = function (item) {
        var self = this;
        return function (e) {
            var skippedAt = self.player.currentTime();
            e.preventDefault();
            self.player.currentTime(item.timecode);
            events.emit({
                kind: "SKIP_TO_CONTENT",
                video: document.location.href,
                label: item.title,
                videoTimeCode: self.player.currentTime(),
                skippedAt: skippedAt
            }, true);
        };
    };

    InteractiveVideoPlayer.prototype.initQuestions = function () {
        for (var i = this.timeline.length - 1; i >= 0; i--) {
            var item = this.timeline[i];
            this.questions = this.questions.concat(item.questions);
        }
    };

    InteractiveVideoPlayer.prototype.buildNavigation = function () {
        var nav = $("#videoNavigation");
        nav.html("<ul></ul>");
        for (var i = 0; i < this.timeline.length; i++) {
            var item = this.timeline[i];
            var nodeId = "navitem" + String(i);
            var node = $(this.createNavItem(item, nodeId));

            nav.append(node);
            $("#" + nodeId).click(this.handleNavigationClick(item));

            if (item.questions.length > 0) {
                var list = $("<ul></ul>");
                nav.append(list);
                for (var j = 0; j < item.questions.length; j++) {
                    var question = item.questions[j];
                    var questionId = "navitem" + String(i) + String(j);
                    var questionNode = $(this.createNavItem(question, questionId));

                    list.append(questionNode);
                    $("#" + questionId).click(this.handleNavigationClick(question));
                }
            }
        }
    };

    /**
     * @brief               Places a single input field used in questions.
     * @details
     *
     * @param fieldId       Type: String. The ID of the field.
     * @param evalFunction  Type: (string) => boolean. The function used to evaluate answers.
     * @param offsetTop     Type: Integer. Y offset relative to top-left corner of player.
     * @param offsetLeft    Type: Integer. X offset relative to top-left corner of player.
     */
    InteractiveVideoPlayer.prototype.placeInputField = function (fieldId, evalFunction, offsetTop, offsetLeft) {
        var self = this;
        $("#wrapper").append(this.createInputField(fieldId));
        var field = $("#" + fieldId);
        field.css({
            position: "absolute",
            top: (offsetTop * self.scaleHeight) + "px",
            left: (offsetLeft * self.scaleWidth) + "px",
            minWidth: 90 * self.scaleWidth,
            minHeight: 20 * self.scaleHeight
        });
        field.mathquill("editable");
    };

    InteractiveVideoPlayer.prototype.validateAnswer = function (field, value) {
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
    };

    InteractiveVideoPlayer.prototype.getVisibleQuestionID = function () {
        for (var i = 0; i < this.timeline.length; i++) {
            var item = this.timeline[i];
            for (var j = 0; j < item.questions.length; j++) {
                var question = item.questions[j];
                if (question.visible) {
                    return [i, j];
                }
            }
        }
        return null;
    };

    InteractiveVideoPlayer.prototype.getVisibleQuestion = function () {
        var id = this.getVisibleQuestionID();
        if (id === null) return null;
        return this.timeline[id[0]].questions[id[1]];
    };

    InteractiveVideoPlayer.prototype.removeAllQuestions = function () {
        $("#wrapper").find(".question").remove();
        this.hideAllFields();
    };

    InteractiveVideoPlayer.prototype.hideAllFields = function () {
        for (var i = this.questions.length - 1; i >= 0; i--) {
            this.questions[i].visible = false;
        }
    };

    InteractiveVideoPlayer.prototype.createInputField = function (id) {
        return '<span class="question" id="' + id + '"></span>';
    };

    InteractiveVideoPlayer.prototype.createNavItem = function (item, id) {
        return '<li><a href="#" id="' + id + '">' + formatTime(item.timecode) + ' - ' + item.title + '</a></li>';
    };

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

    return InteractiveVideoPlayer;
}());
