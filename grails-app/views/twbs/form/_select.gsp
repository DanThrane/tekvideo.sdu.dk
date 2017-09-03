<%@ page import="dk.danthrane.TagLibUtils; dk.danthrane.twbs.Validation" %>
<div class="form-group ${validationClass} ${sizeClass} ${clazz}">
    <g:render template="/twbs/form/defaultLabel" model="${pageScope.variables}" />

    <g:render template="/twbs/form/openHorizontalWrapper" model="${pageScope.variables}" />

    <select class='form-control' name='${name}' ${multiple} id='${id}' ${disabled} ${raw(TagLibUtils.expandAttributes(attrs))}>
        <g:each in="${list}" var="item">
            <option value="${item.value}" ${item.selected}>${item.message}</option>
        </g:each>
    </select>

    <g:render template="/twbs/form/closeHorizontalWrapper" model="${pageScope.variables}" />

    <g:render template="/twbs/form/defaultHelp" model="${pageScope.variables}" />
</div>