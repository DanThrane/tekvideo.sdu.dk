<%@ page import="dk.danthrane.twbs.NavStyle; dk.sdu.tekvideo.NodeStatus; dk.danthrane.twbs.ButtonSize; dk.danthrane.twbs.ButtonStyle; dk.sdu.tekvideo.FaIcon" contentType="text/html;charset=UTF-8" %>
<%--suppress ALL --%>
<%--suppress JSDuplicatedDeclaration --%>
<html>
<head>
    <title>Mine kurser</title>
    <meta name="layout" content="main_fluid"/>
    <script src="//cdnjs.cloudflare.com/ajax/libs/Chart.js/1.0.2/Chart.min.js"></script>
    <sdu:requireAjaxAssets/>
</head>

<body>

<g:content key="sidebar-left">
    <twbs:select
            list="${["Fra i dag", "Over den sidste uge", "Over den sidste måned", "Over de sidste 6 måneder", "Over det sidste år", "Siden start"]}"
            name="dataRange" labelText="Vis data"/>
    <ul class="fa-ul">
        <g:each in="${0..3}" var="i">
            <li>
                <fa:icon listItem="true" icon="${FaIcon.GRADUATION_CAP}"/>
                <a href="#">Fag ${i}</a>
                <a href="#" class="pull-right collapse-tree"><fa:icon icon="${FaIcon.COMPRESS}"/></a>
                <ul class="fa-ul">
                    <g:each in="${0..3}" var="j">
                        <li>
                            <fa:icon listItem="true" icon="${FaIcon.USERS}"/>
                            <a href="#">Emne ${j}</a>
                            <a href="#" class="pull-right collapse-tree"><fa:icon icon="${FaIcon.COMPRESS}"/></a>
                            <ul class="fa-ul">
                                <g:each in="${0..3}" var="k">
                                    <li>
                                        <fa:icon listItem="true" icon="${FaIcon.PLAY}"/>
                                        <a href="#">Video ${k}</a>
                                    </li>
                                </g:each>
                            </ul>
                        </li>
                    </g:each>

                </ul>
            </li>
        </g:each>
    </ul>
</g:content>

