var ManagementTreeView = (function () {
    function ManagementTreeView(sel, base) {
        this.selector = sel;
        this.baseUrl = base;
    }

    ManagementTreeView.prototype.handleEdit = function (node) {
        var self = this;
        return function (obj) {
            var location = null;
            switch (node.type) {
                case "course":
                    location = self.baseUrl + "courseManagement/manage/" + node.id;
                    break;
                case "subject":
                    location = self.baseUrl + "courseManagement/manageSubject/" + node.id;
                    break;
                case "video":
                    location = self.baseUrl + "courseManagement/editVideo/" + node.id;
                    break;
            }
            if (location !== null) {
                document.location.href = location;
            }
        }
    };

    ManagementTreeView.prototype.handleStatusChange = function (node, status) {
        var self = this;
        return function (obj) {
            var location = null;
            switch (node.type) {
                case "course":
                    location = self.baseUrl + "courseManagement/courseStatus/" + node.id + "?status=" + status;
                    break;
                case "subject":
                    location = self.baseUrl + "courseManagement/subjectStatus/" + node.id + "?status=" + status;
                    break;
                case "writtenexercise":
                case "video":
                    location = self.baseUrl + "courseManagement/exerciseStatus/" + node.id + "?status=" + status;
                    break;
            }
            if (location !== null) {
                document.location.href = location; // TODO This will redirect to the wrong page
            }
        }
    };

    ManagementTreeView.prototype.handleCreate = function (node) {
        var self = this;
        return function (obj) {
            var location = null;
            switch (node.type) {
                case "course":
                    location = self.baseUrl + "courseManagement/createSubject/" + node.id;
                    break;
                case "subject":
                    location = self.baseUrl + "courseManagement/createVideo/" + node.parent + "?subject=" + node.id;
                    break;
            }
            if (location !== null) {
                document.location.href = location;
            }
        };
    };

    ManagementTreeView.prototype._sendUpdatedOrder = function (data) {
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
                Util.postJson(self.baseUrl + "courseManagement/updateSubjects", message, {});
                break;
            case "writtenexercise":
            case "video":
                var message = {
                    subject: parent,
                    order: order
                };
                Util.postJson(self.baseUrl + "courseManagement/updateExercises", message, {});
                break;
        }
    };

    ManagementTreeView.prototype.init = function () {
        var self = this;
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
                            return self.baseUrl + "courseManagement/jstCourses";
                        } else {
                            switch (node.type) {
                                case "course":
                                    return self.baseUrl + "courseManagement/jstSubjects/" + node.id;
                                case "subject":
                                    return self.baseUrl + "courseManagement/jstExercises/" + node.id;
                                default:
                                    console.log("Unknown node");
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
                    "valid_children": ["video", "writtenexercise"]
                },
                "video": {
                    "icon": "fa fa-play",
                    "valid_children": []
                },
                "writtenexercise": {
                    "icon": "fa fa-pencil-square",
                    "valid_children": []
                }
            },
            "plugins": ["contextmenu", "dnd", "search", "state", "types", "wholerow"],
            "contextmenu": {
                "items": function (node) {
                    var options = {};
                    if (node.type !== "video" && node.type !== "writtenexercise") {
                        options.create = {
                            "label": "Tilføj element",
                            "action": self.handleCreate(node)
                        };
                    }
                    options.edit = {
                        "label": "Rediger",
                        "action": self.handleEdit(node)
                    };
                    options.move_to_visible = {
                        "label": "Flyt til: Synlige",
                        "separator_before": true,
                        "_disabled": true,
                        "action": self.handleStatusChange(node, "VISIBLE")
                    };
                    options.move_to_invisible = {
                        "label": "Flyt til: Usynlige",
                        "action": self.handleStatusChange(node, "INVISIBLE")
                    };
                    options.move_to_trash = {
                        "label": "Flyt til: Papirkurv",
                        "action": self.handleStatusChange(node, "TRASH")
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
                switch (data.node.type) {
                    case "subject":
                        var message = {
                            subject: data.node.id,
                            newCourse: data.parent,
                            position: data.position
                        };
                        Util.postJson(self.baseUrl + "courseManagement/moveSubject", message, {});
                        break;
                    case "writtenexercise": // TODO @refactor needs to handle writtenexercises
                    case "video":
                        var message = {
                            exercise: data.node.id,
                            newSubject: data.parent,
                            position: data.position
                        };
                        Util.postJson(self.baseUrl + "courseManagement/moveExercise", message, {});
                        break;
                }
            } else {
                self._sendUpdatedOrder(data);
            }
        });
    };

    return ManagementTreeView;
})();