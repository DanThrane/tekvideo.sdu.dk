<%@ page import="dk.sdu.tekvideo.NodeStatus; dk.danthrane.twbs.ButtonSize; dk.danthrane.twbs.ButtonStyle; dk.sdu.tekvideo.FaIcon" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Mine kurser</title>
    <meta name="layout" content="main_fluid"/>
    <script src="//cdnjs.cloudflare.com/ajax/libs/Chart.js/1.0.2/Chart.min.js"></script>
    <sdu:requireAjaxAssets/>
</head>

<body>

<g:content key="sidebar-left">
    More content in left sidebar
</g:content>

<twbs:row>
    <twbs:column md="4">
        <sdu:card class="stat-card yellow">
            <twbs:column sm="10">
                <span class="name">Test Kort</span>
                <span class="stat">123 stat</span>
            </twbs:column>
            <twbs:column sm="2">
                <fa:icon icon="${FaIcon.FIRE}" size="4"/>
            </twbs:column>
        </sdu:card>
    </twbs:column>
    <twbs:column md="4">
        <sdu:card class="stat-card red">
            <twbs:column sm="10">
                <span class="name">Test Kort</span>
                <span class="stat">123 stat</span>
            </twbs:column>
            <twbs:column sm="2">
                <fa:icon icon="${FaIcon.FIRE_EXTINGUISHER}" size="4"/>
            </twbs:column>
        </sdu:card>
    </twbs:column>
    <twbs:column md="4">
        <sdu:card class="stat-card green">
            <twbs:column sm="10">
                <span class="name">Test Kort</span>
                <span class="stat">123 stat</span>
            </twbs:column>
            <twbs:column sm="2">
                <fa:icon icon="${FaIcon.SHOPPING_CART}" size="4"/>
            </twbs:column>
        </sdu:card>
    </twbs:column>
</twbs:row>

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

                    <div>I alt <i>42</i> visninger med <i>30</i> svar, hvoraf <i>20 (66%)</i> var korrekte</div>
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

                    <div>I alt <i>42</i> visninger med <i>30</i> svar, hvoraf <i>20 (66%)</i> var korrekte</div>
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

                    <div>I alt <i>42</i> visninger med <i>30</i> svar, hvoraf <i>20 (66%)</i> var korrekte</div>
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

                    <div>I alt <i>42</i> visninger med <i>30</i> svar, hvoraf <i>20 (66%)</i> var korrekte</div>
                </twbs:column>
            </twbs:row>
        </sdu:card>
    </twbs:column>
</twbs:row>

