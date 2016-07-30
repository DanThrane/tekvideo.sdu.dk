var AccountManagement = {};

$(function () {
    var ROLES = ["ROLE_STUDENT", "ROLE_TEACHER"];

    var userEndpoint;
    var saveEndpoint;
    var cachedUsers;

    var $refreshButton;
    var $search;
    var $studentCheckbox;
    var $teacherCheckbox;
    var $tekvideoCheckbox;
    var $ssoCheckbox;
    var $userEntries;

    var userTemplate;
    var sduLogoTemplate;
    var roleTemplates = {};

    var $editUsername;
    var $editRealName;
    var $editEmail;
    var $editElearn;
    var $editSduUser;
    var $editTekUser;
    var $editRoleCheckboxes = {};
    var $editSaveButton;
    var $editCancelButton;

    var editingUser;

    var mainStack;

    AccountManagement.init = function() {
        $search = $("#search");
        $search.on("input", function() {
            AccountManagement.renderUsersWithReappliedFilter();
        });

        $studentCheckbox = $("#student");
        $teacherCheckbox = $("#teacher");
        $tekvideoCheckbox = $("#tekvideo-user");
        $ssoCheckbox = $("#sdu-sso");

        $(".checkbox-dropdown a").click(function () {
            var $target = $(event.currentTarget);
            var $checkbox = $target.find("input[type=checkbox]");
            if ($checkbox) {
                var isChecked = $checkbox.is(":checked");
                $checkbox.prop("checked", !isChecked);
                AccountManagement.renderUsersWithReappliedFilter();
                return false; // Prevent dropdown from closing
            }
        });

        $refreshButton = $("#refresh-button");
        $refreshButton.click(function() {
            AccountManagement.refreshUsers();
        });

        $userEntries = $("#user-entries");

        userTemplate = $("#user-template").html();
        sduLogoTemplate = $("#sso-template").html();

        for (var i = 0; i < ROLES.length; i++) {
            var role = ROLES[i];
            roleTemplates[role] = $("#role-template-" + role).html();
        }

        mainStack = new CardStack("#main-stack");
        initEditForm();
    };

    AccountManagement.registerUserEndpoint = function (url) {
        userEndpoint = url;
    };

    AccountManagement.registerSaveEndpoint = function (url) {
        saveEndpoint = url;
    };

    AccountManagement.refreshUsers = function () {
        startLoadingIndicator();
        $.get(userEndpoint, function (data) {
            AccountManagement.insertUserData(data);
            AccountManagement.renderUsersWithReappliedFilter();
            stopLoadingIndicator();
        });
    };

    function startLoadingIndicator() {
        $refreshButton.find("i").addClass("fa-spin");
    }

    function stopLoadingIndicator() {
        $refreshButton.find("i").removeClass("fa-spin");
    }

    AccountManagement.renderUsersWithReappliedFilter = function () {
        var searchParameters = getSearchParameters();
        var users = findUsers(cachedUsers, searchParameters);

        $userEntries.empty();
        for (var i = 0; i < users.length; i++) {
            var user = users[i];
            $userEntries.append(renderUser(user));
        }
    };

    function renderUser(user) {
        var rolesHtml = "";
        for (var i = 0; i < user.roles.length; i++) {
            var roleName = user.roles[i];
            var template = roleTemplates[roleName];
            if (template) {
                rolesHtml += template;
            } else {
                rolesHtml += "(" + roleName + ")";
            }
        }

        var row = $(userTemplate.format(
            Util.raw(user.isCas ? sduLogoTemplate : "TekVideo"),
            user.realName ? user.realName : "Ikke angivet",
            user.username,
            user.email ? user.email : "Ikke angivet",
            user.elearnId ? user.elearnId : "Ikke angivet",
            Util.raw(rolesHtml)
        ));

        row.find(".edit-button").click(function() {
            AccountManagement.editUser(user);
        });

        return row;
    }

    AccountManagement.setSearchParameters = function (parameters) {
        $search.val(parameters.freeText);
        $studentCheckbox.prop("checked", parameters.isStudent);
        $teacherCheckbox.prop("checked", parameters.isTeacher);
        $tekvideoCheckbox.prop("checked", parameters.isTekUser);
        $ssoCheckbox.prop("checked", parameters.isSsoUser);
    };

    function getSearchParameters() {
        var isStudent = $studentCheckbox.is(":checked");
        var isTeacher = $teacherCheckbox.is(":checked");
        var isTekUser = $tekvideoCheckbox.is(":checked");
        var isSsoUser = $ssoCheckbox.is(":checked");
        var freeText = $search.val();

        return {
            isStudent: isStudent,
            isTeacher: isTeacher,
            isTekUser: isTekUser,
            isSsoUser: isSsoUser,
            freeText: freeText
        };
    }

    function findUsers(users, parameters) {
        return users.filter(function (user) {
            return userMatchesFreeTextQuery(user, parameters) && 
                userMatchesRoleQuery(user, parameters) &&
                userMatchesTypeQuery(user, parameters);
        });
    }

    function userMatchesFreeTextQuery(user, parameters) {
        var freeText = parameters.freeText;
        if (freeText) {
            var normalizedQuery = freeText.toLowerCase();
            var fieldsToCheck = ["username", "email", "elearnId", "realName"];

            return fieldsToCheck.some(function (field) {
                var fieldValue = user[field];
                if (fieldValue) {
                    return fieldValue.toLowerCase().indexOf(normalizedQuery) !== -1;
                } else {
                    return false;
                }
            });
        } else {
            return true;
        }
    }

    function userMatchesRoleQuery(user, parameters) {
        var requiredRoles = [];
        if (parameters.isStudent) requiredRoles.push("ROLE_STUDENT");
        if (parameters.isTeacher) requiredRoles.push("ROLE_TEACHER");

        if (requiredRoles.length > 0) {
            return arrayContainsAll(user.roles, requiredRoles);
        } else {
            return true;
        }
    }

    function userMatchesTypeQuery(user, parameters) {
        if (parameters.isSsoUser || parameters.isTekUser) {
            return userMatchesType(user, parameters);
        } else {
            return true;
        }
    }

    function userMatchesType(user, parameters) {
        return parameters.isSsoUser === user.isCas || parameters.isTekUser === !user.isCas;
    }

    function arrayContainsAll(haystack, needles) {
        return needles.every(function (element) {
            return haystack.indexOf(element) !== -1;
        });
    }

    AccountManagement.insertUserData = function (response) {
        cachedUsers = response.result;
    };

    AccountManagement.editUser = function (user) {
        resetEditState();

        editingUser = user;

        $editRealName.val(user.realName);
        $editUsername.text(user.username);
        $editEmail.val(user.email);
        $editElearn.val(user.elearnId);
        if (user.isCas) {
            $editSduUser.removeClass("hide");
        } else {
            $editTekUser.removeClass("hide");
        }
        for (var i = 0; i < user.roles.length; i++) {
            var checkbox = $editRoleCheckboxes[user.roles[i]];
            if (checkbox) {
                checkbox.prop("checked", true);
            }
        }
        mainStack.select("#edit-user");
    };

    function resetEditState() {
        $(".edit-user-type").addClass("hide");
        for (var key in $editRoleCheckboxes) {
            var $editRoleCheckbox = $editRoleCheckboxes[key];
            $editRoleCheckbox.prop("checked", false);
        }
    }

    function onEditSave() {
        var roles = [];
        for (var i = 0; i < ROLES.length; i++) {
            var role = ROLES[i];
            var $checkbox = $editRoleCheckboxes[role];
            if ($checkbox.is(":checked")) {
                roles.push(role);
            }
        }

        var request = {
            username: editingUser.username,
            isCas: editingUser.isCas,
            realName: $editRealName.val(),
            email: $editEmail.val(),
            elearnId: $editElearn.val(),
            roles: roles
        };

        updateEditingUser(request);

        Util.postJson(saveEndpoint, request, {});
        displayUsers();
    }

    function updateEditingUser(updated) {
        editingUser.realName = updated.realName;
        editingUser.email = updated.email;
        editingUser.elearnId = updated.elearnId;
        editingUser.roles = updated.roles;
        AccountManagement.renderUsersWithReappliedFilter();
    }

    function onEditCancel() {
        displayUsers();
    }

    function displayUsers() {
        mainStack.select("#view-users");
    }

    function initEditForm() {
        for (var i = 0; i < ROLES.length; i++) {
            var role = ROLES[i];
            $editRoleCheckboxes[role] = $("#edit-role-" + role);
        }
        $editRealName = $("#edit-realName");
        $editUsername = $("#edit-username");
        $editEmail = $("#edit-email");
        $editElearn = $("#edit-elearnId");
        $editSduUser = $("#sso-user-type");
        $editTekUser = $("#tekvideo-user-type");
        $editSaveButton = $("#edit-save-button");
        $editSaveButton.click(function() {
            onEditSave();
        });
        $editCancelButton = $("#edit-cancel-button");
        $editCancelButton.click(function() {
            onEditCancel();
        });
    }

});
