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