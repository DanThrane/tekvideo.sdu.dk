//= require cardstack

var AjaxUtil = {};
(function(exports) {
    // TODO documentation
    function registerJSONForm(buttonSelector, endpoint, dataSupplier, callbacks) {
        var button = new AjaxSubmitButton(buttonSelector);
        $(buttonSelector).click(function (e) {
            var data = dataSupplier(e);
            var hasCallbacks = callbacks !== undefined;
            button.setLoadingState();
            Util.postJson(endpoint, data, {
                success: function(data) {
                    button.setSuccessState();
                    if (hasCallbacks && callbacks.success !== undefined) {
                        callbacks.success(data);
                    }
                },
                error: function (data) {
                    button.setErrorState();
                    if (hasCallbacks && callbacks.success !== undefined) {
                        callbacks.error(data);
                    }
                },
                complete: function() {
                    if (hasCallbacks && callbacks.complete !== undefined) {
                        callbacks.complete();
                    }

                    setTimeout(function() { button.setDefaultState(); }, 2000);
                }
            });
        });
    }

    exports.registerJSONForm = registerJSONForm;
})(AjaxUtil);

(function(exports) {
    var AjaxSubmitButton = function (selector) {
        this.cardStack = new CardStack(selector + " .card-stack");
    };

    AjaxSubmitButton.prototype.setDefaultState = function() {
        this.cardStack.select(".default");
    };

    AjaxSubmitButton.prototype.setLoadingState = function() {
        this.cardStack.select(".loading");
    };

    AjaxSubmitButton.prototype.setSuccessState = function() {
        this.cardStack.select(".success");
    };

    AjaxSubmitButton.prototype.setErrorState = function() {
        this.cardStack.select(".error");
    };
    exports.AjaxSubmitButton = AjaxSubmitButton;
})(window);

