(function (module) {
    function ListManipulator(elementSelector, upArrowSelector, downArrowSelector, deleteSelector) {
        this.elementSelector = elementSelector;
        this.upArrowSelector = upArrowSelector;
        this.downArrowSelector = downArrowSelector;
        this.deleteSelector = deleteSelector;
    }

    ListManipulator.prototype.init = function() {
        var self = this;
        $(self.upArrowSelector).click(function() {
            var thisItem = $(this).closest(self.elementSelector);
            var index = thisItem.index();
            if (index == 0) return;

            var items = $(self.elementSelector);

            thisItem.insertBefore($(items[index - 1]));
        });

        $(self.downArrowSelector).click(function() {
            var thisItem = $(this).closest(self.elementSelector);
            var index = thisItem.index();
            var items = $(self.elementSelector);

            if (index == items.length - 1) return;

            thisItem.insertAfter($(items[index + 1]));
        });

        if (self.deleteSelector) { // Allow for the delete operation to be optional
            $(self.deleteSelector).click(function () {
                $(this).closest(self.elementSelector).remove();
            });
        }
    };

    ListManipulator.prototype.getItems = function () {
        return $(this.elementSelector);
    };

    ListManipulator.prototype.map = function(mapper) {
        return $.map(this.getItems(), function (element) {
            return mapper($(element));
        });
    };

    module.ListManipulator = ListManipulator;
}(window));