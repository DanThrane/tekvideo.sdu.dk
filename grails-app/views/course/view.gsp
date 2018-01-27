<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="grails.converters.JSON; dk.sdu.tekvideo.FaIcon; dk.danthrane.twbs.ButtonStyle; dk.danthrane.twbs.ButtonSize" %>
<html>
<head>
    <meta name="layout" content="main_fluid">
    <title>${title}</title>
    <g:render template="/polymer/includePolymer"/>

    <sdu:appResourceImport href="components/tv-browser.html"/>
</head>

<body>

<twbs:pageHeader>
    <h3>
        <span id="title">${title}</span>
        <small id="subtitle">${subtitle}</small>
        <span style="float: right;">
            <twbs:linkButton style="${ButtonStyle.SUCCESS}" controller="course" action="completeSignup"
                             id="${course.id}" elementId="signup-button" class="${inCourse ? "hide" : ""}">
                <fa:icon icon="${dk.sdu.tekvideo.FaIcon.USER_PLUS}"/>
                Tilmeld
            </twbs:linkButton>
            <twbs:linkButton style="${ButtonStyle.DANGER}" controller="course" action="completeSignoff"
                             id="${course.id}" elementId="signoff-button" class="${inCourse ? "" : "hide"}">
                Afmeld
            </twbs:linkButton>
        </span>
    </h3>
</twbs:pageHeader>

<twbs:row>
    <twbs:column>
        <ol class="breadcrumb" id="breadcrumb-list">
            <g:each in="${breadcrumbs}" var="crumb">
                <g:if test="${crumb.link}">
                    <li><a href="${raw(crumb.link)}">${crumb.title}</a></li>
                </g:if>
                <g:else>
                    <li class="active">${crumb.title}</li>
                </g:else>
            </g:each>
        </ol>
    </twbs:column>
</twbs:row>

<tv-browser id="browser" items='${data as grails.converters.JSON}'></tv-browser>

<script>
    $(function () {
        var $crumbsList = $("#breadcrumb-list");
        var $title = $("#title");
        var $subtitle = $("#subtitle");
        var $signupButton = $("#signup-button");
        var $signoffButton = $("#signoff-button");
        var browser = $("#browser")[0];
        var signupLink = "${createLink(controller: "course", action: "completeSignup")}/";
        var signoffLink = "${createLink(controller: "course", action: "completeSignoff")}/";

        // Regex which determines if a link change should be handled via an ajax call
        var ajaxRegex = new RegExp("\\/t\\/[^/]+\\/[^/]+\\/[^/]+\\/[^/]+(\\/[^/]+)?$");

        var NodeExplorer = {};
        NodeExplorer.setBreadcrumbs = function (crumbs) {
            $crumbsList.empty();
            for (var i = 0; i < crumbs.length; i++) {
                var crumb = crumbs[i];
                var item;
                if (crumb.link) {
                    item = $("<li><a href='" + Util.escapeHtml(crumb.link) + "'>" + Util.escapeHtml(crumb.title) +
                        "</li>");
                    item.on("click", function (e) {
                        if (processLink(e.target.getAttribute("href"))) {
                            e.preventDefault();
                        }
                    });
                } else {
                    item = $("<li class='active'>" + Util.escapeHtml(crumb.title) + "</li>");
                }
                $crumbsList.append(item);
            }
        };

        NodeExplorer.setTitle = function (title, subtitle) {
            if (subtitle === undefined) subtitle = "";
            $title.text(title);
            $subtitle.text(subtitle);
        };

        browser.addEventListener("link", function (e) {
            if (processLink(e.detail.link)) {
                history.pushState({}, "", e.detail.link);
                e.detail.original.preventDefault();
            }
        });

        window.onpopstate = function (e) {
            processLink(document.location.pathname);
        };

        function processLink(link) {
            if (ajaxRegex.test(link)) {

                $.get(link + ".json", function (data) {
                    NodeExplorer.setBreadcrumbs(data.breadcrumbs);
                    NodeExplorer.setTitle(data.title, data.subtitle);
                    browser.items = data.data;
                });
                return true;
            }
            return false;
        }

        $crumbsList.find("a").on("click", function (e) {
            if (processLink(e.target.getAttribute("href"))) {
               e.preventDefault();
            }
        });
    });

</script>
</body>
</html>
