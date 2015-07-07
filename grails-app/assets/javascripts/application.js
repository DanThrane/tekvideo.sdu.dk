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

String.prototype.format = function() {
    var args = arguments;
    return this.replace(/{(\d+)}/g, function(match, number) {
        return typeof args[number] != 'undefined'
            ? args[number]
            : match
            ;
    });
};

String.prototype.startsWith = function (prefix) {
    return this.indexOf(prefix) === 0;
};

String.prototype.paddingLeft = function (paddingValue) {
    return String(paddingValue + this).slice(-paddingValue.length);
};