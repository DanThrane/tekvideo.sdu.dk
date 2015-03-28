var events = {};

(function(module) {
    var events = [];
    var disabled = true;
    var endpoint;
    if (!Date.now) { Date.now = function() { return new Date().getTime(); } }
    
    module.emit = function(event, flush) {
        event.timestamp = Date.now();
        events.push(event);
        if (flush) module.flush();
    };
    
    module.start = function() {
        setInterval(function() { module.flush(); }, 5000);
    };

    module.flush = function(async) {
        if (disabled) return;
        async = typeof async !== 'undefined' ? async : true;
        if (events.length > 0) {
            $.ajax({
                type: "POST",
                url: endpoint,
                async: async,
                data: JSON.stringify({ events: events }),
                success: function(data) {
                    console.log(data);
                },
                contentType: "application/json",
                dataType: 'json'
            });
            events = [];
        }
    };

    module.configure = function(ep) {
        disabled = false;
        endpoint = ep;
    };

    module.disable = function() {
        disabled = true;
    };
    
    module.enable = function() {
        disabled = false;
    };
    return module;
})(events);