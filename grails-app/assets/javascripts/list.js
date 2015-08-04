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

            self.swapVideos(index - 1, index, function() {
                thisItem.insertBefore($(items[index - 1]));
            });
        });

        $(self.downArrowSelector).click(function() {
            var thisItem = $(this).closest(self.elementSelector);
            var index = thisItem.index();
            var items = $(self.elementSelector);

            if (index == items.length - 1) return;

            self.swapVideos(index, index + 1, function() {
                thisItem.insertAfter($(items[index + 1]));
            });
        });

        if (self.deleteSelector) { // Allow for the delete operation to be optional
            $(self.deleteSelector).click(function () {
                $(this).closest(self.elementSelector).remove();
            });
        }
    };

    ListManipulator.prototype.swapVideos = function (idxDown, idxUp, callback) {
        var allItems = $(this.elementSelector);

        var upperItem = $(allItems[idxDown]);
        var lowerItem = $(allItems[idxUp]);

        var upperPos = upperItem.position();
        var lowerPos = lowerItem.position();

        var upperItemMovement = lowerPos.top - upperPos.top;
        var lowerItemMovement = upperPos.top - lowerPos.top;

        upperItem.animate({ top: movementFromDelta(upperItemMovement) });
        lowerItem.animate({ top: movementFromDelta(lowerItemMovement )}, function() {
            callback();
            // Clear the animated properties post-callback
            upperItem.css("top", "initial");
            lowerItem.css("top", "initial");
        });
    };

    ListManipulator.prototype.getItems = function () {
        return $(this.elementSelector);
    };

    ListManipulator.prototype.map = function(mapper) {
        return $.map(this.getItems(), function (element) {
            return mapper($(element));
        });
    };

    function movementFromDelta(delta) {
        if (delta <= 0) {
            return "-=" + Math.abs(delta);
        } else {
            return "+=" + Math.abs(delta);
        }
    }

    module.ListManipulator = ListManipulator;
}(window));