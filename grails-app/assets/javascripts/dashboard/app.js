//= require answers/AnswersPage
//= require comments/CommentsPage
//= require students/StudentsPage
//= require views/ViewsPage
//= require moment
//= require bootstrap-datetimepicker.min

$(function () {
    var exports = {};
    exports.activePage = "answers";
    exports.root = null;

    var stack = new CardStack("#stack");
    var disableChangeEventOnField = false;

    var pages = {
        "answers": new AnswersPage(exports),
        "comments": new CommentsPage(exports),
        "students": new StudentsPage(exports),
        "views": new ViewsPage(exports)
    };

    var $dataRange = $("#dataRange");
    var $spinner = $("#spinner");

    var baseConfig = {locale: "da"};
    var fromConfig = jQuery.extend(true, {}, baseConfig);
    var toConfig = jQuery.extend(true, {}, baseConfig);
    toConfig.useCurrent = false;

    var $from = $("#from");
    var $to = $("#to");
    var $customRangeSelector = $("#custom-range-selector");

    $from.datetimepicker(fromConfig);
    $to.datetimepicker(toConfig);

    $(".menu-item").click(function (e) {
        e.preventDefault();
        if (exports.root !== null) {
            var id = $(this).data("href");
            stack.select("#" + id);
            $(".nav li").removeClass("active");
            $(this).parent().addClass("active");
            exports.activePage = id;
            onDataChange();
        }
    });

    var treeNodes = $(".collapse-tree");
    treeNodes.click(function (e) {
        e.preventDefault();
        $(this).parent().find("ul").toggle();
        $(this).find("i").toggleClass("fa-compress fa-expand");
    });
    treeNodes.click(); // Collapse-all by simulating that we click each of them

    $dataRange.change(function () {
        var CUSTOM = -1;
        var val = parseInt($dataRange.val());
        console.log(val);
        if (val !== CUSTOM) {
            console.log("...");
            $customRangeSelector.addClass("hide");

            var now = Date.now();
            var since = (val == 0) ? 0 : now - (1000 * 60 * 60 * 24 * val);

            disableChangeEventOnField = true;
            $from.data("DateTimePicker").date(new Date(since));
            $to.data("DateTimePicker").date(new Date(now));
            onDataChange();
            disableChangeEventOnField = false;
        } else {
            console.log("Showing custom range");
            $customRangeSelector.removeClass("hide");
        }
    });

    $(".tree-link").click(function (e) {
        e.preventDefault();
        if (exports.root === null) {
            // Enable other tabs
            $(".menu-item").parent().removeClass("disabled")
        }
        $(".tree .selected").removeClass("selected");
        $(this).parent().addClass("selected");
        exports.root = $(this).attr("href").replace("#/", "");
        onDataChange();
    });

    exports.displaySpinner = function () {
        $spinner.removeClass("hide");
    };

    exports.removeSpinner = function () {
        $spinner.addClass("hide");
    };

    exports.refresh = function () {
        onDataChange();
    };

    $from.on("dp.change", function (e) {
        $to.data("DateTimePicker").minDate(e.date);
        if (!disableChangeEventOnField) {
            onDataChange();
        }
    });

    $to.on("dp.change", function (e) {
        $from.data("DateTimePicker").maxDate(e.date);
        if (!disableChangeEventOnField) {
            onDataChange();
        }
    });

    $dataRange.trigger("change");

    function onDataChange() {
        console.log("Calling");
        pages[exports.activePage].onSelect(exports.root, readTimestamp($from), readTimestamp($to));
    }

    function readTimestamp(selector) {
        var timestamp = parseInt(selector.data("DateTimePicker").date().format("x"));
        return (timestamp < 0) ? 0 : timestamp; // Avoid problems with dates pre-dating 1970
    }
});
