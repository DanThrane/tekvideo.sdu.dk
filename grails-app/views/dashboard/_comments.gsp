<%@ page import="dk.sdu.tekvideo.DashboardPeriod; dk.danthrane.twbs.NavStyle; dk.sdu.tekvideo.NodeStatus; dk.danthrane.twbs.ButtonSize; dk.danthrane.twbs.ButtonStyle; dk.sdu.tekvideo.FaIcon" contentType="text/html;charset=UTF-8" %>
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