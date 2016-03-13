var ManagementTreeView = (function () {
    function ManagementTreeView(sel) {
        this.selector = sel;
    }

    function handleEdit(node) {
        return function (obj) {
            var location = null;
            switch (node.type) {
                case "course":
                    location = "manage/" + node.id;
                    break;
                case "subject":
                    location = "manageSubject/" + node.id;
                    break;
                case "video":
                    location = "editVideo/" + node.id;
                    break;
            }
            if (location !== null) {
                document.location.href = location;
            }
        }
    }

    function handleStatusChange(node, status) {
        return function (obj) {
            var location = null;
            switch (node.type) {
                case "course":
                    location = "courseStatus/" + node.id + "?status=" + status;
                    break;
                case "subject":
                    location = "subjectStatus/" + node.id + "?status=" + status;
                    break;
                case "video":
                    location = "videoStatus/" + node.id + "?status=" + status;
                    break;
            }
            if (location !== null) {
                document.location.href = location; // TODO This will redirect to the wrong page
            }
        }
    }

    function handleCreate(node) {
        return function (obj) {
            var location = null;
            switch (node.type) {
                case "course":
                    location = "createSubject/" + node.id;
                    break;
                case "subject":
                    location = "createVideo/" + node.parent + "?subject=" + node.id;
                    break;
            }
            if (location !== null) {
                document.location.href = location;
            }
        };
    }

    ManagementTreeView.prototype.init = function () {
        $(this.selector).jstree({
            "core": {
                "check_callback": true,
                "themes": {
                    "name": "proton",
                    "responsive": true
                },
                "data": {
                    "url": function (node) {
                        if (node.id === "#") {
                            return "jstCourses";
                        } else {
                            switch (node.type) {
                                case "course":
                                    return "jstSubjects/" + node.id;
                                case "subject":
                                    return "jstVideos/" + node.id;
                                default:
                                    console.log("Unknown node ");
                                    console.log(node);
                                    return null;
                            }
                        }
                    },
                    "dataType": "json",
                    "contentType": "application/json"
                }
            },
            "types": {
                "#": {
                    "valid_children": ["course"]
                },
                "course": {
                    "icon": "fa fa-graduation-cap",
                    "valid_children": ["subject"]
                },
                "subject": {
                    "icon": "fa fa-users",
                    "valid_children": ["video"]
                },
                "video": {
                    "icon": "fa fa-play",
                    "valid_children": []
                }
            },
            "plugins": ["contextmenu", "dnd", "search", "state", "types", "wholerow"],
            "contextmenu": {
                "items": function (node) {
                    var options = {};
                    if (node.type !== "video") {
                        options.create = {
                            "label": "Tilføj element",
                            "action": handleCreate(node)
                        };
                    }
                    options.edit = {
                        "label": "Rediger",
                        "action": handleEdit(node)
                    };
                    options.move_to_visible = {
                        "label": "Flyt til: Synlige",
                        "separator_before": true,
                        "_disabled": true,
                        "action": handleStatusChange(node, "VISIBLE")
                    };
                    options.move_to_invisible = {
                        "label": "Flyt til: Usynlige",
                        "action": handleStatusChange(node, "INVISIBLE")
                    };
                    options.move_to_trash = {
                        "label": "Flyt til: Papirkurv",
                        "action": handleStatusChange(node, "TRASH")
                    };
                    return options;
                }
            },
            "dnd": {
                "is_draggable": function (nodes) {
                    for (var i = 0; i < nodes.length; i++) {
                        if (nodes[i].type === "course") {
                            return false;
                        }
                    }
                    return true;
                }
            }
        }).bind("move_node.jstree", function (e, data) {
            if (data.old_parent !== data.parent) {
                console.log("Not yet implemented.");
            } else {
                var parent = parseInt(data.parent);
                var orderStrings = data.new_instance._model.data[parent].children;
                var order = [];
                for (var i = 0; i < orderStrings.length; i++) order[i] = parseInt(orderStrings[i]);
                switch (data.node.type) {
                    case "subject":
                        var message = {
                            course: parent,
                            order: order
                        };
                        Util.postJson("updateSubjects", message, {});
                        break;
                    case "video":
                        var message = {
                            subject: parent,
                            order: order
                        };
                        Util.postJson("updateVideos", message, {});
                        break;
                }
            }
        });
    };

    return ManagementTreeView;
})();