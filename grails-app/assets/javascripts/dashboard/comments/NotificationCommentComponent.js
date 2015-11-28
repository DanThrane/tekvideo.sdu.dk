var NotificationCommentComponent = (function () {
    function NotificationCommentComponent(element) {
        this.element = element;
    }

    NotificationCommentComponent.prototype.init = function () {
        var $element = $(this.element);
        var $short = $($element.find(".short"));
        var $expanded = $($element.find(".expanded"));
        $expanded.hide();

        $element.find(".expand-button").click(function (e) {
            e.preventDefault();
            $short.slideUp();
            $expanded.slideDown();
        });

        $element.find(".shrink-button").click(function (e) {
            e.preventDefault();
            $expanded.slideUp();
            $short.slideDown();
        });
    };
    return NotificationCommentComponent;
}());