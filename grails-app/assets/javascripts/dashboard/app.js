//= require answers/AnswersPage
//= require comments/CommentsPage
//= require students/StudentsPage
//= require views/ViewsPage

$(function () {
    (function () {
        var exports = {};
        exports.activePage = "views";
        exports.root = null;

        var stack = new CardStack("#stack");

        var pages = {
            "answers" : new AnswersPage(exports),
            "comments": new CommentsPage(exports),
            "students": new StudentsPage(exports),
            "views"   : new ViewsPage(exports)
        };

        var $dataRange = $("#dataRange");
        var $spinner = $("#spinner");

        $(".menu-item").click(function (e) {
            e.preventDefault();
            var id = $(this).data("href");
            stack.select("#" + id);
            $(".nav li").removeClass("active");
            $(this).parent().addClass("active");
            exports.activePage = id;
            onDataChange();
        });

        $(".notification-comment-component").each(function (idx, element) {
            var component = new NotificationCommentComponent(element);
            component.init();
        });

        $(".answer-breakdown").each(function (idx, element) {
            var breakdown = new AnswerBreakdown(element);
            breakdown.init();
            breakdown._onExpansion();
        });

        var treeNodes = $(".collapse-tree");
        treeNodes.click(function (e) {
            e.preventDefault();
            $(this).parent().find("ul").toggle();
            $(this).find("i").toggleClass("fa-compress fa-expand");
        });
        treeNodes.click(); // Collapse-all by simulating that we click each of them

        $dataRange.change(function () {
            onDataChange();
        });

        $(".tree-link").click(function (e) {
            e.preventDefault();
            $(".tree .selected").removeClass("selected");
            $(this).parent().addClass("selected");
            exports.root = $(this).attr("href").replace("#/", "");
            onDataChange();
        });

        exports.displaySpinner = function() {
            $spinner.removeClass("hide");
        };

        exports.removeSpinner = function() {
            $spinner.addClass("hide");
        };

        function onDataChange() {
            pages[exports.activePage].onSelect(exports.root, $dataRange.val());
        }
        return exports;
    }());
});
