<%@ page import="dk.danthrane.twbs.FormTagLib; dk.danthrane.TagLibUtils; dk.danthrane.twbs.Validation" %>
%{-- TODO: Doesn't render properly when size is set --}%
<div class="form-group ${validationClass} ${sizeClass} ${clazz}">
    <g:render template="/twbs/form/defaultLabel" model="${pageScope.variables}" />

    <g:render template="/twbs/form/openHorizontalWrapper" model="${pageScope.variables}" />

    <textarea name="${name}" class="form-control" id="${id}" ${raw(placeholder)}
        ${disabled} ${raw(TagLibUtils.expandAttributes(attrs))}>${value}</textarea>

    <g:render template="/twbs/form/closeHorizontalWrapper" model="${pageScope.variables}" />

    <g:if test="${validation != Validation.DEFAULT}">
        <twbs:icon icon="${validation.icon}" class="form-control-feedback" />
        <span class="sr-only">(${validation.name().toLowerCase()})</span>
    </g:if>

    <g:render template="/twbs/form/defaultHelp" model="${pageScope.variables}" />
</div>
