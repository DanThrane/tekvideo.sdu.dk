//= require answers/AnswersPage
//= require comments/CommentsPage
//= require students/StudentsPage
//= require views/ViewsPage

$(function () {
    var activePage = "views";
    var stack = new CardStack("#stack");
    var $dataRange = $("#dataRange");
    var pages = {
        "answers" : new AnswersPage(),
        "comments": new CommentsPage(),
        "students": new StudentsPage(),
        "views"   : new ViewsPage()
    };

    $(".menu-item").click(function (e) {
        e.preventDefault();
        var id = $(this).data("href");
        stack.select("#" + id);
        $(".nav li").removeClass("active");
        $(this).parent().addClass("active");
        activePage = id;
        initPage(activePage, $dataRange.val());
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
        initPage(activePage, $(this).val());
    });

    initPage(activePage, $dataRange.val());

    function initPage(id, period) {
        console.log("Init on " + id + " with period " + period);
        pages[activePage].onSelect(null, period);
    }
});
