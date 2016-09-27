<%@ page import="dk.sdu.tekvideo.DashboardPeriod; dk.danthrane.twbs.NavStyle; dk.sdu.tekvideo.NodeStatus; dk.danthrane.twbs.ButtonSize; dk.danthrane.twbs.ButtonStyle; dk.sdu.tekvideo.FaIcon" contentType="text/html;charset=UTF-8" %>
<%--suppress ALL --%>
<%--suppress JSDuplicatedDeclaration --%>
<html>
<head>
    <title>Mine kurser</title>
    <meta name="layout" content="main_fluid"/>

    <script>
        // setup Polymer options
        window.Polymer = {lazyRegister: true, dom: 'shadow'};

        // load webcomponents polyfills
        (function() {
            if ('registerElement' in document
                    && 'import' in document.createElement('link')
                    && 'content' in document.createElement('template')) {
                // browser has web components
            } else {
                // polyfill web components
                var e = document.createElement('script');
                e.src = "${createLink(absolute:true, uri:'/assets/bower_components/webcomponentsjs/webcomponents-lite.min.js')}";
                document.head.appendChild(e);
            }
        })();

    </script>

    <link rel="import" href="${createLink(absolute:true, uri:'/assets/dashboard2/tekvideo-dashboard.html')}">
</head>

<body>

<g:content key="sidebar-left">
    <twbs:select list="${DashboardPeriod.values()}" name="dataRange" labelText="Vis data"
                 value="${DashboardPeriod.MONTH}"/>
    <div id="custom-range-selector">
        <twbs:input name="from" labelText="Fra"/>
        <twbs:input name="to" labelText="Til" />
    </div>
    <ul class="fa-ul tree">
        <g:each in="${courses.findAll { it != null }}" var="course">
            <li>
                <fa:icon listItem="true" icon="${FaIcon.GRADUATION_CAP}"/>
                <a href="#/course/${course.id}" class="tree-link">${course.fullName} [${course.name}]</a>
                <a href="#" class="pull-right collapse-tree"><fa:icon icon="${FaIcon.COMPRESS}"/></a>
                <ul class="fa-ul">
                    <g:each in="${course.subjects.findAll { it != null }}" var="subject">
                        <li>
                            <fa:icon listItem="true" icon="${FaIcon.USERS}"/>
                            <a href="#/subject/${subject.id}" class="tree-link">${subject.name}</a>
                            <a href="#" class="pull-right collapse-tree"><fa:icon icon="${FaIcon.COMPRESS}"/></a>
                            <ul class="fa-ul">
                                <g:each in="${subject.videos.findAll { it != null }}" var="video">
                                    <li>
                                        <fa:icon listItem="true" icon="${FaIcon.PLAY}"/>
                                        <a href="#/video/${video.id}" class="tree-link">${video.name}</a>
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
        <tekvideo-dashboard
                node="video/12083"
                period-from="0"
                period-to="4000000000"
                base-url="${createLink(absolute:true, uri:'/')}">
        </tekvideo-dashboard>
    </twbs:column>
</twbs:row>

<script>
    var baseUrl = "${createLink(absolute:true, uri:'/')}";
</script>

</body>

</html>