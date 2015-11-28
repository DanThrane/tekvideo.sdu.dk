<%@ page import="dk.sdu.tekvideo.DashboardPeriod; dk.danthrane.twbs.NavStyle; dk.sdu.tekvideo.NodeStatus; dk.danthrane.twbs.ButtonSize; dk.danthrane.twbs.ButtonStyle; dk.sdu.tekvideo.FaIcon" contentType="text/html;charset=UTF-8" %>
<div id="views" class="card-item active">
    <twbs:column md="12">
        <twbs:row>
            <twbs:column md="6">
                <twbs:pageHeader><h3>Visninger <small>Over den sidste uge</small></h3></twbs:pageHeader>
                <canvas id="my-chart" style="width: 100%; height: 500px"></canvas>
            </twbs:column>
            <twbs:column md="6">
                <twbs:pageHeader><h3>Populære Videoer</h3></twbs:pageHeader>
                <g:each in="${1..5}" var="i">
                    <sdu:card class="popular-video">
                        <twbs:row>
                            <twbs:column md="1">
                                <h3>${i}</h3>
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
                </g:each>

            </twbs:column>
        </twbs:row>
    </twbs:column>
</div>