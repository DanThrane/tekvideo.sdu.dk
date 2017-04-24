<%@ page import="grails.converters.JSON; java.time.LocalDateTime; dk.danthrane.twbs.ButtonStyle; dk.sdu.tekvideo.FaIcon" contentType="text/html;charset=UTF-8" %>
<g:applyLayout name="stats_layout">
    <html>
    <head>
        <title>
            <g:if test="${node != null}">${node.name}</g:if>
            <g:else>Hjem</g:else>
        </title>

        <g:render template="/polymer/includePolymer"/>
        <link rel="import" href="${createLink(absolute: false, uri: '/static/')}/components/tv-browser.html">
    </head>

    <body>

    <g:if test="${node != null && stats?.size() > 0}">
        <twbs:row>
            <twbs:column>
                <twbs:pageHeader><h4>Statistikker</h4></twbs:pageHeader>
                <i>Vælg en statistik for mere information</i>
            </twbs:column>
        </twbs:row>

        <twbs:row>
            <twbs:column class="layout horizontal wrap stat-card-container">
                <g:each in="${stats}" var="stat">
                    <twbs:linkButton url="${stat.url}" style="${ButtonStyle.LINK}">
                        <fa:icon icon="${stat.icon}" /> ${stat.name}
                    </twbs:linkButton>
                </g:each>
            </twbs:column>
        </twbs:row>
    </g:if>

    <twbs:row>
        <twbs:column>
            <tv:browser items="${items}"/>
        </twbs:column>
    </twbs:row>

    </body>
    </html>
</g:applyLayout>
