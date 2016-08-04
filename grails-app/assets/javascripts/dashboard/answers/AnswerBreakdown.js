var AnswerBreakdown = (function () {
    var COLORS = ["#F44336", "#E91E63", "#9C27B0", "#673AB7", "#3F51B5", "#2196F3", "#03A9F4", "#00BCD4",
        "#009688", "#4CAF50", "#8BC34A", "#CDDC39", "#FFEB3B", "#FFC107", "#FF9800", "#FF5722", "#795548",
        "#9E9E9E", "#607D8B"];

    function AnswerBreakdown(answerPage, app, video, students, period) {
        this.answerPage = answerPage;
        this.video = video;
        try {
            this.timeline = JSON.parse(video.timelineJson);
        } catch (e) {
            this.timeline = [];
        }
        this.students = students.map(function (e) {
            return e.username;
        });
        this.period = period;
        this.app = app;
        this.player = null;
        this.chart = null;
    }

    AnswerBreakdown.prototype.init = function () {
        var self = this;

        var $container = $("#answer-breakdown-container");
        var rawTemplate = $("#answer-breakdown-template").html();
        var component = $(rawTemplate.format(self.video.name));
        self.element = component;
        $container.html(component);

        this.element.find(".answer-selected").hide();
        this.element.find(".no-answer-selected").show();

        this.element.find(".sub-menu a").click(function (e) {
            e.preventDefault();

            self.element.find(".stats .item").addClass("hide");
            var page = $(this).data("href");
            self.element.find(".stats").find("." + page).removeClass("hide");

            self.element.find(".sub-menu li").removeClass("active");
            $(this).parent().addClass("active");
            self.initAnswerSubPage(page);
        });

        this.element.find(".close-answer-breakdown").click(function (e) {
            e.preventDefault();
            self.answerPage.show();
            $container.empty(); // Remove ourselves from the DOM
        });

        this.findAnswers();
    };

    AnswerBreakdown.prototype.findAnswers = function () {
        var self = this;
        self.app.displaySpinner();
        $.getJSON(baseUrl + "dashboard/answers/" + self.video.id + "?period=" + self.period, function (data) {
            console.log(data);
            self.answers = data.result;
            self.initializePlayer();
        }).always(function () {
            self.app.removeSpinner();
        });
    };

    function prepareAnswerAnalysis(answers) {
        var prep = {};
        for (var i = 0; i < answers.length; i++) {
            if (answers[i].answer in prep) {
                prep[answers[i].answer]++;
            } else {
                prep[answers[i].answer] = 1;
            }
        }

        var labels = [];
        var count = [];
        for (var key in prep) {
            if (!prep.hasOwnProperty(key)) continue;
            labels.push(key);
            count.push(prep[key]);
        }

        return {labels: labels, count: count};
    }

    AnswerBreakdown.prototype.initAnswerSubPage = function (page) {
        switch (page) {
            case "breakdown":
                break;
        }
    };

    AnswerBreakdown.prototype.initializePlayer = function () {
        var self = this;
        if (this.player !== null) this.player.destroy();
        this.player = new InteractiveVideoPlayer(this.element.find(".player-container"));
        this.player.autoplay = false;

        this.player.startPlayer(this.video.youtubeId, this.video.videoType, this.timeline);
        this.player.on("questionShown", function (e) {
            self.onQuestionSelected(e.subjectId, e.questionId);
        });

        this.element.find(".participation-table").append(this.buildParticipationTable(this.analyzeParticipation()));
    };

    AnswerBreakdown.prototype.onQuestionSelected = function (subjectId, questionId) {
        var question = this.timeline[subjectId].questions[questionId];
        this.element.find(".no-answer-selected").hide();

        this.element.find(".question-name").text(question.title);

        var statTemplate = $("#field-stat-template").html();
        var rowTemplate = $("#field-stat-row-template").html();

        var ourAnswers = this.answers.filter(function (value) {
            return value.subject === subjectId && value.question === questionId;
        });

        var ctx = this.element.find(".histogram").get(0).getContext("2d");
        var prep = prepareAnswerAnalysis(ourAnswers);
        var data = [];
        var backgroundColor = [];
        var labels = [];
        var config = {
            type: "pie",
            data: {
                datasets: [{
                    data: data,
                    backgroundColor: backgroundColor
                }],
                labels: labels
            },
            options: {
                responsive: true
            }
        };

        for (var i = 0; i < prep.labels.length; i++) {
            data.push(prep.count[i]);
            labels.push("Svar '" + prep.labels[i] + "'");
            backgroundColor.push(COLORS[i]);
        }

        if (this.chart !== null) {
            this.chart.destroy();
        }
        this.chart = new Chart(ctx, config);

        var fields = "";
        for (var i = 0; i < question.fields.length; i++) {
            var field = question.fields[i];
            $("#" + field.name).text(field.name);
            var localAnswers = ourAnswers.filter(function (value) {
                return value.field === i;
            });

            var rows = "";
            for (var j = 0; j < localAnswers.length; j++) {
                var answer = localAnswers[j];
                rows += rowTemplate.format(Util.raw(this.formatUser(answer.user)), answer.answer,
                    Util.raw(this.formatCorrect(answer.correct)));
            }

            fields += statTemplate.format(field.name, Util.raw(rows));
        }
        this.element.find(".field-statistics").html(fields);

        this.element.find(".answer-selected").show();
    };

    AnswerBreakdown.prototype.formatUser = function (user) {
        if (user !== null) {
            return Util.escapeHtml(user);
        } else {
            if (this.guestTemplate === undefined) {
                this.guestTemplate = $("#guest-template").html();
            }
            return this.guestTemplate;
        }
    };

    AnswerBreakdown.prototype.formatCorrect = function (correct) {
        if (this.correctTemplate === undefined || this.wrongTemplate === undefined) {
            this.correctTemplate = $("#correct-template").html();
            this.wrongTemplate = $("#wrong-template").html();
        }
        return (correct) ? this.correctTemplate : this.wrongTemplate;
    };

    AnswerBreakdown.prototype.analyzeParticipation = function () {
        var students = this.students;
        var answers = this.answers;
        var timeline = this.timeline;

        var result = {};
        for (var i = 0; i < students.length; i++) {
            result[students[i]] = {"user": students[i], answered: [], correctlyAnswered: []};
        }

        // Funny looking initialization code:
        for (var subjectIdx = 0; subjectIdx < timeline.length; subjectIdx++) {
            var subject = timeline[subjectIdx];
            for (var studentIdx = 0; studentIdx < students.length; studentIdx++) {
                var entry = result[students[studentIdx]];
                entry.answered.push([]);
                entry.correctlyAnswered.push([]);
            }
            for (var questionIdx = 0; questionIdx < subject.questions.length; questionIdx++) {
                for (var studentIdx = 0; studentIdx < students.length; studentIdx++) {
                    var entry = result[students[studentIdx]];
                    entry.answered[entry.answered.length - 1].push(0);
                    entry.correctlyAnswered[entry.correctlyAnswered.length - 1].push(0);
                }
            }
        }

        // Actually finding the stats:
        for (var i = 0; i < answers.length; i++) {
            var answer = answers[i];
            if (answer.user in result) {
                var entry = result[answer.user];
                entry.answered[answer.subject][answer.question]++;
                if (answer.correct) entry.correctlyAnswered[answer.subject][answer.question]++;
            }
        }
        return result;
    };

    AnswerBreakdown.prototype.buildParticipationTable = function (analysis) {
        var table = $("<table class='table'></table>");
        var header = $("<thead></thead>");
        var body = $("<tbody></tbody>");

        header.append($("<th>Bruger</th>"));
        for (var subjectIdx = 0; subjectIdx < this.timeline.length; subjectIdx++) {
            var subject = this.timeline[subjectIdx];
            for (var questionIdx = 0; questionIdx < subject.questions.length; questionIdx++) {
                var question = subject.questions[questionIdx];
                var th = $("<th></th>");
                th.text(question.title);
                header.append(th);
            }
        }

        for (var key in analysis) {
            if (!analysis.hasOwnProperty(key)) continue;

            var row = $("<tr></tr>");
            var report = analysis[key];
            var answered = report.answered;
            row.append($("<td> " + key + " </td>"));
            for (var i = 0; i < answered.length; i++) {
                for (var j = 0; j < answered[i].length; j++) {
                    row.append($("<td>" + report.correctlyAnswered[i][j] + "/" + report.answered[i][j] + "</td>"));
                }
            }
            body.append(row);
        }

        table.append(header);
        table.append(body);
        return table;
    };

    return AnswerBreakdown;
}());