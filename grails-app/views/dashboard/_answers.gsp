<%@ page import="dk.sdu.tekvideo.DashboardPeriod; dk.danthrane.twbs.NavStyle; dk.sdu.tekvideo.NodeStatus; dk.danthrane.twbs.ButtonSize; dk.danthrane.twbs.ButtonStyle; dk.sdu.tekvideo.FaIcon" contentType="text/html;charset=UTF-8" %>
<div id="answers" class="card-item active">
    <twbs:column md="12">
        <twbs:pageHeader><h4>Et emne</h4></twbs:pageHeader>
        <div id="answer-thumbnail-container"></div>
    </twbs:column>

    <script type="text/template" id="row-template"><twbs:row class="answer-template-row"/></script>

    <script type="text/template" id="answer-thumbnail-template">
        <twbs:column md="2">
            <sdu:linkCard class="answer-thumbnail" uri="#">
                <img src="http://img.youtube.com/vi/{0}/hqdefault.jpg" class="img-responsive"
                     alt="Video thumbnail">
                <hr>
                <a href="#">{1}</a>
            </sdu:linkCard>
        </twbs:column>
    </script>

    <div id="answer-breakdown-container"></div>

    <script type="text/template" id="answer-breakdown-template">
        <twbs:column>
            <div class="answer-breakdown">
                <sdu:card>
                    <h5>{0}</h5>

                    <p>Vælg et spørgsmål af dem for at se hvordan der er blevet svaret på det givne spørgsmål:</p>

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
                            </div>
                        </div>
                    </div>
                </sdu:card>
            </div>
        </twbs:column>
    </script>

    <script type="text/template" id="field-stat-template">
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

    <script type="text/template" id="field-stat-row-template">
    <tr>
        <td>{0}</td>
        <td>{1}</td>
        <td>{2}</td>
    </tr>
    </script>
</div>