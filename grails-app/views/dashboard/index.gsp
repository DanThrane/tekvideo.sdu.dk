<%@ page import="dk.sdu.tekvideo.DashboardPeriod; dk.danthrane.twbs.NavStyle; dk.sdu.tekvideo.NodeStatus; dk.danthrane.twbs.ButtonSize; dk.danthrane.twbs.ButtonStyle; dk.sdu.tekvideo.FaIcon" contentType="text/html;charset=UTF-8" %>
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
    <twbs:select list="${DashboardPeriod.values()}" name="dataRange" labelText="Vis data"/>
    <ul class="fa-ul">
        <g:each in="${courses.findAll { it != null }}" var="course">
            <li>
                <fa:icon listItem="true" icon="${FaIcon.GRADUATION_CAP}"/>
                <a href="#">${course.fullName} [${course.name}]</a>
                <a href="#" class="pull-right collapse-tree"><fa:icon icon="${FaIcon.COMPRESS}"/></a>
                <ul class="fa-ul">
                    <g:each in="${course.subjects.findAll { it != null }}" var="subject">
                        <li>
                            <fa:icon listItem="true" icon="${FaIcon.USERS}"/>
                            <a href="#">${subject.name}</a>
                            <a href="#" class="pull-right collapse-tree"><fa:icon icon="${FaIcon.COMPRESS}"/></a>
                            <ul class="fa-ul">
                                <g:each in="${subject.videos.findAll { it != null }}" var="video">
                                    <li>
                                        <fa:icon listItem="true" icon="${FaIcon.PLAY}"/>
                                        <a href="#">${video.name}</a>
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
                    <twbs:navbarLink uri="#" data-href="views" class="menu-item" active="true">
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
                    <twbs:navbarLink uri="#" data-href="answers" class="menu-item">
                        <fa:icon icon="${FaIcon.PENCIL_SQUARE}"/>
                        Svar
                        <twbs:badge>18</twbs:badge>
                    </twbs:navbarLink>
                </twbs:nav>
            </twbs:column>
        </twbs:row>
        <twbs:row>
            <div id="stack" class="card-stack">
                <g:render template="views" />
                <g:render template="comments" />
                <g:render template="students" />
                <g:render template="answers" />
            </div>
        </twbs:row>
    </twbs:column>
</twbs:row>

<asset:javascript src="dashboard/app.js" />

</body>

</html>