<twbs:row>
    <twbs:column md="12">
        <h3>Et Emne 1 <small>Fra Et Fag 1 (Forår 2005)</small></h3>
        <twbs:row>
            <twbs:column md="12">
                <twbs:nav style="${NavStyle.TAB}" justified="true">
                    <twbs:navbarLink uri="#" data-href="views" class="menu-item">
                        <fa:icon icon="${FaIcon.YOUTUBE_PLAY}"/>
                        Afspilninger
                        <twbs:badge>42</twbs:badge>
                    </twbs:navbarLink>
                    <twbs:navbarLink uri="#" data-href="comments" class="menu-item">
                        <fa:icon icon="${FaIcon.COMMENT}"/>
                        Kommentarer
                        <twbs:badge>1</twbs:badge>
                    </twbs:navbarLink>
                    <twbs:navbarLink uri="#" data-href="students" class="menu-item">
                        <fa:icon icon="${FaIcon.GRADUATION_CAP}"/>
                        Aktivitet
                        <twbs:badge>12</twbs:badge>
                    </twbs:navbarLink>
                    <twbs:navbarLink uri="#" data-href="answers" class="menu-item" active="true">
                        <fa:icon icon="${FaIcon.PENCIL_SQUARE}"/>
                        Svar
                        <twbs:badge>18</twbs:badge>
                    </twbs:navbarLink>
                </twbs:nav>
            </twbs:column>
        </twbs:row>
        <twbs:row>
            <div id="stack" class="card-stack">
                <div id="views" class="card-item">
                    <twbs:column md="12">
                        <twbs:row>
                            <twbs:column md="6">
                                <twbs:pageHeader><h3>Visninger <small>Over den sidste uge</small></h3></twbs:pageHeader>
                                <canvas id="my-chart" style="width: 100%; height: 500px"></canvas>
                            </twbs:column>
                            <twbs:column md="6">
                                <twbs:pageHeader><h3>Populære Videoer</h3></twbs:pageHeader>
                                <sdu:card class="popular-video">
                                    <twbs:row>
                                        <twbs:column md="1">
                                            <h3>1</h3>
                                        </twbs:column>
                                        <twbs:column md="11">
                                            <div>
                                                <a href="#">Video 1</a>
                                                fra
                                                <a href="#">Emne 1</a>
                                                i
                                                <a href="#">Kursus 1</a></div>

                                            <div>I alt <i>42</i> visninger med <i>30</i> svar, hvoraf <i>20 (66%)</i> var korrekte
                                            </div>
                                        </twbs:column>
                                    </twbs:row>
                                </sdu:card>
                                <sdu:card class="popular-video">
                                    <twbs:row>
                                        <twbs:column md="1">
                                            <h3>2</h3>
                                        </twbs:column>
                                        <twbs:column md="11">
                                            <div>
                                                <a href="#">Video 1</a>
                                                fra
                                                <a href="#">Emne 1</a>
                                                i
                                                <a href="#">Kursus 1</a></div>

                                            <div>I alt <i>42</i> visninger med <i>30</i> svar, hvoraf <i>20 (66%)</i> var korrekte
                                            </div>
                                        </twbs:column>
                                    </twbs:row>
                                </sdu:card>
                                <sdu:card class="popular-video">
                                    <twbs:row>
                                        <twbs:column md="1">
                                            <h3>3</h3>
                                        </twbs:column>
                                        <twbs:column md="11">
                                            <div>
                                                <a href="#">Video 1</a>
                                                fra
                                                <a href="#">Emne 1</a>
                                                i
                                                <a href="#">Kursus 1</a></div>

                                            <div>I alt <i>42</i> visninger med <i>30</i> svar, hvoraf <i>20 (66%)</i> var korrekte
                                            </div>
                                        </twbs:column>
                                    </twbs:row>
                                </sdu:card>
                                <sdu:card class="popular-video">
                                    <twbs:row>
                                        <twbs:column md="1">
                                            <h3>4</h3>
                                        </twbs:column>
                                        <twbs:column md="11">
                                            <div>
                                                <a href="#">Video 1</a>
                                                fra
                                                <a href="#">Emne 1</a>
                                                i
                                                <a href="#">Kursus 1</a></div>

                                            <div>I alt <i>42</i> visninger med <i>30</i> svar, hvoraf <i>20 (66%)</i> var korrekte
                                            </div>
                                        </twbs:column>
                                    </twbs:row>
                                </sdu:card>
                            </twbs:column>
                        </twbs:row>
                    </twbs:column>
                </div>

                <div id="comments" class="card-item">
                    <twbs:column md="12">
                        <g:each in="${0..3}">
                            <twbs:pageHeader><h4>En gruppering</h4></twbs:pageHeader>
                            <sdu:card>
                                <g:each in="${0..3}">
                                    <div class="notification-comment-component">
                                        <div class="short">
                                            <twbs:row>
                                                <twbs:column md="10">
                                                    <b>
                                                        USERNAME til <a href="#">VIDEO</a> 11/11
                                                    </b>
                                                    <i>Lorem Ipsum is simply dummy text of the printing and typesetting
                                                    industry. Lorem Ipsum has been the industry's standard dummy text ever
                                                    since the...</i>
                                                </twbs:column>
                                                <twbs:column md="2">
                                                    <twbs:button block="true" size="${ButtonSize.XTRA_SMALL}"
                                                                 style="${ButtonStyle.PRIMARY}" class="expand-button">
                                                        <fa:icon
                                                                icon="${FaIcon.COMMENT}"/> Læs resten eller svar</twbs:button>
                                                </twbs:column>
                                            </twbs:row>
                                        </div>

                                        <div class="expanded">
                                            <twbs:row>
                                                <twbs:column md="10">
                                                    <b>
                                                        USERNAME har skrevet en kommentar til <a
                                                            href="#">VIDEO</a> den 11/11/11 kl. 11:11
                                                    </b>
                                                </twbs:column>
                                                <twbs:column md="2">
                                                    <twbs:button block="true" size="${ButtonSize.XTRA_SMALL}"
                                                                 style="${ButtonStyle.PRIMARY}" class="shrink-button">
                                                        Skjul
                                                    </twbs:button>
                                                </twbs:column>
                                            </twbs:row>
                                            <hr>
                                            <blockquote>
                                                Lorem Ipsum is simply dummy text of the printing and typesetting industry.
                                                Lorem Ipsum has been the industry's standard dummy text ever since the
                                                1500s.
                                            </blockquote>

                                            <twbs:row>
                                                <twbs:form inline="true">
                                                    <twbs:column md="10">
                                                        <twbs:input
                                                                placeholder="Skriv en besked her for at svare på kommentaren"
                                                                showLabel="false" name="comment"/>
                                                    </twbs:column>
                                                    <twbs:column md="2">
                                                        <twbs:button style="${ButtonStyle.SUCCESS}" block="true">
                                                            <fa:icon icon="${FaIcon.COMMENT}"/> Svar nu
                                                        </twbs:button>
                                                    </twbs:column>
                                                </twbs:form>
                                            </twbs:row>
                                        </div>
                                    </div>
                                </g:each>
                            </sdu:card>
                        </g:each>
                    </twbs:column>
                </div>

                <div id="students" class="card-item">
                    <twbs:column sm="12">
                        <p>
                            <b>8 studerende</b> har til faget <b>"Et fag 2"</b> har set videoer over de sidste
                            <b>14 dage</b>. Fordelingen ser ud som følger:
                        </p>

                        <twbs:table condensed="true" striped="true" responsive="true">
                            <thead>
                            <th>Navn</th>
                            <th>Antal unikke videoer i faget</th>
                            <th>Antal svar</th>
                            <th>Detaljer</th>
                            </thead>
                            <tbody>
                            <g:each in="${0..10}" var="i">
                                <tr>
                                    <td>Studerende ${i}</td>
                                    <td>${(int) Math.floor(Math.random() * i)}</td>
                                    <td>10 (8 <span class="text-success"><fa:icon
                                            icon="${FaIcon.CHECK}"/></span>/2 <span
                                            class="text-danger"><fa:icon icon="${FaIcon.TIMES}"/></span> [80%])</td>
                                    <td><twbs:button style="${ButtonStyle.INFO}">&gt;</twbs:button></td>
                                </tr>
                            </g:each>
                            </tbody>
                        </twbs:table>
                    </twbs:column>
                </div>

                <div id="answers" class="card-item active">
                    <twbs:column md="12">
                        <twbs:pageHeader><h4>Et emne</h4></twbs:pageHeader>
                        <div class="answer-breakdown">
                            <sdu:card>
                                <h5>VIDEO</h5>

                                <p>Der har været <b>42 svar</b> fra <b>10 studerende</b>, samt <b>10 svar</b> fra gæster
                                </p>

                                <p>Videoen består af <b>9</b> spørgsmål. Vælg et af dem for at se hvordan der er blevet svaret på det givne spørgsmål:
                                </p>

                                <div class="player-container">
                                    <twbs:row>
                                        <twbs:column md="6">
                                            <div class="embed-responsive embed-responsive-16by9"
                                                 style="border: 1px solid black;">
                                                <div class="wrapper" style="z-index: 20"></div>

                                                <div class="player"></div>
                                            </div>
                                        </twbs:column>
                                        <twbs:column md="6">
                                            <ul class="video-navigation">
                                            </ul>
                                        </twbs:column>
                                    </twbs:row>
                                </div>

                                <hr>

                                <twbs:nav justified="true" style="${NavStyle.TAB}" class="sub-menu">
                                    <twbs:navbarLink url="#!/participation" data-href="participation" active="true">
                                        <fa:icon icon="${FaIcon.USERS}"/>
                                        Deltagelse fra studerende
                                    </twbs:navbarLink>
                                    <twbs:navbarLink url="#!/breakdown" data-href="breakdown">
                                        <fa:icon icon="${FaIcon.PIE_CHART}"/>
                                        Analyse af svar
                                    </twbs:navbarLink>
                                    <twbs:navbarLink url="#!/raw" data-href="raw">
                                        <fa:icon icon="${FaIcon.AREA_CHART}"/>
                                        Rå data
                                    </twbs:navbarLink>
                                </twbs:nav>

                                <div class="stats">
                                    <div class="participation item">
                                        <div class="participation-table"></div>
                                    </div>

                                    <div class="breakdown item hide">
                                        <div class="no-answer-selected">
                                            <h5 style="text-align: center;">Vælg et spørgsmål fra listen for at se information om det</h5>
                                        </div>

                                        <div class="answer-selected">
                                            <h5>Fordeling af svar til <span class="question-name"></span></h5>

                                            <canvas class="histogram" style="width: 100%; height: 300px"></canvas>
                                        </div>
                                    </div>

                                    <div class="raw item hide">
                                        <div class="no-answer-selected">
                                            <h5 style="text-align: center;">Vælg et spørgsmål fra listen for at se information om det</h5>
                                        </div>

                                        <div class="answer-selected">
                                            <h5>Svar til <span class="question-name"></span></h5>

                                            <div class="field-statistics"></div>

                                            <script type="text/template" class="field-stat-template hide">
                                            <b>{0}</b> <br/>
                                            <twbs:table>
                                                <thead>
                                                <th>Bruger</th>
                                                <th>Svar</th>
                                                <th>Korrekt</th>
                                                </thead>
                                                <tbody>
                                                {1}
                                                </tbody>
                                            </twbs:table>
                                            </script>

                                            <script type="text/template" class="field-stat-row-template">
                                            <tr>
                                                <td>{0}</td>
                                                <td>{1}</td>
                                                <td>{2}</td>
                                            </tr>
                                            </script>
                                        </div>
                                    </div>
                                </div>
                            </sdu:card>
                        </div>
                    </twbs:column>
                </div>
            </div>
        </twbs:row>
    </twbs:column>