<twbs:row>
    <twbs:column md="6">
        <twbs:pageHeader><h3>Dine fag</h3></twbs:pageHeader>
        <sdu:card>
            <twbs:row>
                <twbs:column md="6">
                    <h4 style="margin: 4px;">Et fag</h4>
                </twbs:column>
                <twbs:column md="6">
                    <twbs:buttonGroup class="pull-right">
                        <twbs:button>
                            <fa:icon icon="${FaIcon.YOUTUBE_PLAY}"/>
                            42
                        </twbs:button>
                        <twbs:button>
                            <fa:icon icon="${FaIcon.COMMENT}"/>
                            1
                        </twbs:button>
                        <twbs:button>
                            <fa:icon icon="${FaIcon.GRADUATION_CAP}"/>
                            8
                        </twbs:button>
                        <twbs:button>
                            <fa:icon icon="${FaIcon.PENCIL_SQUARE}"/>
                            10
                        </twbs:button>
                    </twbs:buttonGroup>
                </twbs:column>
            </twbs:row>
        </sdu:card>
        <sdu:card>
            <twbs:row>
                <twbs:column md="6">
                    <h4 style="margin: 4px;">Et fag 2</h4>
                </twbs:column>
                <twbs:column md="6">
                    <twbs:buttonGroup class="pull-right">
                        <twbs:button>
                            <fa:icon icon="${FaIcon.YOUTUBE_PLAY}"/>
                            42
                        </twbs:button>
                        <twbs:button>
                            <fa:icon icon="${FaIcon.COMMENT}"/>
                            1
                        </twbs:button>
                        <twbs:button active="true">
                            <fa:icon icon="${FaIcon.GRADUATION_CAP}"/>
                            8
                        </twbs:button>
                        <twbs:button>
                            <fa:icon icon="${FaIcon.PENCIL_SQUARE}"/>
                            10
                        </twbs:button>
                    </twbs:buttonGroup>
                </twbs:column>
            </twbs:row>
            <hr>
            <twbs:row>
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
                        </thead>
                        <tbody>
                        <tr>
                            <td>Studerende 1</td>
                            <td>3</td>
                            <td>10 (8 <span class="text-success"><fa:icon icon="${FaIcon.CHECK}"/></span>/2 <span
                                    class="text-danger"><fa:icon icon="${FaIcon.TIMES}"/></span> [80%])</td>
                        </tr>
                        <tr>
                            <td>Studerende 2</td>
                            <td>3</td>
                            <td>10 (8 <span class="text-success"><fa:icon icon="${FaIcon.CHECK}"/></span>/2 <span
                                    class="text-danger"><fa:icon icon="${FaIcon.TIMES}"/></span> [80%])</td>
                        </tr>
                        <tr>
                            <td>Studerende 3</td>
                            <td>3</td>
                            <td>10 (8 <span class="text-success"><fa:icon icon="${FaIcon.CHECK}"/></span>/2 <span
                                    class="text-danger"><fa:icon icon="${FaIcon.TIMES}"/></span> [80%])</td>
                        </tr>
                        <tr>
                            <td>Studerende 4</td>
                            <td>3</td>
                            <td>10 (8 <span class="text-success"><fa:icon icon="${FaIcon.CHECK}"/></span>/2 <span
                                    class="text-danger"><fa:icon icon="${FaIcon.TIMES}"/></span> [80%])</td>
                        </tr>
                        <tr>
                            <td>Studerende 5</td>
                            <td>3</td>
                            <td>10 (8 <span class="text-success"><fa:icon icon="${FaIcon.CHECK}"/></span>/2 <span
                                    class="text-danger"><fa:icon icon="${FaIcon.TIMES}"/></span> [80%])</td>
                        </tr>
                        <tr>
                            <td>Studerende 6</td>
                            <td>3</td>
                            <td>10 (8 <span class="text-success"><fa:icon icon="${FaIcon.CHECK}"/></span>/2 <span
                                    class="text-danger"><fa:icon icon="${FaIcon.TIMES}"/></span> [80%])</td>
                        </tr>
                        <tr>
                            <td>Studerende 7</td>
                            <td>3</td>
                            <td>10 (8 <span class="text-success"><fa:icon icon="${FaIcon.CHECK}"/></span>/2 <span
                                    class="text-danger"><fa:icon icon="${FaIcon.TIMES}"/></span> [80%])</td>
                        </tr>
                        <tr>
                            <td>Studerende 8</td>
                            <td>3</td>
                            <td>10 (8 <span class="text-success"><fa:icon icon="${FaIcon.CHECK}"/></span>/2 <span
                                    class="text-danger"><fa:icon icon="${FaIcon.TIMES}"/></span> [80%])</td>
                        </tr>
                        </tbody>
                    </twbs:table>
                </twbs:column>
            </twbs:row>
        </sdu:card>
        <sdu:card>
            <twbs:row>
                <twbs:column md="6">
                    <h4 style="margin: 4px;">Et fag 3</h4>
                </twbs:column>
                <twbs:column md="6">
                    <twbs:buttonGroup class="pull-right">
                        <twbs:button>
                            <fa:icon icon="${FaIcon.YOUTUBE_PLAY}"/>
                            42
                        </twbs:button>
                        <twbs:button>
                            <fa:icon icon="${FaIcon.COMMENT}"/>
                            1
                        </twbs:button>
                        <twbs:button>
                            <fa:icon icon="${FaIcon.GRADUATION_CAP}"/>
                            8
                        </twbs:button>
                        <twbs:button>
                            <fa:icon icon="${FaIcon.PENCIL_SQUARE}"/>
                            10
                        </twbs:button>
                    </twbs:buttonGroup>
                </twbs:column>
            </twbs:row>
        </sdu:card>
    </twbs:column>
    <twbs:column md="6">
        <twbs:pageHeader><h3>Nylig aktivitet <small>Blandt studerende tilmeldt dine fag</small></h3></twbs:pageHeader>
        <div class="timeline-wrapper">
            <div class="timeline-inner">
                <ul class="timeline">
                    <li>
                        <div class="timeline-badge">
                            <fa:icon icon="${FaIcon.VIDEO_CAMERA}"/>
                        </div>

                        <div class="timeline-panel">
                            <div class="timeline-heading">
                                <h4 class="timeline-title">Dan Thrane har set en video</h4>

                                <p>
                                    <small class="text-muted">
                                        <fa:icon icon="${FaIcon.CLOCK_O}"/> 11 timer siden &mdash;
                                        <a href="#"><fa:icon icon="${FaIcon.YOUTUBE_PLAY}"/> En video</a>
                                    </small>
                                </p>
                            </div>

                            %{--<div class="timeline-body">--}%
                            %{--</div>--}%
                        </div>
                    </li>
                    <li class="timeline-inverted">
                        <div class="timeline-badge danger">
                            <fa:icon icon="${FaIcon.TIMES}"/>
                        </div>

                        <div class="timeline-panel">
                            <div class="timeline-heading">
                                <h4 class="timeline-title">Dan Thrane svarede på et spørgsmål</h4>

                                <p>
                                    <small class="text-muted">
                                        <fa:icon icon="${FaIcon.CLOCK_O}"/> 11 timer siden &mdash;
                                        <a href="#"><fa:icon icon="${FaIcon.YOUTUBE_PLAY}"/> En video</a>
                                    </small>
                                </p>
                            </div>

                            <div class="timeline-body">
                                <p>Dan svarede 42 på et spørgsmål, hvilket var forkert.</p>
                            </div>
                        </div>
                    </li>
                    <li>
                        <div class="timeline-badge success">
                            <fa:icon icon="${FaIcon.COMMENT}"/>
                        </div>

                        <div class="timeline-panel">
                            <div class="timeline-heading">
                                <h4 class="timeline-title">Dan Thrane svarede på et spørgsmål</h4>

                                <p>
                                    <small class="text-muted">
                                        <fa:icon icon="${FaIcon.CLOCK_O}"/> 11 timer siden &mdash;
                                        <a href="#"><fa:icon icon="${FaIcon.YOUTUBE_PLAY}"/> En video</a>
                                    </small>
                                </p>
                            </div>

                            <div class="timeline-body">
                                <blockquote>
                                    Dette er kommentar jeg skrev
                                    <footer>Dan Thrane (dathr12)</footer>
                                </blockquote>
                            </div>
                        </div>
                    </li>
                    <li class="timeline-inverted">
                        <div class="timeline-badge">
                            <fa:icon icon="${FaIcon.VIDEO_CAMERA}"/>
                        </div>

                        <div class="timeline-panel">
                            <div class="timeline-heading">
                                <h4 class="timeline-title">Dan Thrane har set en video</h4>

                                <p>
                                    <small class="text-muted">
                                        <fa:icon icon="${FaIcon.CLOCK_O}"/> 11 timer siden &mdash;
                                        <a href="#"><fa:icon icon="${FaIcon.YOUTUBE_PLAY}"/> En video</a>
                                    </small>
                                </p>
                            </div>

                            %{--<div class="timeline-body">--}%
                            %{--</div>--}%
                        </div>
                    </li>
                    <li>
                        <div class="timeline-badge danger">
                            <fa:icon icon="${FaIcon.TIMES}"/>
                        </div>

                        <div class="timeline-panel">
                            <div class="timeline-heading">
                                <h4 class="timeline-title">Dan Thrane svarede på et spørgsmål</h4>

                                <p>
                                    <small class="text-muted">
                                        <fa:icon icon="${FaIcon.CLOCK_O}"/> 11 timer siden &mdash;
                                        <a href="#"><fa:icon icon="${FaIcon.YOUTUBE_PLAY}"/> En video</a>
                                    </small>
                                </p>
                            </div>

                            <div class="timeline-body">
                                <p>Dan svarede 42 på et spørgsmål, hvilket var forkert.</p>
                            </div>
                        </div>
                    </li>
                    <li class="timeline-inverted">
                        <div class="timeline-badge success">
                            <fa:icon icon="${FaIcon.COMMENT}"/>
                        </div>

                        <div class="timeline-panel">
                            <div class="timeline-heading">
                                <h4 class="timeline-title">Dan Thrane svarede på et spørgsmål</h4>

                                <p>
                                    <small class="text-muted">
                                        <fa:icon icon="${FaIcon.CLOCK_O}"/> 11 timer siden &mdash;
                                        <a href="#"><fa:icon icon="${FaIcon.YOUTUBE_PLAY}"/> En video</a>
                                    </small>
                                </p>
                            </div>

                            <div class="timeline-body">
                                <blockquote>
                                    Dette er kommentar jeg skrev
                                    <footer>Dan Thrane (dathr12)</footer>
                                </blockquote>
                            </div>
                        </div>
                    </li>
                </ul>
            </div>
        </div>
    </twbs:column>
</twbs:row>

<script type="text/javascript">
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