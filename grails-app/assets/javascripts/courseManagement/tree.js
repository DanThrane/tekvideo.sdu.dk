var ManagementTreeView = (function () {
    var TYPES = {
        "#": {
            "valid_children": ["course"]
        },
        "course": {
            "icon": "fa fa-graduation-cap",
            "valid_children": ["subject"]
        },
        "subject": {
            "icon": "fa fa-users",
            "valid_children": ["video", "writtenexercisegroup"]
        },
        "video": {
            "icon": "fa fa-play",
            "valid_children": []
        },
        "writtenexercisegroup": {
            "icon": "fa fa-pencil-square",
            "valid_children": []
        }
    };

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
                case "writtenexercisegroup":
                    location = self.baseUrl + "courseManagement/editWrittenExercise/" + node.id;
                    break;
            }
            if (location !== null) {
                document.location.href = location;
            }
        }
    };
    ManagementTreeView.prototype.handleCopy = function (node) {
        var self = this;
        return function (obj) {
            self.copiedNode = node;
            console.log("Copied " + node.id);
        };
    };

    ManagementTreeView.prototype.handleInsert = function (node) {
        var self = this;
        return function (obj) {
            var location = self.baseUrl + "courseManagement/";
            console.log("Inserting ", self.copiedNode);
            switch (node.type) {
                case "subject":
                    location += "copyExerciseToSubject";
                    break;
                case "course":
                    location += "copySubjectToCourse";
                    break;
            }

            Util.postJson(location, { destination: node.id, element: self.copiedNode.id }, {
                success: function() {
                    self.tree.refresh();
                },
                error: function() {
                    console.log("ERROR!")
                }
            });
        };
    };

    ManagementTreeView.prototype.canInsertTo = function (node) {
        if (!this.copiedNode) return false;
        var typeDef = TYPES[node.type];
        if (!typeDef) return false;
        return typeDef.valid_children.indexOf(this.copiedNode.type) !== -1;
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
                case "writtenexercisegroup":
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

    ManagementTreeView.prototype.handleCreateWrittenExercise = function (node) {
        var self = this;
        return function (obj) {
            document.location.href = self.baseUrl + "courseManagement/createWrittenExercise/" + node.id;
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
            case "writtenexercisegroup":
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
            "types": TYPES,
            "plugins": ["contextmenu", "dnd", "search", "state", "types", "wholerow"],
            "contextmenu": {
                "items": function (node) {
                    var options = {};
                    if (node.type === "course") {
                        options.create = {
                            "label": "Tilføj element",
                            "action": self.handleCreate(node)
                        };
                    }
                    if (node.type === "subject") {
                        options.create_video = {
                            label: "Tilføj video",
                            action: self.handleCreate(node)
                        };
                        options.create_written_exercise = {
                            label: "Tilføj skriftlig opgave",
                            action: self.handleCreateWrittenExercise(node)
                        }
                    }
                    options.edit = {
                        "label": "Rediger",
                        "action": self.handleEdit(node)
                    };
                    options.copy = {
                        "label": "Kopier",
                        "action": self.handleCopy(node)
                    };
                    options.insert = {
                        "label": "Indsæt",
                        "_disabled": !self.canInsertTo(node),
                        "action": self.handleInsert(node)
                    };
                    options.move_to_invisible = {
                        "separator_before": true,
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
                    case "writtenexercisegroup": // TODO @refactor needs to handle writtenexercisegroups
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

        $(this.selector).bind("contextmenu", function(e){
            // Disable context menu within the tree element.

            // Normally when we right click within the tree element it is to get the tree menu, and not the ordinary
            // browser menu. Thus we disable it to give a more pleasant user experience.

            // We need this because of the small amount of spacing we have between individual items. These spaces
            // would trigger the normal browser menu.
            return false;
        });

        this.tree = $(this.selector).jstree(true);
    };

    return ManagementTreeView;
})();