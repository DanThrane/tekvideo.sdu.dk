var events = {};

(function(module) {
    var events = [];
    if (!Date.now) { Date.now = function() { return new Date().getTime(); } }
    
    module.emit = function(event, flush) {
        event.timestamp = Date.now();
        events.push(event);
        if (flush) module.flush();
    };
    
    module.init = function() {
        setInterval(function() { module.flush(); }, 5000);
    };

    module.flush = function() {
        if (events.length > 0) {
            $.ajax({
                type: "POST",
                url: "/videos_with_questions_web/event/register",
                data: JSON.stringify({ events: events }),
                success: function(data) { 
                    events = [];
                },
                contentType: "application/json",
                dataType: 'json'
            });
        }
    };
    
    return module;
})(events);