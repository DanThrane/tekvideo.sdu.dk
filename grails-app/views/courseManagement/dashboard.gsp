<%@ page import="dk.danthrane.twbs.NavStyle; dk.sdu.tekvideo.NodeStatus; dk.danthrane.twbs.ButtonSize; dk.danthrane.twbs.ButtonStyle; dk.sdu.tekvideo.FaIcon" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Mine kurser</title>
    <meta name="layout" content="main_fluid"/>
    <script src="//cdnjs.cloudflare.com/ajax/libs/Chart.js/1.0.2/Chart.min.js"></script>
    <sdu:requireAjaxAssets/>
</head>

<body>

<g:content key="sidebar-left">
    <b>Tree view of courses/subjects/videos</b><br/>
    <twbs:select
            list="${["Over den sidste uge", "Over den sidste måned", "Over de sidste 6 måneder", "Over det sidste år", "Siden start"]}"
            name="dataRange" labelText="Vis data"/>
    <ul>
        <g:each in="${0..3}">
            <li>
                <a href="#">Fag 1</a>
                <ul>
                    <li>
                        <a href="#">Emne 1</a>
                        <ul>
                            <li><a href="#">Video 1</a></li>
                            <li><a href="#">Video 1</a></li>
                            <li><a href="#">Video 1</a></li>
                        </ul>
                    </li>
                    <li>
                        <a href="#">Emne 1</a>
                        <ul>
                            <li><a href="#">Video 1</a></li>
                            <li><a href="#">Video 1</a></li>
                            <li><a href="#">Video 1</a></li>
                        </ul>
                    </li>
                    <li>
                        <a href="#">Emne 1</a>
                        <ul>
                            <li><a href="#">Video 1</a></li>
                            <li><a href="#">Video 1</a></li>
                            <li><a href="#">Video 1</a></li>
                        </ul>
                    </li>
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
                                            <twbs:button class="check-button">
                                                Tjek svar
                                            </twbs:button>
                                        </twbs:column>
                                    </twbs:row>
                                </div>

                                <hr>

                                <div class="no-answer-selected">
                                    <h5 style="text-align: center;">Vælg et spørgsmål fra listen for at se information om det</h5>
                                </div>

                                <div class="answer-selected hide">
                                    <h5>Histogram over svar</h5>
                                    <canvas class="histogram" style="width: 100%; height: 300px"></canvas>
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
        }

        AnswerBreakdownComponent.prototype.init = function () {
            var ctx = this.element.find(".histogram").get(0).getContext("2d");
            var data = {
                labels: ["Svar: 22", "Svar: 42", "Svar: 50"],
                datasets: [
                    {
                        label: "22",
                        fillColor: "rgba(220,220,220,0.5)",
                        strokeColor: "rgba(220,220,220,0.8)",
                        highlightFill: "rgba(220,220,220,0.75)",
                        highlightStroke: "rgba(220,220,220,1)",
                        data: [65, 10, 30]
                    }
                ]
            };
            var histogram = new Chart(ctx).Bar(data, {
                multiTooltipTemplate: "<\%= datasetLabel %> - <\%= value %>"
            });
        };

        AnswerBreakdownComponent.prototype._onExpansion = function () {
            this.bootstrapDemoVideo();
        };

        AnswerBreakdownComponent.prototype.bootstrapDemoVideo = function () {
            var player = new InteractiveVideoPlayer(this.element.find(".player-container"));
            player.startPlayer("5r8TkDyoBu4", true,
                    [
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
                    ]
            );
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