<%@ page import="dk.sdu.tekvideo.FaIcon; dk.danthrane.twbs.Icon" %>
<%@ page import="dk.danthrane.twbs.ButtonStyle; dk.danthrane.twbs.ButtonSize" %>
<html>
<head>
    <meta name="layout" content="main_fluid">
    <title>Kontoadministration</title>
    <asset:javascript src="cardstack.js" />
</head>

<body>

<twbs:row>
    <twbs:column>
        <twbs:pageHeader>
            <h3>Kontoadministration</h3>
        </twbs:pageHeader>
    </twbs:column>
</twbs:row>

<div class="card-stack" id="main-stack">
    <div class="card-item active" id="view-users">
        <twbs:row>
            <twbs:column md="6">
                <twbs:input name="search" showLabel="false" placeholder="Fritekst søgning">
                    <g:content key="addon-right">
                        <twbs:inputGroupButton>
                            <twbs:button><fa:icon icon="${FaIcon.SEARCH}"/></twbs:button>
                        </twbs:inputGroupButton>
                    </g:content>
                </twbs:input>
            </twbs:column>
            <twbs:column md="5">
                <twbs:dropdownToggle>
                    <fa:icon icon="${FaIcon.USER_PLUS}"/> Attributer
                    <twbs:dropdownMenu class="checkbox-dropdown">
                        <twbs:dropdownHeader>Roler</twbs:dropdownHeader>
                        <twbs:dropdownItem>
                            <twbs:checkbox labelText="" name="student">
                                <fa:icon icon="${FaIcon.GRADUATION_CAP}"/>
                                Studerende
                            </twbs:checkbox>
                        </twbs:dropdownItem>
                        <twbs:dropdownItem>
                            <twbs:checkbox labelText="" name="teacher">
                                <fa:icon icon="${FaIcon.LOCK}"/>
                                Underviser
                            </twbs:checkbox>
                        </twbs:dropdownItem>
                        <twbs:dropdownHeader>Bruger type</twbs:dropdownHeader>
                        <twbs:dropdownItem>
                            <twbs:checkbox labelText="TekVideo" name="tekvideo-user"/>
                        </twbs:dropdownItem>
                        <twbs:dropdownItem>
                            <twbs:checkbox labelText="SDU SSO" name="sdu-sso"/>
                        </twbs:dropdownItem>
                    </twbs:dropdownMenu>
                </twbs:dropdownToggle>
            </twbs:column>
            <twbs:column md="1">
                <div style="text-align: right">
                    <twbs:button id="refresh-button">
                        <fa:icon icon="${FaIcon.REFRESH}"/>
                    </twbs:button>
                </div>
            </twbs:column>
        </twbs:row>
        <twbs:row>
            <twbs:column>
                <twbs:table responsive="true" hover="true">
                    <thead>
                    <twbs:th>Bruger type</twbs:th>
                    <twbs:th>Navn</twbs:th>
                    <twbs:th>Brugernavn</twbs:th>
                    <twbs:th>Email</twbs:th>
                    <twbs:th>E-learn ID</twbs:th>
                    <twbs:th>Rettigheder</twbs:th>
                    <twbs:th>Opgaver</twbs:th>
                    </thead>
                    <tbody id="user-entries">
                    </tbody>
                </twbs:table>
            </twbs:column>
        </twbs:row>
    </div>

    <div class="card-item" id="edit-user">
        <twbs:row>
            <twbs:column md="6" push-md="3">
                <sdu:card>
                    <p>
                        <b>Brugernavn</b>
                    </p>
                    <p id="edit-username"></p>
                    <twbs:input name="edit-realName" labelText="Navn" placeholder="Mangler"/>
                    <twbs:input name="edit-email" labelText="Email" placeholder="Mangler"/>
                    <twbs:input name="edit-elearnId" labelText="E-learn ID" placeholder="Mangler"/>
                    <p>
                        <b>Bruger type</b>
                    </p>
                    <p class="edit-user-type" id="sso-user-type">
                        <asset:image src="sdu_branch.png" class="sdu-logo-button"/>
                        SDU SSO
                    </p>
                    <p class="edit-user-type" id="tekvideo-user-type">
                        TekVideo
                    </p>

                    <p><b>Rettigheder</b></p>
                    <twbs:form inline="true">
                        <twbs:checkbox labelText="" name="edit-role-ROLE_TEACHER">
                            <fa:icon icon="${FaIcon.LOCK}"/> Underviser
                        </twbs:checkbox>
                        <twbs:checkbox labelText="" name="edit-role-ROLE_STUDENT">
                            <fa:icon icon="${FaIcon.GRADUATION_CAP}"/> Studerende
                        </twbs:checkbox>
                    </twbs:form>
                    <div style="text-align: right;">
                        <twbs:button style="${ButtonStyle.WARNING}" id="edit-cancel-button">
                            <fa:icon icon="${FaIcon.TIMES}" />
                            Annulér
                        </twbs:button>
                        <twbs:button style="${ButtonStyle.SUCCESS}" id="edit-save-button">
                            <fa:icon icon="${FaIcon.SAVE}"/>
                            Gem ændringer
                        </twbs:button>
                    </div>
                </sdu:card>
            </twbs:column>
        </twbs:row>
    </div>
</div>


<script type="text/template" id="user-template">
<twbs:tr>
    <twbs:td>{0}</twbs:td>
    <twbs:td>{1}</twbs:td>
    <twbs:td>{2}</twbs:td>
    <twbs:td>{3}</twbs:td>
    <twbs:td>{4}</twbs:td>
    <twbs:td>{5}</twbs:td>
    <twbs:td>
        <twbs:buttonToolbar>
            <twbs:buttonGroup>
                <twbs:button class="edit-button"><fa:icon icon="${FaIcon.EDIT}"/></twbs:button>
            </twbs:buttonGroup>
        </twbs:buttonToolbar>
    </twbs:td>
</twbs:tr>
</script>

<script type="text/template" id="sso-template">
<asset:image src="sdu_branch.png" class="sdu-logo-button"/>
</script>

<script type="text/template" id="role-template-ROLE_STUDENT">
<fa:icon icon="${FaIcon.GRADUATION_CAP}"/>
</script>

<script type="text/template" id="role-template-ROLE_TEACHER">
<fa:icon icon="${FaIcon.LOCK}"/>
</script>

<asset:javascript src="accountManagement/management.js"/>
<script>
    $(function () {
        AccountManagement.init();
        AccountManagement.registerUserEndpoint("<g:createLink action="users" />");
        AccountManagement.registerSaveEndpoint("<g:createLink action="updateUser" />");
        AccountManagement.refreshUsers();
    });
</script>
</body>
</html>
