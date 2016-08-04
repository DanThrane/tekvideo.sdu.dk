<%@ page import="dk.sdu.tekvideo.DashboardPeriod; dk.danthrane.twbs.NavStyle; dk.sdu.tekvideo.NodeStatus; dk.danthrane.twbs.ButtonSize; dk.danthrane.twbs.ButtonStyle; dk.sdu.tekvideo.FaIcon" contentType="text/html;charset=UTF-8" %>
<div id="views" class="card-item dashboard-content-no-margin">
    <twbs:column md="12">
        <twbs:row>
            <twbs:column md="6">
                <twbs:pageHeader><h3>Visninger</h3></twbs:pageHeader>
                <canvas id="my-chart" style="width: 100%; height: 500px"></canvas>
                <twbs:checkbox labelText="Cumulative stats" name="cumulative" />
                <div id="views-no-data" class="hide">
                    Der er ingen kommentarer til dette emne.
                </div>
            </twbs:column>
            <twbs:column md="6">
                <twbs:pageHeader><h3>Populære Videoer</h3></twbs:pageHeader>

                <div id="popular-video-container"></div>

                <div id="popular-video-template" class="hide">
                    <sdu:card class="popular-video">
                        <twbs:row>
                            <twbs:column md="1">
                                <h3>{0}</h3>
                            </twbs:column>
                            <twbs:column md="11">
                                <div>
                                    <b>{1}</b>
                                    fra
                                    <b>{2}</b>
                                    i
                                    <b>{3}</b>
                                </div>

                                <div>
                                    I alt <b>{4}</b> visninger med <b>{5}</b> svar, hvoraf <b>{6}</b> var korrekte
                                </div>
                            </twbs:column>
                        </twbs:row>
                    </sdu:card>
                </div>
            </twbs:column>
        </twbs:row>
    </twbs:column>
</div>