<%@ page import="grails.converters.JSON; java.time.LocalDateTime; dk.danthrane.twbs.ButtonStyle; dk.sdu.tekvideo.FaIcon" contentType="text/html;charset=UTF-8" %>
<g:applyLayout name="stats_layout">
    <html>
    <head>
        <title>
            <g:if test="${node != null}">${node.name}</g:if>
            <g:else>Hjem</g:else>
        </title>

        <g:render template="/polymer/includePolymer"/>
        <link rel="import" href="${createLink(absolute: true, uri: '/assets/')}/components/tv-browser.html">
    </head>

    <body>

    <twbs:row>
        <twbs:column class="layout horizontal wrap stat-card-container">
            <sdu:card class="stat-card">
                <h3>Studerende</h3>
                Lorem ipsum dolor sit amet, consectetur adipisicing elit. Assumenda cupiditate dolorum explicabo laudantium minus quam quasi quidem. Consequatur dicta dolorem ipsam porro quo recusandae, ut. Eaque itaque omnis rem repellat.
            </sdu:card>
        </twbs:column>
    </twbs:row>

    <twbs:row>
        <twbs:column>
            <tv:browser items="${items}"/>
        </twbs:column>
    </twbs:row>

    </body>
    </html>
</g:applyLayout>
