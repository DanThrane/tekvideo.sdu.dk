var events = {};

(function(module) {
    var events = [];
    var disabled = true;
    var endpoint;
    var meta = {};

    if (!Date.now) { Date.now = function() { return new Date().getTime(); } }

    function mergeProperties(obj1, obj2){
        var name;
        var result = {};
        for (name in obj1) {
            if (obj1.hasOwnProperty(name)) result[name] = obj1[name];
        }
        for (name in obj2) {
            if (obj2.hasOwnProperty(name)) result[name] = obj2[name];
        }
        return result;
    }

    module.emit = function(event, flush) {
        var eventWithMetaData = mergeProperties(event, meta);
        eventWithMetaData.timestamp = Date.now();
        events.push(eventWithMetaData);
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

    module.setMetaData = function(data) {
        meta = data;
    };
    return module;
})(events);