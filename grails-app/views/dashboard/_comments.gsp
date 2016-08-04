<%@ page import="dk.sdu.tekvideo.DashboardPeriod; dk.danthrane.twbs.NavStyle; dk.sdu.tekvideo.NodeStatus; dk.danthrane.twbs.ButtonSize; dk.danthrane.twbs.ButtonStyle; dk.sdu.tekvideo.FaIcon" contentType="text/html;charset=UTF-8" %>
<div id="comments" class="card-item">
    <twbs:column md="12">
        <div id="comment-no-data" class="hide">
            Der er ingen data tilgænglig for dette emne.
        </div>
        <div id="comment-container"></div>

        <div id="comment-template" class="hide">
            <sdu:card>
                <div class="notification-comment-component">
                    <div class="short">
                        <twbs:row>
                            <twbs:column md="10">
                                <b>
                                    {0} til <a href="{4}">{1}</a> {2}
                                </b>
                                <i>{3}</i>
                            </twbs:column>
                            <twbs:column md="2">
                                <twbs:button block="true" size="${ButtonSize.XTRA_SMALL}"
                                             style="${ButtonStyle.PRIMARY}" class="expand-button">
                                    <fa:icon icon="${FaIcon.COMMENT}"/> Læs resten eller svar
                                </twbs:button>
                            </twbs:column>
                        </twbs:row>
                    </div>

                    <div class="expanded">
                        <twbs:row>
                            <twbs:column md="10">
                                <b>
                                    {0} har skrevet en kommentar til <a href="#">{1}</a> {2}
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
                            {3}
                        </blockquote>

                        <twbs:row>
                            <twbs:form inline="true">
                                <twbs:column md="10">
                                    <twbs:input
                                            placeholder="Skriv en besked her for at svare på kommentaren"
                                            showLabel="false" name="comment" disabled="true"/>
                                </twbs:column>
                                <twbs:column md="2">
                                    <twbs:button style="${ButtonStyle.SUCCESS}" block="true" disabled="true">
                                        <fa:icon icon="${FaIcon.COMMENT}"/> Svar nu
                                    </twbs:button>
                                </twbs:column>
                            </twbs:form>
                        </twbs:row>
                    </div>
                </div>
            </sdu:card>
        </div>
    </twbs:column>
</div>