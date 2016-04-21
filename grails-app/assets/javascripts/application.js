//= require jquery
//= require bootstrap
//= require lib/popcorn
//= require lib/underscore
//= require lib/mathquill.min
//= require lib/kas
//= require lib/tex-wrangler
//= require events
//= require interactivevideos
//= require cardstack

var Util = {};
Util.postJson = function(url, data, callbacks) {
    $.ajax({
        type: "POST",
        url: url,
        async: true,
        data: JSON.stringify(data),
        success: callbacks.success,
        error: callbacks.error,
        complete: callbacks.complete,
        contentType: "application/json",
        dataType: 'json'
    });
};

Util.escapeHtml = function(string) {
    var entityMap = {
        "&": "&amp;",
        "<": "&lt;",
        ">": "&gt;",
        '"': '&quot;',
        "'": '&#39;',
        "/": '&#x2F;'
    };

    return String(string).replace(/[&<>"'\/]/g, function (s) {
        return entityMap[s];
    });
};

Util.raw = function(str) {
    return { content: str, raw: true };
};

String.prototype.format = function() {
    var args = arguments;
    return this.replace(/{(\d+)}/g, function(match, number) {
        var arg = args[number];
        if (typeof arg != 'undefined') {
            if (typeof arg === "object") {
                if (arg.hasOwnProperty("raw") && arg.raw === true &&
                    arg.hasOwnProperty("content")) {
                    return arg.content;
                } else {
                    throw new Error("Unable to format object!");
                }
            } else {
                return Util.escapeHtml(arg);
            }
        } else {
            return match;
        }
    });
};

String.prototype.startsWith = function (prefix) {
    return this.indexOf(prefix) === 0;
};

String.prototype.paddingLeft = function (paddingValue) {
    return String(paddingValue + this).slice(-paddingValue.length);
};