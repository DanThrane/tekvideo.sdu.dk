var CardStack = function(selector, useTransition) {
    this.element = $(selector);

    // Adds the relevant transition class. The "no-transition" class is always added, to avoid an initial "hiding"
    // animation
    this.element.addClass("no-transition");
    if (useTransition) {
        var _this = this;
        setTimeout(function () { _this.element.removeClass("no-transition").addClass("transition"); }, 1);
    }
};

CardStack.prototype.hideAll = function () {
    this.element.find(".card-item.active").removeClass("active");
};

CardStack.prototype.select = function (id) {
    this.hideAll();
    return this.element.find(id).addClass("active");
};