</twbs:row>

<script type="text/javascript">
    var NotificationCommentComponent = (function () {
        function NotificationCommentComponent(element) {
            this.element = element;
        }

        NotificationCommentComponent.prototype.init = function () {
            var $element = $(this.element);
            var $short = $($element.find(".short"));
            var $expanded = $($element.find(".expanded"));
            $expanded.hide();

            $element.find(".expand-button").click(function (e) {
                e.preventDefault();
                $short.slideUp();
                $expanded.slideDown();
            });

            $element.find(".shrink-button").click(function (e) {
                e.preventDefault();
                $expanded.slideUp();
                $short.slideDown();
            });
        };
        return NotificationCommentComponent;
    }());

    var AnswerBreakdownComponent = (function () {
        function AnswerBreakdownComponent(element) {
            this.element = $(element);
            this.player = null;
        }

        AnswerBreakdownComponent.prototype.init = function () {
            var self = this;

            this.element.find(".sub-menu a").click(function (e) {
                e.preventDefault();

                self.element.find(".stats .item").addClass("hide");
                var page = $(this).data("href");
                self.element.find(".stats").find("." + page).removeClass("hide");

                self.element.find(".sub-menu li").removeClass("active");
                $(this).parent().addClass("active");
                self.initAnswerSubPage(page);
            });
        };

        var COLORS = ["#F44336", "#E91E63", "#9C27B0", "#673AB7", "#3F51B5", "#2196F3", "#03A9F4", "#00BCD4",
            "#009688", "#4CAF50", "#8BC34A", "#CDDC39", "#FFEB3B", "#FFC107", "#FF9800", "#FF5722", "#795548",
            "#9E9E9E", "#607D8B"];

        var HIGHLIGHT_COLORS = ["#EF5350", "#EC407A", "#AB47BC", "#7E57C2", "#5C6BC0", "#42A5F5", "#29B6F6",
            "#26C6DA", "#26A69A", "#66BB6A", "#9CCC65", "#D4E157", "#FFEE58", "#FFCA28", "#FFA726", "#FF7043",
            "#8D6E63", "#BDBDBD", "#78909C"];

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

        AnswerBreakdownComponent.prototype.initAnswerSubPage = function (page) {
            switch (page) {
                case "breakdown":
                    var ctx = this.element.find(".histogram").get(0).getContext("2d");
                    var prep = prepareAnswerAnalysis(this.answers);
                    var data = [];
                    for (var i = 0; i < prep.labels.length; i++) {
                        data.push({
                            value: prep.count[i],
                            color: COLORS[i],
                            highlight: HIGHLIGHT_COLORS[i],
                            label: "Svar " + prep.labels[i]
                        });
                    }
                    var chart = new Chart(ctx).Pie(data);
                    break;
            }
        };

        AnswerBreakdownComponent.prototype._onExpansion = function () {
            this.element.find(".answer-selected").hide();
            this.element.find(".no-answer-selected").show();
            this.bootstrapDemoVideo();
        };

        AnswerBreakdownComponent.prototype.bootstrapDemoVideo = function () {
            var self = this;
            if (this.player !== null) this.player.destroy();
            this.player = new InteractiveVideoPlayer(this.element.find(".player-container"));
            this.player.autoplay = false;

            this.answers = [
                <g:each in="${0..10}" var="i">
                <g:each in="${0..2}" var="j">
                {
                    "user": "A user ${i}",
                    "answer": ${new Random().nextInt(20)},
                    "correct": ${new Random().nextBoolean()},
                    "subject": 1,
                    "question": 0,
                    "field": ${j}
                },
                </g:each>
                </g:each>
            ];
            this.students = [
                <g:each in="${0..20}" var="i">
                "A user ${i}",
                </g:each>
            ];
            this.timeline = [
                {
                    "title": "Stedfunktionen",
                    "timecode": 0,
                    "questions": []
                },
                {
                    "title": "Bevægelse med konstant hastighed",
                    "timecode": 68,
                    "questions": [{
                        "title": "Brug af stedfunktionen",
                        "timecode": 200,
                        "fields": [{
                            "name": "field-0",
                            "topoffset": 298,
                            "leftoffset": 184,
                            "answer": {
                                "type": "between",
                                "min": 2.999,
                                "max": 3.001
                            }
                        }, {
                            "name": "field-1",
                            "topoffset": 299,
                            "leftoffset": 283,
                            "answer": {
                                "type": "between",
                                "min": 3.499,
                                "max": 3.501
                            }
                        }, {
                            "name": "field-2",
                            "topoffset": 298,
                            "leftoffset": 376,
                            "answer": {
                                "type": "between",
                                "min": 3.999,
                                "max": 4.001
                            }
                        }],
                        "visible": true,
                        "shown": true
                    }]
                }
            ];
            this.player.startPlayer("5r8TkDyoBu4", true, this.timeline);
            this.player.on("questionShown", function (e) {
                self.displayField(e.subjectId, e.questionId);
            });

            this.element.find(".participation-table").append(this.buildParticipationTable(this.analyzeParticipation()));
        };

        AnswerBreakdownComponent.prototype.displayField = function (subjectId, questionId) {
            var question = this.timeline[subjectId].questions[questionId];
            this.element.find(".no-answer-selected").hide();

            this.element.find(".question-name").text(question.title);

            var statTemplate = this.element.find(".field-stat-template").html();
            var rowTemplate = this.element.find(".field-stat-row-template").html();

            var ourAnswers = this.answers.filter(function (value) {
                return value.subject === subjectId && value.question === questionId;
            });

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
                    rows += rowTemplate.format(answer.user, answer.answer, answer.correct);
                }

                fields += statTemplate.format(field.name, rows);
            }
            this.element.find(".field-statistics").html(fields);

            this.element.find(".answer-selected").show();
        };

        AnswerBreakdownComponent.prototype.analyzeParticipation = function () {
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

        AnswerBreakdownComponent.prototype.buildParticipationTable = function (analysis) {
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

        return AnswerBreakdownComponent;
    }());

    $(function () {
        var stack = new CardStack("#stack");
        $(".menu-item").click(function (e) {
            e.preventDefault();
            var id = $(this).data("href");
            stack.select("#" + id);
            $(".nav li").removeClass("active");
            $(this).parent().addClass("active");
        });

        $(".notification-comment-component").each(function (idx, element) {
            var component = new NotificationCommentComponent(element);
            component.init();
        });

        $(".answer-breakdown").each(function (idx, element) {
            var breakdown = new AnswerBreakdownComponent(element);
            breakdown.init();
            breakdown._onExpansion();
        });

        $(".collapse-tree").click(function (e) {
            e.preventDefault();
            $(this).parent().find("ul").slideToggle();
            $(this).find("i").toggleClass("fa-compress fa-expand");
        });
    });
    var timeline = [];
    var chart = null;

    function createChart(labels, data) {
        if (chart) chart.destroy();
        var ctx = $("#my-chart").get(0).getContext("2d");
        chart = new Chart(ctx).Line({
            labels: labels,
            datasets: [
                {
                    label: "Visninger",
                    fillColor: "rgba(220,220,220,0.2)",
                    strokeColor: "rgba(220,220,220,1)",
                    pointColor: "rgba(220,220,220,1)",
                    pointStrokeColor: "#fff",
                    pointHighlightFill: "#fff",
                    pointHighlightStroke: "rgba(220,220,220,1)",
                    data: data
                }
            ]
        });
    }
    createChart(["09/11 17:33", "09/11 18:33", "09/11 19:33", "09/11 20:33", "09/11 21:33", "09/11 22:33", "09/11 23:33", "10/11 00:33", "10/11 01:33", "10/11 02:33", "10/11 03:33", "10/11 04:33", "10/11 05:33", "10/11 06:33", "10/11 07:33", "10/11 08:33", "10/11 09:33", "10/11 10:33", "10/11 11:33", "10/11 12:33", "10/11 13:33", "10/11 14:33", "10/11 15:33", "10/11 16:33"], [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 2]);
</script>

</body>

</html>